package domains

type UserCategories struct {
	UserId     string     `json:"userId" bson:"user_id"`
	Categories []Category `json:"categories" bson:"categories"`
}
