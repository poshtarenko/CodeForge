import {AxiosResponse} from "axios";
import $api from "../http/api";
import {IProblem} from "../models/entity/IProblem";
import {CreateTestRequest} from "../models/request/CreateTestRequest";
import {ITest} from "../models/entity/ITest";
import {TryCodeResult} from "../models/entity/TryCodeResult";
import {TryCodeRequest} from "../models/request/TryCodeRequest";
import {SaveAnswerRequest} from "../models/request/SaveAnswerRequest";

export default class AnswerService {

    static async tryCode(request: TryCodeRequest): Promise<AxiosResponse<TryCodeResult>> {
        return $api.post("/answer/try_code", request);
    }

    static async saveAnswer(request: SaveAnswerRequest) {
        return $api.post("/answer", request);
    }

}