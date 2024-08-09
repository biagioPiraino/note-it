package tasks

import (
	"context"
	"errors"
	"fmt"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	helper "note-it/helpers/repositories"
	"note-it/structs"
	"note-it/structs/domains"
)

func GetTasks(dbSingleton *structs.Database, userId string) []domains.Task {
	const collectionName = "tasks"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.ComplexOperationsTimeout())
	defer cancel()

	var tasks []domains.Task

	filter := bson.D{{"user_id", userId}}
	cursor, err := coll.Find(ctx, filter)
	defer func(cursor *mongo.Cursor, ctx context.Context) {
		cursorErr := cursor.Close(ctx)
		if cursorErr != nil {
			panic(cursorErr)
		}
	}(cursor, ctx)

	if err != nil {
		panic(err)
	}

	if err = cursor.All(ctx, &tasks); err != nil {
		panic(err)
	}

	// Initialise the slice in case is nil
	if tasks == nil {
		tasks = []domains.Task{}
	}

	return tasks
}

func GetTask(dbSingleton *structs.Database, userId string, taskId string) (domains.Task, bool) {
	const collectionName = "tasks"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	var task domains.Task

	filter := bson.D{{
		"$and", bson.A{
			bson.D{{"user_id", userId}},
			bson.D{{"task_id", taskId}},
		},
	}}

	err := coll.FindOne(ctx, filter).Decode(&task)

	if errors.Is(err, mongo.ErrNoDocuments) {
		fmt.Printf("No document was found for user %s and task %s", userId, taskId)
		return task, false
	}

	if err != nil {
		panic(err)
	}

	return task, true
}

func AddTask(dbSingleton *structs.Database, task domains.Task) bool {
	const collectionName = "tasks"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	_, err := coll.InsertOne(ctx, task)
	if err != nil {
		panic(err)
	}

	return true
}

func UpdateTask(dbSingleton *structs.Database, task domains.Task) bool {
	const collectionName = "tasks"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	filter := bson.D{{
		"$and", bson.A{
			bson.D{{"user_id", task.UserId}},
			bson.D{{"task_id", task.Id}},
		},
	}}

	_, err := coll.ReplaceOne(ctx, filter, task)
	if err != nil {
		panic(err)
	}

	return true
}

func DeleteTask(dbSingleton *structs.Database, userId string, taskId string) bool {
	const collectionName = "tasks"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	filter := bson.D{{
		"$and", bson.A{
			bson.D{{"user_id", userId}},
			bson.D{{"task_id", taskId}},
		},
	}}

	_, err := coll.DeleteOne(ctx, filter)
	if err != nil {
		panic(err)
	}

	return true
}
