package users

import (
	"github.com/gin-gonic/gin"
	"net/http"
	helper "note-it/helpers/handlers"
	usersServices "note-it/services/users"
	"note-it/structs/api"
)

func UpdateUser(c *gin.Context) {
	userId := helper.GetClaimSubject(c)
	var updateUser api.User

	if err := c.BindJSON(&updateUser); err != nil {
		c.IndentedJSON(http.StatusBadRequest, gin.H{"message": "user has bad format"})
		return
	}

	success := usersServices.UpdateUser(userId, updateUser)
	if !success {
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"error": "an error occurred while updating user"})
		return
	}

	c.IndentedJSON(http.StatusOK, nil)
}
