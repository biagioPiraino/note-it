package structs

import "go.mongodb.org/mongo-driver/mongo"

type Database struct {
	Client       *mongo.Client
	DatabaseName *string
}
