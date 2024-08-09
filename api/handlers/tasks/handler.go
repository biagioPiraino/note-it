package tasks

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"net/http"
	helper "note-it/helpers/handlers"
	tasksServices "note-it/services/tasks"
	"note-it/structs"
	"note-it/structs/api"
)

func GetTasks(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	tasks := tasksServices.GetTasks(dbSingleton, userId)
	c.IndentedJSON(http.StatusOK, tasks)
}

func GetTask(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	id := c.Param("id")

	task, found := tasksServices.GetTask(dbSingleton, userId, id)

	if !found {
		c.IndentedJSON(http.StatusNotFound, gin.H{"message": fmt.Sprintf("task %s not found", id)})
		return
	}

	c.IndentedJSON(http.StatusOK, task)
}

func AddTask(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	var newTask api.Task

	if err := c.Bind(&newTask); err != nil {
		c.IndentedJSON(http.StatusBadRequest, gin.H{"message": fmt.Sprintf("task has bad format: %s", err.Error())})
		return
	}

	savedTask, success := tasksServices.AddTask(dbSingleton, userId, newTask)

	if !success {
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": "an error occurred while saving task"})
		return
	}

	c.IndentedJSON(http.StatusCreated, savedTask)
}

func UpdateTask(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	id := c.Param("id")

	var updatedTask api.Task

	if err := c.Bind(&updatedTask); err != nil {
		c.IndentedJSON(http.StatusBadRequest, gin.H{"message": "task has bad format"})
		return
	}

	savedTask, success := tasksServices.UpdateTask(dbSingleton, userId, updatedTask)

	if !success {
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": fmt.Sprintf("an error occurred while updating task %s", id)})
		return
	}

	c.IndentedJSON(http.StatusOK, savedTask)
}

func DeleteTask(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	id := c.Param("id")

	success := tasksServices.DeleteTask(dbSingleton, userId, id)

	if !success {
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": fmt.Sprintf("an error occurred while deleting task %s", id)})
		return
	}

	c.IndentedJSON(http.StatusNoContent, gin.H{})
}
