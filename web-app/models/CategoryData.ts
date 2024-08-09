import {CategoryType} from "./enums/CategoryType";

export class CategoryData {
  id: string = "";
  name: string = "";
  colour: string = "#4BB";
  type: CategoryType = CategoryType.Note;

  constructor(type: CategoryType) {
    this.type = type;
  }
}
