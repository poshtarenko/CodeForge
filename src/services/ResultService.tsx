import {AxiosResponse} from "axios";
import $api from "../http/api";
import {IProblem} from "../models/entity/IProblem";
import {CreateTestRequest} from "../models/request/CreateTestRequest";
import {ITest} from "../models/entity/ITest";
import {TryCodeResult} from "../models/entity/TryCodeResult";
import {TryCodeRequest} from "../models/request/TryCodeRequest";
import {SaveAnswerRequest} from "../models/request/SaveAnswerRequest";
import {IAnswer} from "../models/entity/IAnswer";
import {IResult} from "../models/entity/IResult";

export default class ResultService {

    static async tryCode(request: TryCodeRequest): Promise<AxiosResponse<TryCodeResult>> {
        return $api.post("/answer/try_code", request);
    }

    static async findRespondentTestResult(testId: number): Promise<AxiosResponse<IResult>> {
        return $api.get("/result/by_test_and_respondent/" + testId);
    }

    static async saveAnswer(request: SaveAnswerRequest) {
        return $api.post("/answer", request);
    }

}