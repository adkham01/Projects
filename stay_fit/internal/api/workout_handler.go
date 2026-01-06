package api

import (
	"database/sql"
	"encoding/json"
	"errors"
	"log"
	"net/http"

	"github.com/adkham01/stay_fit/internal/middleware"
	"github.com/adkham01/stay_fit/internal/store"
	"github.com/adkham01/stay_fit/internal/utils"
)

type WorkoutHandler struct {
	workoutStore store.WorkoutStore
	logger       *log.Logger
}

func NewWorkoutHandler(workoutStore store.WorkoutStore, logger *log.Logger) *WorkoutHandler {
	return &WorkoutHandler{
		workoutStore: workoutStore,
		logger:       logger,
	}
}

func (wh *WorkoutHandler) HandleGetWorkoutById(w http.ResponseWriter, r *http.Request) {
	workoutId, err := utils.ReadIdParam(r)
	if err != nil {
		wh.logger.Printf("ERROR reading id param: %v", err)
		utils.WriteJson(w, http.StatusBadRequest, utils.Envelope{"ERROR": "invalid workout id"})
		return
	}

	workout, err := wh.workoutStore.GetWorkoutByID(workoutId)
	if err != nil {
		wh.logger.Printf("ERROR GetWorkoutById: %v", err)
		utils.WriteJson(w, http.StatusNotFound, utils.Envelope{"ERROR": "workout not found"})
		return
	}
	utils.WriteJson(w, http.StatusOK, utils.Envelope{"workout": workout})
}

func (wh *WorkoutHandler) HandleCreateWorkout(w http.ResponseWriter, r *http.Request) {
	var workout store.Workout
	err := json.NewDecoder(r.Body).Decode(&workout)
	if err != nil {
		wh.logger.Printf("ERROR failed decoding json %v", err)
		utils.WriteJson(w, http.StatusBadRequest, utils.Envelope{"ERROR": "invalid request body"})
		return
	}

	currentUser := middleware.GetUser(r)
	if currentUser == nil || currentUser == store.AnonymousUser {
		utils.WriteJson(w, http.StatusUnauthorized, utils.Envelope{"ERROR": "You must be logged in"})
		return
	}

	createdWorkout, err := wh.workoutStore.CreateWorkout(&workout)
	if err != nil {
		wh.logger.Printf("ERROR failed to create workout %v", err)
		utils.WriteJson(w, http.StatusInternalServerError, utils.Envelope{"ERROR": "failed to create workout"})
		return
	}
	utils.WriteJson(w, http.StatusCreated, utils.Envelope{"workout": createdWorkout})
}

func (wh *WorkoutHandler) HandleUpdateWorkout(w http.ResponseWriter, r *http.Request) {
	workoutId, err := utils.ReadIdParam(r)
	if err != nil {
		wh.logger.Printf("ERROR reading id param: %v", err)
		utils.WriteJson(w, http.StatusBadRequest, utils.Envelope{"ERROR": "invalid workout update id"})
		return
	}

	existingWorkout, err := wh.workoutStore.GetWorkoutByID(workoutId)
	if err != nil {
		wh.logger.Printf("ERROR getWorkoutById error: %v", err)
		utils.WriteJson(w, http.StatusInternalServerError, utils.Envelope{"ERROR": "internal server error"})
		return
	}

	if existingWorkout == nil {
		wh.logger.Printf("ERROR failed to fetch workout error: %v", err)
		utils.WriteJson(w, http.StatusNotFound, utils.Envelope{"ERROR": "not found"})
		return
	}

	// at this point we can assume we are able to find an existing workout
	var updateWorkoutRequest struct {
		Title           *string              `json:"title"`
		Description     *string              `json:"description"`
		DurationMinutes *int                 `json:"duration_minutes"`
		CaloriesBurned  *int                 `json:"calories_burned"`
		Entries         []store.WorkoutEntry `json:"entries"`
	}

	err = json.NewDecoder(r.Body).Decode(&updateWorkoutRequest)
	if err != nil {
		wh.logger.Printf("ERROR decodingUpdateRequest: %v", err)
		utils.WriteJson(w, http.StatusBadRequest, utils.Envelope{"ERROR": "invalid request"})
		return
	}

	if updateWorkoutRequest.Title != nil {
		existingWorkout.Title = *updateWorkoutRequest.Title
	}
	if updateWorkoutRequest.Description != nil {
		existingWorkout.Description = *updateWorkoutRequest.Description
	}
	if updateWorkoutRequest.DurationMinutes != nil {
		existingWorkout.DurationMinutes = *updateWorkoutRequest.DurationMinutes
	}
	if updateWorkoutRequest.CaloriesBurned != nil {
		existingWorkout.CaloriesBurned = *updateWorkoutRequest.CaloriesBurned
	}
	if updateWorkoutRequest.Entries != nil {
		existingWorkout.Entries = updateWorkoutRequest.Entries
	}

	currentUser := middleware.GetUser(r)
	if currentUser == nil || currentUser == store.AnonymousUser {
		utils.WriteJson(w, http.StatusUnauthorized, utils.Envelope{"ERROR": "You must be logged in"})
		return
	}

	workoutOwner, err := wh.workoutStore.GetWorkoutOwner(workoutId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			wh.logger.Printf("ERROR UpdateingRequest: %v", err)
			utils.WriteJson(w, http.StatusNotFound, utils.Envelope{"ERROR": "Workout does not exist"})
			return
		}
		utils.WriteJson(w, http.StatusInternalServerError, utils.Envelope{"ERROR": "internal server error"})
		return
	}

	if workoutOwner != currentUser.ID {
		utils.WriteJson(w, http.StatusForbidden, utils.Envelope{"ERROR": "you are not authorized to update this workout"})
		return
	}

	err = wh.workoutStore.UpdateWorkout(existingWorkout)
	if err != nil {
		wh.logger.Printf("ERROR UpdateingRequest: %v", err)
		utils.WriteJson(w, http.StatusInternalServerError, utils.Envelope{"ERROR": "internal server error"})
		return
	}

	utils.WriteJson(w, http.StatusOK, utils.Envelope{"workout": existingWorkout})
}

func (wh *WorkoutHandler) HandleDeleteWorkoutById(w http.ResponseWriter, r *http.Request) {

	workoutId, err := utils.ReadIdParam(r)
	if err != nil {
		wh.logger.Printf("ERROR reading id param: %v", err)
		utils.WriteJson(w, http.StatusBadRequest, utils.Envelope{"ERROR": "invalid workout update id"})
		return
	}
	var currentUser = middleware.GetUser(r)
	if currentUser == nil || currentUser == store.AnonymousUser {
		utils.WriteJson(w, http.StatusUnauthorized, utils.Envelope{"ERROR": "You must be logged in"})
		return
	}

	workoutOwner, err := wh.workoutStore.GetWorkoutOwner(workoutId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			wh.logger.Printf("ERROR UpdateingRequest: %v", err)
			utils.WriteJson(w, http.StatusNotFound, utils.Envelope{"ERROR": "Workout does not exist"})
			return
		}
		utils.WriteJson(w, http.StatusInternalServerError, utils.Envelope{"ERROR": "internal server error"})
		return
	}
	if workoutOwner != currentUser.ID {
		utils.WriteJson(w, http.StatusForbidden, utils.Envelope{"ERROR": "you are not authorized to update this workout"})
		return
	}

	err = wh.workoutStore.DeleteWorkoutByID(workoutId)
	if errors.Is(err, sql.ErrNoRows) {
		wh.logger.Printf("ERROR deletingworkout: %v", err)
		utils.WriteJson(w, http.StatusNotFound, utils.Envelope{"ERROR": "not found"})
		return
	}

	if err != nil {
		wh.logger.Printf("ERROR deletingworkout: %v", err)
		utils.WriteJson(w, http.StatusInternalServerError, utils.Envelope{"ERROR": "internal server error"})
		return
	}

	w.WriteHeader(http.StatusNoContent)
	utils.WriteJson(w, http.StatusNoContent, utils.Envelope{"workout": nil})

}
