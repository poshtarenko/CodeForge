import {AxiosResponse} from "axios";
import $api from "../http/api";
import {Problem} from "../models/entity/Problem";
import {CreateProblemRequest} from "../models/request/CreateProblemRequest";
import {UpdateProblemRequest} from "../models/request/UpdateProblemRequest";

export default class ProblemService {

    static async getAvailableProblems(): Promise<AxiosResponse<Problem[]>> {
        return $api.get('/problems/available');
    }

    static async getCustomProblems(): Promise<AxiosResponse<Problem[]>> {
        return $api.get('/problems/custom');
    }

    static async getCustomProblem(id: number): Promise<AxiosResponse<Problem>> {
        return $api.get(`/problems/custom/${id}`);
    }

    static async createCustomProblem(request: CreateProblemRequest): Promise<AxiosResponse<Problem>> {
        return $api.post('/problems/custom', request);
    }

    static async updateCustomProblem(id: number, request: UpdateProblemRequest): Promise<AxiosResponse<Problem>> {
        return $api.put(`/problems/custom/${id}`, request);
    }
}