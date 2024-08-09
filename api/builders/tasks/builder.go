package tasks

import (
	"note-it/structs/api"
	"note-it/structs/constants"
	"note-it/structs/domains"
)

func BuildApiStruct(task domains.Task, category api.Category) api.Task {
	apiStruct := api.Task{
		Id:          task.Id,
		Title:       task.Title,
		Content:     task.Content,
		Status:      int(task.Status),
		Category:    category,
		ScheduledAt: task.ScheduledAt,
		CreatedAt:   task.CreatedAt,
		ModifiedAt:  task.ModifiedAt,
	}

	return apiStruct
}

func BuildDomainStruct(userId string, task api.Task) domains.Task {
	return domains.Task{
		Id:          task.Id,
		UserId:      userId,
		CategoryId:  task.Category.Id,
		Title:       task.Title,
		Content:     task.Content,
		Status:      constants.TaskStatus(task.Status),
		CreatedAt:   task.CreatedAt,
		ModifiedAt:  task.ModifiedAt,
		ScheduledAt: task.ScheduledAt,
	}
}
