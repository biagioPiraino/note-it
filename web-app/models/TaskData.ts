import {TaskStatus} from "./enums/TaskStatus";
import {CategoryData} from "./CategoryData";
import {CategoryType} from "./enums/CategoryType";

export class TaskData {
  id: string = "";
  title: string = "";
  content: string = "";
  createdAt: Date = new Date();
  modifiedAt: Date = new Date();
  scheduledAt: Date = new Date();
  status: TaskStatus = TaskStatus.Todo;
  category: CategoryData = new CategoryData(CategoryType.Task);
}
