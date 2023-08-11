import {IEvaluationResult} from "./IEvaluationResult";

export interface ISolution {
    id: number;
    code: string;
    evaluationResult: IEvaluationResult;
    taskId: number;
}