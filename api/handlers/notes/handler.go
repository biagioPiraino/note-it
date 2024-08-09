package notes

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"net/http"
	helper "note-it/helpers/handlers"
	notesServices "note-it/services/notes"
	"note-it/structs"
	"note-it/structs/api"
)

func GetNotes(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	notes := notesServices.GetNotes(dbSingleton, userId)
	c.IndentedJSON(http.StatusOK, notes)
}

func GetNote(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	id := c.Param("id")

	note, found := notesServices.GetNote(dbSingleton, userId, id)

	if !found {
		c.IndentedJSON(http.StatusNotFound, gin.H{"message": fmt.Sprintf("note %s not found", id)})
		return
	}

	c.IndentedJSON(http.StatusOK, note)
}

func AddNote(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	var newNote api.Note

	if err := c.BindJSON(&newNote); err != nil {
		c.IndentedJSON(http.StatusBadRequest, gin.H{"message": "note has bad format"})
		return
	}

	savedNote, success := notesServices.AddNote(dbSingleton, userId, newNote)

	if !success {
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"error": "an error occurred while creating a new note"})
		return
	}

	c.IndentedJSON(http.StatusCreated, savedNote)
}

func UpdateNote(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	id := c.Param("id")

	var updatedNote api.Note

	if err := c.BindJSON(&updatedNote); err != nil {
		c.IndentedJSON(http.StatusBadRequest, gin.H{"message": "note has bad format"})
		return
	}

	savedNote, success := notesServices.UpdateNote(dbSingleton, userId, updatedNote)

	if !success {
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"error": fmt.Sprintf("an error occurred while updating note %s", id)})
		return
	}

	c.IndentedJSON(http.StatusOK, savedNote)
}

func DeleteNote(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	id := c.Param("id")

	success := notesServices.DeleteNote(dbSingleton, userId, id)

	if !success {
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"error": fmt.Sprintf("an error occurred while deleting note %s", id)})
		return
	}

	c.IndentedJSON(http.StatusNoContent, gin.H{})
}
