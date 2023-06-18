import {ILanguage} from "./ILanguage";
import {ICategory} from "./ICategory";

export interface Problem {
    id: number;
    name: string;
    description: string;
    language: ILanguage;
    category: ICategory;
    templateCode: string;
}
