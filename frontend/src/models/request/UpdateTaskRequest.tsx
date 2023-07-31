export interface UpdateTaskRequest {
    id: number,
    note: string,
    maxScore: number,
    problemId: number,
    testId: number
}