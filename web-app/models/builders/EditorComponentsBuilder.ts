import {EditorComponentType} from "../enums/EditorComponentType";
import {RegexComponentMap} from "./RegexComponentMap";
import {EditorComponent} from "../EditorComponent";

export class EditorComponentsBuilder {

  //#region Class attributes
  private _regexMapping: Array<RegexComponentMap> = new Array<RegexComponentMap>(
    new RegexComponentMap(/{t:(\d+)}/,/{t:\d}([\s\S]*?){e-t}/g, EditorComponentType.Title),
    new RegexComponentMap(/{s:(\d)}/,/{s:\d}([\s\S]*?){e-s}/g, EditorComponentType.Subtitle),
    new RegexComponentMap(/{h:(\d)}/,/{h:\d}([\s\S]*?){e-h}/g, EditorComponentType.Heading),
    new RegexComponentMap(/{c:(\d)}/,/{c:\d}([\s\S]*?){e-c}/g, EditorComponentType.Caption),
    new RegexComponentMap(/{p:(\d)}/,/{p:\d}([\s\S]*?){e-p}/g, EditorComponentType.Paragraph),
    new RegexComponentMap(/{l:(\d)}/,/{l:\d}([\s\S]*?){e-l}/g, EditorComponentType.List)
  );

  private _listItemRegex: RegExp = /{li}([\s\S]*?){e-li}/g

  constructor() {  }
  //#endregion

  public BuildComponents(content: string): Array<EditorComponent> {
    const components = new Array<EditorComponent>();

    for (const mapping of this._regexMapping) {
      const regex = mapping.regex;

      let match: any;
      let displayOrder: number;

      while (null !== (match = regex.exec(content))) {
        if (mapping.componentType === EditorComponentType.List) {

          let listMatch: any;
          const listContent: string = match[1];
          const listItems: Array<string> = new Array<string>();

          displayOrder = this.ExtractDisplayOrder(match[0], mapping.regexPrefix);

          while (null !== (listMatch = this._listItemRegex.exec(listContent))) {
            listItems.push(listMatch[1]);
          }

          components.push(new EditorComponent(displayOrder, mapping.componentType, "", listItems));
        } else {
          displayOrder = this.ExtractDisplayOrder(match[0], mapping.regexPrefix);
          components.push(new EditorComponent(displayOrder, mapping.componentType, match[1], new Array<string>()));
        }
      }
    }

    return components.sort((x, y) => x.displayOrder - y.displayOrder);
  }

  private ExtractDisplayOrder(content: string, regexPrefix: RegExp): number {
    const match = content.match(regexPrefix);
    if (match) return parseInt(match[1]);
    else return 0;
  }
}
