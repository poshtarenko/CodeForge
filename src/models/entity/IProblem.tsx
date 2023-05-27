import {ILanguage} from "./ILanguage";
import {ICategory} from "./ICategory";

export interface IProblem {
    id: number;
    name: string;
    description: string;
    language: ILanguage;
    category: ICategory;
}
