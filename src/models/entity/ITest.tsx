import {ILanguage} from "./ILanguage";
import {ICategory} from "./ICategory";

export interface ITest {
    id: number;
    name: string;
    code: string;
    maxDuration: number;
    authorId: number;
    tasks: ITask[];
}

export interface ITask {
    id: number;
    note: string;
    maxScore: number;
    problem: IProblem;
}

export interface IProblem {
    id: number;
    name: string;
    description: string;
    language: ILanguage;
    category: ICategory;
}