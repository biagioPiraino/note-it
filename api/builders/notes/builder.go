package notes

import (
	"note-it/structs/api"
	"note-it/structs/domains"
)

func BuildApiStruct(note domains.Note, category api.Category) api.Note {
	return api.Note{
		Id:         note.NoteId,
		CreatedAt:  note.CreatedAt,
		ModifiedAt: note.ModifiedAt,
		Title:      note.Title,
		Summary:    note.Summary,
		Content:    note.Content,
		Category:   category,
	}
}

func BuildDomainStruct(userId string, note api.Note) domains.Note {
	return domains.Note{
		UserId:     userId,
		NoteId:     note.Id,
		CategoryId: note.Category.Id,
		CreatedAt:  note.CreatedAt,
		ModifiedAt: note.ModifiedAt,
		Title:      note.Title,
		Summary:    note.Summary,
		Content:    note.Content,
	}
}
