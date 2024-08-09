package api

import "time"

type Task struct {
	Id          string    `json:"id"`
	Title       string    `json:"title"`
	Content     string    `json:"content"`
	Status      int       `json:"status"`
	Category    Category  `json:"category"`
	ScheduledAt time.Time `json:"scheduledAt"`
	CreatedAt   time.Time `json:"createdAt"`
	ModifiedAt  time.Time `json:"modifiedAt"`
}
