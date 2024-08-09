export class TaskEditorValidator {
  titleRules: any[];
  contentRules: any[];
  categoryRules: any[];

  constructor() {
    this.titleRules = [
      (value: boolean | string) => {
        if (value) {
          const stringValue: string = String(value);

          if (stringValue.length > 30) {
            return "Title cannot contain more than 30 characters.";
          }

          return true;
        }
        return "Insert a title.";
      }
    ];

    this.contentRules = [
      (value: boolean | string) => {
        if (value) {
          const stringValue: string = String(value);

          if (stringValue.length > 50) {
            return "Task cannot contain more than 50 characters.";
          }

          return true;
        }
        return "Insert a task.";
      }
    ]

    this.categoryRules = [
      (value: boolean | string) => {
        if (value) {
          return true;
        }
        return "Select a category";
      }
    ]

  }
}
