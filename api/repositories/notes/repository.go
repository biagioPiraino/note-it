package notes

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

func GetNotes(dbSingleton *structs.Database, userId string) []domains.Note {
	const collectionName = "notes"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.ComplexOperationsTimeout())
	defer cancel()

	var notes []domains.Note

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

	if err = cursor.All(ctx, &notes); err != nil {
		panic(err)
	}

	// Initialise the slice in case is nil
	if notes == nil {
		notes = []domains.Note{}
	}

	return notes
}

func GetNote(dbSingleton *structs.Database, userId string, noteId string) (domains.Note, bool) {
	const collectionName = "notes"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	var note domains.Note

	filter := bson.D{{
		"$and", bson.A{
			bson.D{{"user_id", userId}},
			bson.D{{"note_id", noteId}},
		},
	}}

	err := coll.FindOne(ctx, filter).Decode(&note)

	if errors.Is(err, mongo.ErrNoDocuments) {
		fmt.Printf("No document was found for user %s and note %s", userId, noteId)
		return note, false
	}

	if err != nil {
		panic(err)
	}

	return note, true
}

func AddNote(dbSingleton *structs.Database, note domains.Note) bool {
	const collectionName = "notes"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	_, err := coll.InsertOne(ctx, note)
	if err != nil {
		panic(err)
	}

	return true
}

func UpdateNote(dbSingleton *structs.Database, note domains.Note) bool {
	const collectionName = "notes"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	filter := bson.D{{
		"$and", bson.A{
			bson.D{{"user_id", note.UserId}},
			bson.D{{"note_id", note.NoteId}},
		},
	}}

	_, err := coll.ReplaceOne(ctx, filter, note)
	if err != nil {
		panic(err)
	}

	return true
}

func DeleteNote(dbSingleton *structs.Database, userId string, noteId string) bool {
	const collectionName = "notes"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	filter := bson.D{{
		"$and", bson.A{
			bson.D{{"user_id", userId}},
			bson.D{{"note_id", noteId}},
		},
	}}

	_, err := coll.DeleteOne(ctx, filter)

	if err != nil {
		panic(err)
	}

	return true
}
