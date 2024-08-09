export enum TaskStatus {
  Todo,
  InProgress,
  Completed
}

export class TaskStatusMapper {
  readonly Description = Object.freeze({
    [TaskStatus.Todo]: "To do",
    [TaskStatus.InProgress]: "In progress",
    [TaskStatus.Completed]: "Completed"
  });

  readonly Color = Object.freeze({
    [TaskStatus.Todo]: "primary",
    [TaskStatus.InProgress]: "amber-darken-4",
    [TaskStatus.Completed]: "green-darken-2"
  });

  getDescription(status: TaskStatus): string {
    return this.Description[status];
  }

  getColour(status: TaskStatus): string {
    return this.Color[status];
  }
}
