package main

import (
	"flag"
	"fmt"
	"net/http"
	"time"

	"github.com/adkham01/stay_fit/internal/app"
	"github.com/adkham01/stay_fit/internal/routes"
)

func main() {
	var port int
	flag.IntVar(&port, "port", 8080, "go backend server port")
	flag.Parse()

	application, err := app.NewApplication()
	if err != nil {
		panic(err)
	}
	defer application.DB.Close()

	r := routes.SetUpRoutes(application)

	server := &http.Server{
		Addr:         fmt.Sprintf(":%d", port),
		Handler:      r,
		IdleTimeout:  time.Minute,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 30 * time.Second,
	}
	application.Logger.Printf("We are listening on port %d\n", port)

	err = server.ListenAndServe()
	if err != nil {
		application.Logger.Fatal(err)
	}
}
