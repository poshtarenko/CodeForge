export interface IParticipation {
    id: number,
    lessonId: number,
    code: string,
    respondent: IRespondent,
    evaluationResult: IEvaluationResult
}

export interface IRespondent {
    id: number,
    username: string
}

export interface IEvaluationResult {
    output: string,
    error: string
}