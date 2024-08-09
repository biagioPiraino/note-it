export class EditorValidator {
  titleRules: any[];
  summaryRules: any[];
  categoryRules: any[];
  noteContentRules: any[];

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
        return "Insert a memorable title.";
      }
    ];

    this.summaryRules = [
      (value: boolean | string) => {
        if (value) {
          const stringValue: string = String(value);

          if (stringValue.length > 150) {
            return "Summary cannot contain more than 150 characters.";
          }

          return true;
        }
        return "Insert a noteworthy summary.";
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

    this.noteContentRules = [
      (value: boolean | string) => {
        if (value) {
          return true;
        }
        return "Don't forget to note something down before leaving.";
      }
    ]
  }
}
