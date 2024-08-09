package services

import (
	"context"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	helper "note-it/helpers/database"
	"note-it/structs"
)

func InitialiseDatabase() *structs.Database {
	config := helper.LoadDatabaseConfiguration()
	instance := createDatabaseInstance(&config)
	return instance
}

func createDatabaseInstance(config *structs.DatabaseConfig) *structs.Database {
	client, err := mongo.Connect(context.Background(), options.Client().ApplyURI(config.Database.Connection))

	if err != nil {
		panic(err)
	}

	return &structs.Database{Client: client, DatabaseName: &config.Database.Name}
}
