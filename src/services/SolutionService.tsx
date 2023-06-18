import {AxiosResponse} from "axios";
import $api from "../http/api";
import {Problem} from "../models/entity/Problem";
import {CreateTestRequest} from "../models/request/CreateTestRequest";
import {ITest} from "../models/entity/ITest";
import {TryCodeResult} from "../models/entity/TryCodeResult";
import {TryCodeRequest} from "../models/request/TryCodeRequest";
import {SaveSolutionRequest} from "../models/request/SaveSolutionRequest";
import {IAnswer} from "../models/entity/IAnswer";

export default class SolutionService {

    static async tryCode(request: TryCodeRequest): Promise<AxiosResponse<TryCodeResult>> {
        return $api.post("/solution/try_code", request);
    }

    static async putSolution(request: SaveSolutionRequest) {
        return $api.post("/solution", request);
    }

}