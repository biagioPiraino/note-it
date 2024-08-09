package domains

import (
	"note-it/structs/constants"
	"time"
)

type Task struct {
	Id          string               `bson:"task_id"`
	UserId      string               `bson:"user_id"`
	CategoryId  string               `bson:"category_id"`
	Title       string               `bson:"title"`
	Content     string               `bson:"content"`
	Status      constants.TaskStatus `bson:"status"`
	ScheduledAt time.Time            `bson:"scheduled_at"`
	CreatedAt   time.Time            `bson:"created_at"`
	ModifiedAt  time.Time            `bson:"modified_at"`
}
