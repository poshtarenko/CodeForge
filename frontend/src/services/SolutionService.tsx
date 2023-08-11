import {AxiosResponse} from "axios";
import $api from "../http/api";
import {IEvaluationResult} from "../models/entity/IEvaluationResult";
import {TryCodeRequest} from "../models/request/TryCodeRequest";
import {SaveSolutionRequest} from "../models/request/SaveSolutionRequest";

export default class SolutionService {

    static async tryCode(request: TryCodeRequest): Promise<AxiosResponse<IEvaluationResult>> {
        return $api.post("/solutions/try_code", request);
    }

    static async putSolution(request: SaveSolutionRequest) {
        return $api.post("/solutions", request);
    }

}