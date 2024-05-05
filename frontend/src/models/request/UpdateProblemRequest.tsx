export interface CreateProblemRequest {
    name: string,
    description: string,
    languageId: number,
    templateCode: string,
    testingCode: string,
}