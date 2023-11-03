import {AxiosResponse} from "axios";
import $api from "../http/api";
import {ISolutionResult} from "../models/entity/ISolutionResult";
import {TryCodeRequest} from "../models/request/TryCodeRequest";
import {SaveSolutionRequest} from "../models/request/SaveSolutionRequest";

export default class SolutionService {

    static async tryCode(request: TryCodeRequest): Promise<AxiosResponse<ISolutionResult>> {
        return $api.post("/solutions/try_code", request);
    }

    static async putSolution(request: SaveSolutionRequest) {
        return $api.post("/solutions", request);
    }

}