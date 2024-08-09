package categories

import (
	"github.com/gin-gonic/gin"
	"net/http"
	helper "note-it/helpers/handlers"
	categoriesServices "note-it/services/categories"
	"note-it/structs"
	"note-it/structs/api"
	"note-it/structs/constants"
	"strconv"
)

func GetCategories(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	allCategories := categoriesServices.GetCategories(dbSingleton, userId)
	c.IndentedJSON(http.StatusOK, allCategories)
}

func GetCategoriesByType(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	categoryType, err := strconv.Atoi(c.Param("type"))

	if err != nil {
		c.IndentedJSON(http.StatusBadRequest, gin.H{"message": "Category StringType Not Found"})
		return
	}

	filteredCategories := categoriesServices.GetCategoriesByType(dbSingleton, userId, constants.CategoryType(categoryType))
	c.IndentedJSON(http.StatusOK, filteredCategories)
}

func AddCategory(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	var newCategory api.Category

	if err := c.BindJSON(&newCategory); err != nil {
		c.IndentedJSON(http.StatusBadRequest, gin.H{"message": "category has bad format"})
		return
	}

	updatedCategories := categoriesServices.AddCategory(dbSingleton, userId, newCategory)
	c.IndentedJSON(http.StatusCreated, updatedCategories)
}

func UpdateCategory(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	var updateCategory api.Category

	if err := c.BindJSON(&updateCategory); err != nil {
		c.IndentedJSON(http.StatusBadRequest, gin.H{"message": "category has bad format"})
		return
	}

	updatedCategories := categoriesServices.UpdateCategory(dbSingleton, userId, updateCategory)
	c.IndentedJSON(http.StatusOK, updatedCategories)
}

func RemoveCategory(c *gin.Context, dbSingleton *structs.Database) {
	userId := helper.GetClaimSubject(c)
	id := c.Param("id")

	updatedCategories := categoriesServices.RemoveCategory(dbSingleton, userId, id)
	c.IndentedJSON(http.StatusOK, updatedCategories)
}
