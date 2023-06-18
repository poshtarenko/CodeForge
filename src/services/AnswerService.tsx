import {AxiosResponse} from "axios";
import $api from "../http/api";
import {IAnswer} from "../models/entity/IAnswer";

export default class AnswerService {

    static async findRespondentCurrentAnswer(testId: number): Promise<AxiosResponse<IAnswer>> {
        return $api.get("/answer/current/" + testId);
    }

    static async startTest(testCode: string): Promise<AxiosResponse<IAnswer>> {
        return $api.post("/answer/start_test/" + testCode);
    }

    static async finishAnswer(answerId: number): Promise<AxiosResponse<IAnswer>> {
        return $api.post("/answer/finish/" + answerId);
    }

    static async findRespondentAnswersOnTest(testId: number): Promise<AxiosResponse<IAnswer[]>> {
        return $api.get("/answer/by_test_and_respondent/" + testId);
    }

    static async findTestAnswers(testId: number): Promise<AxiosResponse<IAnswer[]>> {
        return $api.get("/answer/by_test/" + testId);
    }

}