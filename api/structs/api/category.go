package api

type Category struct {
	Id     string `json:"id"`
	Name   string `json:"name"`
	Colour string `json:"colour"`
	Type   int    `json:"type"`
}
