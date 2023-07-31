import {AxiosResponse} from "axios";
import {ITest} from "../models/entity/ITest";
import {CreateTestRequest} from "../models/request/CreateTestRequest";
import $api from "../http/api";
import {UpdateTestRequest} from "../models/request/UpdateTestRequest";

export default class TestService {

    static async getTestAsAuthor(id: number): Promise<AxiosResponse<ITest>> {
        return $api.get('/tests/as_author/' + id);
    }

    static async getTestAsRespondent(id: number): Promise<AxiosResponse<ITest>> {
        return $api.get('/tests/as_respondent/' + id);
    }

    static async getMyTests(): Promise<AxiosResponse<ITest[]>> {
        return $api.get('/tests/my');
    }

    static async createTest(request: CreateTestRequest): Promise<AxiosResponse<ITest>> {
        return $api.post("/tests", request);
    }

    static async updateTest(id: number, request: UpdateTestRequest): Promise<AxiosResponse<ITest>> {
        return $api.put(`/tests/${id}`, request);
    }

    static extractTestLanguages(test: ITest): string {
        const languages = test!.tasks?.map(task => task!.problem!.language.name)
            .filter((value, index, array) => array.indexOf(value) === index);
        return languages?.join(', ');
    }

    static countTestTasks(test: ITest): number {
        return test!.tasks?.length;
    }

    static calcMaxScore(test: ITest): number {
        const scores = test!.tasks?.map(task => task!.maxScore);
        return scores?.reduce((sum, score) => {
            return sum + score
        }, 0);
    }

}