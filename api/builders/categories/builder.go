package categories

import (
	api "note-it/structs/api"
	"note-it/structs/constants"
	"note-it/structs/domains"
)

func BuildApiStruct(category domains.Category) api.Category {
	return api.Category{
		Id:     category.Id,
		Name:   category.Name,
		Colour: category.Colour,
		Type:   int(category.Type),
	}
}

func BuildDomainStruct(category api.Category) domains.Category {
	return domains.Category{
		Id:     category.Id,
		Name:   category.Name,
		Colour: category.Colour,
		Type:   constants.CategoryType(category.Type),
	}
}
