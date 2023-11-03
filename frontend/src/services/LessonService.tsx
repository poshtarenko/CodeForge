import {AxiosResponse} from "axios";
import $api from "../http/api";
import {ILesson} from "../models/entity/ILesson";
import {CreateLessonRequest} from "../models/request/CreateLessonRequest";
import {UpdateLessonRequest} from "../models/request/UpdateLessonRequest";

export default class LessonService {

    static async getLessonAsAuthor(id: number): Promise<AxiosResponse<ILesson>> {
        return $api.get('/lessons/as_author/' + id);
    }

    static async getLessonAsRespondent(id: number): Promise<AxiosResponse<ILesson>> {
        return $api.get('/lessons/as_respondent/' + id);
    }

    static async getAuthorLessons(): Promise<AxiosResponse<ILesson[]>> {
        return $api.get('/lessons/as_author/my');
    }

    static async createLesson(request: CreateLessonRequest): Promise<AxiosResponse<ILesson>> {
        return $api.post("/lessons", request);
    }

    static async updateLesson(id: number, request: UpdateLessonRequest): Promise<AxiosResponse<ILesson>> {
        return $api.put(`/lessons/${id}`, request);
    }

}