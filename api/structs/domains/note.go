package domains

import "time"

type Note struct {
	UserId     string    `bson:"user_id"`
	NoteId     string    `bson:"note_id"`
	CategoryId string    `bson:"category_id"`
	CreatedAt  time.Time `bson:"created_at"`
	ModifiedAt time.Time `bson:"modified_at"`
	Title      string    `bson:"title"`
	Summary    string    `bson:"summary"`
	Content    string    `bson:"content"`
}
