import {ISolutionResult} from "./ISolutionResult";

export interface ISolution {
    id: number;
    code: string;
    solutionResult: ISolutionResult;
    taskId: number;
}