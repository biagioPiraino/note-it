package categories

import (
	"github.com/google/uuid"
	builder "note-it/builders/categories"
	repository "note-it/repositories/categories"
	"note-it/structs"
	"note-it/structs/api"
	"note-it/structs/constants"
	"note-it/structs/domains"
)

func GetCategories(dbSingleton *structs.Database, userId string) []api.Category {
	categories := repository.GetCategories(dbSingleton, userId)
	apiCategories := buildApiStructs(categories)
	return apiCategories
}

func GetCategoriesByType(dbSingleton *structs.Database, userId string, categoryType constants.CategoryType) []api.Category {
	categories := repository.GetCategoriesByType(dbSingleton, userId, categoryType)
	apiCategories := buildApiStructs(categories)
	return apiCategories
}

func GetCategory(dbSingleton *structs.Database, userId string, categoryId string) (api.Category, bool) {
	category, found := repository.GetCategory(dbSingleton, userId, categoryId)

	if !found {
		return api.Category{}, false
	}

	apiCategory := builder.BuildApiStruct(category)
	return apiCategory, found
}

func AddCategory(dbSingleton *structs.Database, userId string, category api.Category) []api.Category {
	category.Id = uuid.New().String()
	domainStruct := builder.BuildDomainStruct(category)
	userCategories := repository.GetUserCategories(dbSingleton, userId)

	if userCategories == nil {
		entityToCreate := domains.UserCategories{UserId: userId, Categories: []domains.Category{domainStruct}}
		categories := repository.AddUserCategory(dbSingleton, entityToCreate)
		apiCategories := buildApiStructs(categories)
		return apiCategories
	}

	categories := repository.AppendCategory(dbSingleton, userId, domainStruct)
	apiCategories := buildApiStructs(categories)
	return apiCategories
}

func UpdateCategory(dbSingleton *structs.Database, userId string, category api.Category) []api.Category {
	domainStruct := builder.BuildDomainStruct(category)
	categories := repository.UpdateCategory(dbSingleton, userId, domainStruct)
	apiCategories := buildApiStructs(categories)
	return apiCategories
}

func RemoveCategory(dbSingleton *structs.Database, userId string, categoryId string) []api.Category {
	categories := repository.RemoveCategory(dbSingleton, userId, categoryId)
	apiCategories := buildApiStructs(categories)
	return apiCategories
}

func buildApiStructs(categories []domains.Category) []api.Category {
	apiCategories := make([]api.Category, 0)

	for _, category := range categories {
		apiCategories = append(apiCategories, builder.BuildApiStruct(category))
	}

	return apiCategories
}
