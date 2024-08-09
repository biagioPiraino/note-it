package categories

import (
	"context"
	"errors"
	"fmt"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	helper "note-it/helpers/repositories"
	"note-it/structs"
	"note-it/structs/constants"
	"note-it/structs/domains"
)

func GetUserCategories(dbSingleton *structs.Database, userId string) *domains.UserCategories {
	const collectionName = "categories"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.ComplexOperationsTimeout())
	defer cancel()

	filter := bson.D{{"user_id", userId}}
	var userCategories domains.UserCategories
	err := coll.FindOne(ctx, filter).Decode(&userCategories)

	if errors.Is(err, mongo.ErrNoDocuments) {
		fmt.Printf("No category was found for user %s", userId)
		return nil
	}

	if err != nil {
		panic(err)
	}

	return &userCategories
}

func GetCategories(dbSingleton *structs.Database, userId string) []domains.Category {
	userCategories := GetUserCategories(dbSingleton, userId)
	if userCategories == nil {
		return []domains.Category{}
	}
	return userCategories.Categories
}

func GetCategory(dbSingleton *structs.Database, userId string, categoryId string) (domains.Category, bool) {
	userCategories := GetUserCategories(dbSingleton, userId)
	if userCategories == nil {
		return domains.Category{}, false
	}

	for _, category := range userCategories.Categories {
		if category.Id == categoryId {
			return category, true
		}
	}

	return domains.Category{}, true
}

func GetCategoriesByType(dbSingleton *structs.Database, userId string, categoryType constants.CategoryType) []domains.Category {
	userCategories := GetUserCategories(dbSingleton, userId)
	if userCategories == nil {
		return []domains.Category{}
	}

	filteredCategories := make([]domains.Category, 0)

	for _, category := range userCategories.Categories {
		if category.Type == categoryType {
			filteredCategories = append(filteredCategories, category)
		}
	}

	return filteredCategories
}

func AddUserCategory(dbSingleton *structs.Database, userCategory domains.UserCategories) []domains.Category {
	const collectionName = "categories"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	_, err := coll.InsertOne(ctx, userCategory)
	if err != nil {
		panic(err)
	}

	return userCategory.Categories
}

func AppendCategory(dbSingleton *structs.Database, userId string, category domains.Category) []domains.Category {
	const collectionName = "categories"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	var userCategories domains.UserCategories
	filter := bson.D{{"user_id", userId}}

	err := coll.FindOne(ctx, filter).Decode(&userCategories)

	if errors.Is(err, mongo.ErrNoDocuments) {
		fmt.Printf("No category was found for user %s", userId)
		return make([]domains.Category, 0)
	}

	userCategories.Categories = append(userCategories.Categories, category)

	categories := updateUserCategories(coll, &filter, userCategories)
	return categories
}

func UpdateCategory(dbSingleton *structs.Database, userId string, categoryToUpdate domains.Category) []domains.Category {
	const collectionName = "categories"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	var userCategories domains.UserCategories
	filter := bson.D{{"user_id", userId}}

	err := coll.FindOne(ctx, filter).Decode(&userCategories)

	if errors.Is(err, mongo.ErrNoDocuments) {
		fmt.Printf("No category was found for user %s", userId)
		return make([]domains.Category, 0)
	}

	categoryIndex := getCategoryIndex(categoryToUpdate.Id, userCategories.Categories)

	if categoryIndex == -1 {
		fmt.Printf("No category %s was found. Aborting operation.", categoryToUpdate.Id)
		return make([]domains.Category, 0)
	}

	userCategories.Categories[categoryIndex] = categoryToUpdate

	categories := updateUserCategories(coll, &filter, userCategories)
	return categories
}

func RemoveCategory(dbSingleton *structs.Database, userId string, categoryId string) []domains.Category {
	const collectionName = "categories"
	coll := dbSingleton.Client.Database(*dbSingleton.DatabaseName).Collection(collectionName)

	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	var userCategories domains.UserCategories
	filter := bson.D{{"user_id", userId}}

	err := coll.FindOne(ctx, filter).Decode(&userCategories)

	if errors.Is(err, mongo.ErrNoDocuments) {
		fmt.Printf("No category was found for user %s", userId)
		return make([]domains.Category, 0)
	}

	categoryIndex := getCategoryIndex(categoryId, userCategories.Categories)

	if categoryIndex == -1 {
		fmt.Printf("No category %s was found. Aborting operation.", categoryId)
		return make([]domains.Category, 0)
	}

	copy(userCategories.Categories[categoryIndex:], userCategories.Categories[categoryIndex+1:])
	userCategories.Categories = userCategories.Categories[:len(userCategories.Categories)-1]

	categories := updateUserCategories(coll, &filter, userCategories)
	return categories
}

func updateUserCategories(coll *mongo.Collection, filter *bson.D, userCategories domains.UserCategories) []domains.Category {
	update := bson.D{{"$set", bson.D{{"categories", userCategories.Categories}}}}
	ctx, cancel := context.WithTimeout(context.Background(), helper.SimpleOperationsTimeout())
	defer cancel()

	_, err := coll.UpdateOne(ctx, filter, update)

	if err != nil {
		panic(err)
	}

	return userCategories.Categories
}

func getCategoryIndex(categoryId string, categories []domains.Category) int {
	for i, category := range categories {
		if category.Id == categoryId {
			return i
		}
	}
	return -1
}
