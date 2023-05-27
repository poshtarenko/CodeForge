import {AxiosResponse} from "axios";
import $api from "../http/api";
import {IProblem} from "../models/entity/IProblem";

export default class ProblemService {

    static async getAllProblems(): Promise<AxiosResponse<IProblem[]>> {
        return $api.get('/problem/all');
    }

}