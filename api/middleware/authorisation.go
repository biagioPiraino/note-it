package middleware

import (
	"context"
	"github.com/gin-gonic/gin"
	adapter "github.com/gwatts/gin-adapter"
	"log"
	"net/http"
	"net/url"
	"os"
	"time"

	jwtmiddleware "github.com/auth0/go-jwt-middleware/v2"
	"github.com/auth0/go-jwt-middleware/v2/jwks"
	"github.com/auth0/go-jwt-middleware/v2/validator"
)

type CustomClaims struct {
	Scope string `json:"scope"`
}

func AddAuthorisationMiddleware(router *gin.Engine) {
	authMiddleware := createMiddleware()

	// In order to use the auth0 go jwt middleware package within
	// the Gin framework we need an adapter to wrap the auth0
	// middleware handler within the Gin one
	router.Use(adapter.Wrap(authMiddleware.CheckJWT))
}

// Validate is empty and used to satisfy the validator.CustomClaims interface.
func (c CustomClaims) Validate(_ context.Context) error {
	return nil
}

func createMiddleware() *jwtmiddleware.JWTMiddleware {
	issuerURL, _ := url.Parse("https://" + os.Getenv("AUTH0_DOMAIN") + "/")
	audience := os.Getenv("AUTH0_AUDIENCE")

	provider := jwks.NewCachingProvider(issuerURL, 5*time.Minute)

	jwtValidator, err := validator.New(
		provider.KeyFunc,
		validator.RS256,
		issuerURL.String(),
		[]string{audience},
		validator.WithCustomClaims(
			func() validator.CustomClaims {
				return &CustomClaims{}
			},
		),
		validator.WithAllowedClockSkew(time.Minute),
	)
	if err != nil {
		log.Fatalf("Failed to set up the jwt validator")
	}

	errorHandler := func(w http.ResponseWriter, r *http.Request, err error) {
		log.Printf("Encountered error while validating JWT: %v", err)

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusUnauthorized)
		_, err = w.Write([]byte(`{"message":"Failed to validate JWT."}`))
		if err != nil {
			log.Fatalf("An error occurred while returning the unauthorised results.")
		}
	}

	middleware := jwtmiddleware.New(
		jwtValidator.ValidateToken,
		jwtmiddleware.WithErrorHandler(errorHandler),
	)

	return middleware
}
