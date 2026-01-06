package api

import (
	"encoding/json"
	"log"
	"net/http"
	"time"

	"github.com/adkham01/stay_fit/internal/store"
	"github.com/adkham01/stay_fit/internal/tokens"
	"github.com/adkham01/stay_fit/internal/utils"
)

type TokenHandler struct {
	tokenStore store.TokenStore
	userStore  store.UserStore
	logger     *log.Logger
}

type createTokenRequest struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

func NewTokenHandler(tokenStore store.TokenStore, userStore store.UserStore, logger *log.Logger) *TokenHandler {
	return &TokenHandler{
		tokenStore: tokenStore,
		userStore:  userStore,
		logger:     logger,
	}
}

func (h *TokenHandler) HandleCreateToken(w http.ResponseWriter, r *http.Request) {
	var req createTokenRequest
	err := json.NewDecoder(r.Body).Decode(&req)

	if err != nil {
		h.logger.Printf("ERROR: createTokenREquest: %v", err)
		utils.WriteJson(w, http.StatusBadRequest, utils.Envelope{"ERROR": "invalid request payload"})
		return
	}

	user, err := h.userStore.GetUserByUsername(req.Username)
	if err != nil || user == nil {
		h.logger.Printf("ERROR: GetUserByUsername: %v", err)
		utils.WriteJson(w, http.StatusInternalServerError, utils.Envelope{"ERROR": "internal server error"})
		return
	}
	doesPasswordMatch, err := user.PasswordHash.Matches(req.Password)
	if err != nil {
		h.logger.Printf("ERROR: PasswordHash.Matches: %v", err)
		utils.WriteJson(w, http.StatusInternalServerError, utils.Envelope{"ERROR": "internal server error"})
		return
	}

	if !doesPasswordMatch {
		h.logger.Println("ERROR: password does not match")
		utils.WriteJson(w, http.StatusUnauthorized, utils.Envelope{"ERROR": "invalid credentials"})
		return
	}

	token, err := h.tokenStore.CreateNewToken(user.ID, 24*time.Hour, tokens.ScopeAuth)
	if err != nil {
		h.logger.Printf("ERROR: CreateNewToken: %v", err)
		utils.WriteJson(w, http.StatusInternalServerError, utils.Envelope{"ERROR": "internal server error"})
		return
	}

	utils.WriteJson(w, http.StatusOK, utils.Envelope{"auth_token": token})

}
