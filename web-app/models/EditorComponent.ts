import {EditorComponentType} from "./enums/EditorComponentType";

export class EditorComponent {
  content: string;
  displayOrder: number;
  type: EditorComponentType;
  listItems: Array<string>;

  constructor(displayOrder: number, type: EditorComponentType, content: string, listItems: Array<string>) {
    this.type = type;
    this.content = content;
    this.listItems = listItems;
    this.displayOrder = displayOrder;
  }
}
