import {EditorComponentType} from "../enums/EditorComponentType";

export class RegexComponentMap {

  regex: RegExp;
  regexPrefix: RegExp;
  componentType: EditorComponentType;

  constructor(regexPrefix:RegExp, regex: RegExp, componentType: EditorComponentType) {
    this.regex = regex;
    this.regexPrefix = regexPrefix;
    this.componentType = componentType;
  }

}
