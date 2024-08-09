package api

type User struct {
	Name       string `json:"name"`
	Email      string `json:"email"`
	Nickname   string `json:"nickname"`
	Connection string `json:"connection"`
}
