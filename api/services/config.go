package services

import (
	"github.com/joho/godotenv"
	"log"
)

func LoadConfiguration() {
	if err := godotenv.Load(); err != nil {
		log.Fatalf("Error loading the .env file: %v", err)
	}
}
