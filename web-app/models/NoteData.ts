import {CategoryData} from "./CategoryData";
import {CategoryType} from "./enums/CategoryType";

export class NoteData {
  id: string = "";
  createdAt: Date = new Date();
  modifiedAt: Date = new Date();
  title: string = "";
  summary: string = "";
  content: string = "";
  category: CategoryData = new CategoryData(CategoryType.Note);
}
