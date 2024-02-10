export interface IAnswer {
    id: number,
    score: number,
    isFinished: boolean,
    testId: number,
    respondent: IRespondent,
    solutions: ISolution[]
}

export interface IRespondent {
    id: number,
    username: string
}

export interface ISolution {
    id: number,
    code: string,
    taskCompletionStatus: string,
    task: ITask
}

export interface ITask {
    id: number,
    note: string,
    maxScore: number,
    problem: IProblem
}

export interface IProblem {
    id: number,
    name: string,
    description: string,
}