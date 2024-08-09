package router

import (
	"github.com/gin-gonic/gin"
	"gopkg.in/yaml.v3"
	"note-it/handlers/categories"
	"note-it/handlers/notes"
	"note-it/handlers/tasks"
	"note-it/handlers/users"
	"note-it/structs"
	"os"
)

func LoadRouterConfiguration() structs.RouterConfig {
	f, err := os.Open("router_config.yml")

	if err != nil {
		return structs.RouterConfig{}
	}

	defer func(f *os.File) {
		err := f.Close()
		if err != nil {
			panic(err)
		}
	}(f)

	var cfg structs.RouterConfig
	decoder := yaml.NewDecoder(f)
	err = decoder.Decode(&cfg)

	if err != nil {
		return structs.RouterConfig{}
	}

	return cfg
}

func AddApiRoutes(router *gin.Engine, dbSingleton *structs.Database) {
	addNotesRoutes(router, dbSingleton)
	addTasksRoutes(router, dbSingleton)
	addCategoriesRoutes(router, dbSingleton)
	addUsersRoutes(router)
}

func addNotesRoutes(router *gin.Engine, dbSingleton *structs.Database) {
	router.GET("/api/notes", func(c *gin.Context) {
		notes.GetNotes(c, dbSingleton)
	})

	router.GET("/api/notes/:id", func(c *gin.Context) {
		notes.GetNote(c, dbSingleton)
	})

	router.POST("/api/notes", func(c *gin.Context) {
		notes.AddNote(c, dbSingleton)
	})

	router.PUT("/api/notes/:id", func(c *gin.Context) {
		notes.UpdateNote(c, dbSingleton)
	})

	router.DELETE("/api/notes/:id", func(c *gin.Context) {
		notes.DeleteNote(c, dbSingleton)
	})
}

func addTasksRoutes(router *gin.Engine, dbSingleton *structs.Database) {
	router.GET("/api/tasks", func(c *gin.Context) {
		tasks.GetTasks(c, dbSingleton)
	})

	router.GET("/api/tasks/:id", func(c *gin.Context) {
		tasks.GetTask(c, dbSingleton)
	})

	router.POST("/api/tasks", func(c *gin.Context) {
		tasks.AddTask(c, dbSingleton)
	})

	router.PUT("/api/tasks/:id", func(c *gin.Context) {
		tasks.UpdateTask(c, dbSingleton)
	})

	router.DELETE("/api/tasks/:id", func(c *gin.Context) {
		tasks.DeleteTask(c, dbSingleton)
	})
}

func addCategoriesRoutes(router *gin.Engine, dbSingleton *structs.Database) {
	router.GET("/api/categories", func(c *gin.Context) {
		categories.GetCategories(c, dbSingleton)
	})

	router.GET("/api/categories/type/:type", func(c *gin.Context) {
		categories.GetCategoriesByType(c, dbSingleton)
	})

	router.POST("/api/categories", func(c *gin.Context) {
		categories.AddCategory(c, dbSingleton)
	})

	router.PUT("/api/categories/:id", func(c *gin.Context) {
		categories.UpdateCategory(c, dbSingleton)
	})

	router.DELETE("/api/categories/:id", func(c *gin.Context) {
		categories.RemoveCategory(c, dbSingleton)
	})
}

func addUsersRoutes(router *gin.Engine) {
	router.PUT("/api/users/:id", func(c *gin.Context) {
		users.UpdateUser(c)
	})
}
