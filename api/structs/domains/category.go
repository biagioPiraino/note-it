package domains

import "note-it/structs/constants"

type Category struct {
	Id     string                 `bson:"id"`
	Name   string                 `bson:"name"`
	Colour string                 `bson:"colour"`
	Type   constants.CategoryType `bson:"type"`
}
