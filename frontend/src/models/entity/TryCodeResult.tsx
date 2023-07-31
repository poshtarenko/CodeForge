import {ILanguage} from "./ILanguage";
import {ICategory} from "./ICategory";

export interface TryCodeResult {
    isCompleted: boolean,
    evaluationTime: number,
    error: string
}
