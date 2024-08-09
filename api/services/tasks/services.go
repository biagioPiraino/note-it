package tasks

import (
	"github.com/google/uuid"
	builder "note-it/builders/tasks"
	repository "note-it/repositories/tasks"
	catService "note-it/services/categories"
	"note-it/structs"
	"note-it/structs/api"
	"note-it/structs/constants"
	"note-it/structs/domains"
	"time"
)

func GetTasks(dbSingleton *structs.Database, userId string) []api.Task {
	tasks := repository.GetTasks(dbSingleton, userId)
	categories := catService.GetCategories(dbSingleton, userId)
	apiTasks := buildApiStructs(tasks, categories)
	return apiTasks
}

func GetTask(dbSingleton *structs.Database, userId string, taskId string) (api.Task, bool) {
	task, found := repository.GetTask(dbSingleton, userId, taskId)
	if !found {
		return api.Task{}, found
	}

	category, categoryFound := catService.GetCategory(dbSingleton, userId, task.CategoryId)

	if !categoryFound {
		category = buildEmptyApiCategory()
	}

	apiTask := builder.BuildApiStruct(task, category)
	return apiTask, true
}

func AddTask(dbSingleton *structs.Database, userId string, task api.Task) (api.Task, bool) {
	task.Id = uuid.New().String()
	task.CreatedAt = time.Now()
	task.ModifiedAt = time.Now()

	domainTask := builder.BuildDomainStruct(userId, task)
	success := repository.AddTask(dbSingleton, domainTask)
	return task, success
}
func UpdateTask(dbSingleton *structs.Database, userId string, task api.Task) (api.Task, bool) {
	task.ModifiedAt = time.Now()

	domainTask := builder.BuildDomainStruct(userId, task)
	success := repository.UpdateTask(dbSingleton, domainTask)
	return task, success
}

func DeleteTask(dbSingleton *structs.Database, userId string, taskId string) bool {
	return repository.DeleteTask(dbSingleton, userId, taskId)
}

func buildApiStructs(tasks []domains.Task, categories []api.Category) []api.Task {
	apiTasks := make([]api.Task, 0)
	mappedCategories := map[string]api.Category{}

	for _, category := range categories {
		mappedCategories[category.Id] = category
	}

	for _, task := range tasks {
		category, found := mappedCategories[task.CategoryId]

		if !found {
			category = buildEmptyApiCategory()
		}

		apiTasks = append(apiTasks, builder.BuildApiStruct(task, category))
	}

	return apiTasks
}

func buildEmptyApiCategory() api.Category {
	return api.Category{Id: "", Name: "Unassigned", Colour: "#FFFFFF", Type: int(constants.Task)}
}
