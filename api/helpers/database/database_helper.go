package database

import (
	"gopkg.in/yaml.v3"
	"log"
	"note-it/structs"
	"os"
	"strings"
)

func LoadDatabaseConfiguration() structs.DatabaseConfig {
	f, err := os.Open("database_config.yml")

	if err != nil {
		return structs.DatabaseConfig{}
	}

	defer func(f *os.File) {
		err := f.Close()
		if err != nil {
			panic(err)
		}
	}(f)

	var cfg structs.DatabaseConfig
	decoder := yaml.NewDecoder(f)
	err = decoder.Decode(&cfg)

	if err != nil {
		return structs.DatabaseConfig{}
	}

	updateConnStringWithCredentials(&cfg)
	retrieveDatabaseName(&cfg)

	return cfg
}

func retrieveDatabaseName(cfg *structs.DatabaseConfig) {
	databaseName := os.Getenv("DATABASE_NAME")

	if databaseName == "" {
		errorMsg := strings.Join([]string{
			"Database name not specified",
			"Please review your configuration and/or set appropriate env variables."}, ". ")

		log.Fatal(errorMsg)
	}

	cfg.Database.Name = databaseName
}

func updateConnStringWithCredentials(cfg *structs.DatabaseConfig) {
	username := os.Getenv("DATABASE_USERNAME")
	password := os.Getenv("DATABASE_PASSWORD")

	if username == "" || password == "" {
		errorMsg := strings.Join([]string{
			"Database connection string not correctly set",
			"Please review your configuration and/or set appropriate env variables."}, ". ")

		log.Fatal(errorMsg)
	}

	cfg.Database.Connection =
		strings.Replace(cfg.Database.Connection, "<username>", username, 1)

	cfg.Database.Connection =
		strings.Replace(cfg.Database.Connection, "<password>", password, 1)
}
