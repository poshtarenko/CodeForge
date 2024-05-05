export interface IParticipation {
    id: number,
    lessonId: number,
    code: string,
    user: IUser,
    evaluationResult: IEvaluationResult
    evaluating: boolean
}

export interface IUser {
    id: number,
    username: string
}

export interface IEvaluationResult {
    output: string,
    error: string
}