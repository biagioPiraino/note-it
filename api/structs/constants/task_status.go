package constants

type TaskStatus int

const (
	Todo TaskStatus = iota
	InProgress
	Completed
	ArchivedTask
)
