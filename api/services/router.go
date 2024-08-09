package services

import (
	"context"
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	helper "note-it/helpers/router"
	"note-it/middleware"
	"note-it/structs"
	"os"
	"strings"
	"time"
)

func LaunchApiRouter(dbSingleton *structs.Database) {
	// Initialise the router
	router := gin.Default()

	// Load router configuration
	routerConfig := helper.LoadRouterConfiguration()

	// Adding CORS policy
	router.Use(cors.New(cors.Config{
		AllowOrigins:     []string{os.Getenv("NOTE_IT_CLIENT")},
		AllowMethods:     []string{"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"},
		AllowHeaders:     []string{"Origin", "Content-Type", "Authorization"},
		ExposeHeaders:    []string{"Content-Length"},
		AllowCredentials: true,
		MaxAge:           12 * time.Hour,
	}))

	// Adding middlewares
	middleware.AddAuthorisationMiddleware(router)

	// Adding handlers
	helper.AddApiRoutes(router, dbSingleton)

	// Define router address
	fullAddress := strings.Join(
		[]string{routerConfig.Server.Host, routerConfig.Server.Port}, ":")

	// Run router
	err := router.Run(fullAddress)

	// In case we have any error, we panic and display the error messages
	if err != nil {
		panic(err)
	}

	// Defer function are triggered when surrounding function returns,
	// in this case it disconnect the client after the end of this function
	defer func() {
		if err := dbSingleton.Client.Disconnect(context.Background()); err != nil {
			panic(err)
		}
	}()
}
