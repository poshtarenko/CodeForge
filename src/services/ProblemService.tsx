import {AxiosResponse} from "axios";
import $api from "../http/api";
import {Problem} from "../models/entity/Problem";

export default class ProblemService {

    static async getAllProblems(): Promise<AxiosResponse<Problem[]>> {
        return $api.get('/problems/all');
    }

}