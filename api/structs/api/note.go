package api

import "time"

type Note struct {
	Id         string    `json:"id"`
	CreatedAt  time.Time `json:"createdAt"`
	ModifiedAt time.Time `json:"modifiedAt"`
	Title      string    `json:"title"`
	Summary    string    `json:"summary"`
	Content    string    `json:"content"`
	Category   Category  `json:"category"`
}
