package notes

import (
	"github.com/google/uuid"
	builder "note-it/builders/notes"
	repository "note-it/repositories/notes"
	catService "note-it/services/categories"
	"note-it/structs"
	"note-it/structs/api"
	"note-it/structs/constants"
	"note-it/structs/domains"
	"time"
)

func GetNotes(dbSingleton *structs.Database, userId string) []api.Note {
	notes := repository.GetNotes(dbSingleton, userId)
	allCategories := catService.GetCategories(dbSingleton, userId)
	apiNotes := buildApiStructs(notes, allCategories)
	return apiNotes
}

func GetNote(dbSingleton *structs.Database, userId string, noteId string) (api.Note, bool) {
	note, found := repository.GetNote(dbSingleton, userId, noteId)
	if !found {
		return api.Note{}, found
	}

	category, found := catService.GetCategory(dbSingleton, userId, note.CategoryId)

	if !found {
		category = buildEmptyApiCategory()
	}

	apiNote := builder.BuildApiStruct(note, category)
	return apiNote, true
}

func AddNote(dbSingleton *structs.Database, userId string, note api.Note) (api.Note, bool) {
	note.Id = uuid.New().String()
	note.CreatedAt = time.Now()
	note.ModifiedAt = time.Now()

	domainNote := builder.BuildDomainStruct(userId, note)
	success := repository.AddNote(dbSingleton, domainNote)
	return note, success
}

func UpdateNote(dbSingleton *structs.Database, userId string, note api.Note) (api.Note, bool) {
	note.ModifiedAt = time.Now()

	domainNote := builder.BuildDomainStruct(userId, note)
	success := repository.UpdateNote(dbSingleton, domainNote)
	return note, success
}

func DeleteNote(dbSingleton *structs.Database, userId string, noteId string) bool {
	return repository.DeleteNote(dbSingleton, userId, noteId)
}

func buildApiStructs(notes []domains.Note, categories []api.Category) []api.Note {
	apiNotes := make([]api.Note, 0)
	mappedCategories := map[string]api.Category{}

	for _, category := range categories {
		mappedCategories[category.Id] = category
	}

	for _, note := range notes {
		category, found := mappedCategories[note.CategoryId]

		if !found {
			category = buildEmptyApiCategory()
		}

		apiNotes = append(apiNotes, builder.BuildApiStruct(note, category))
	}

	return apiNotes
}

func buildEmptyApiCategory() api.Category {
	return api.Category{Id: "", Name: "Unassigned", Colour: "#FFFFFF", Type: int(constants.Note)}
}
