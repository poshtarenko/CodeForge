import $api from "../http/api";
import {CreateTaskRequest} from "../models/request/CreateTaskRequest";
import {UpdateTaskRequest} from "../models/request/UpdateTaskRequest";

export default class TaskService {

    static async createTask(request: CreateTaskRequest) {
        return $api.post("/tasks", request);
    }

    static async updateTask(request: UpdateTaskRequest) {
        return $api.put("/tasks/" + request.id, request);
    }

    static async deleteTask(id: number) {
        return $api.delete(`/tasks/${id}`);
    }

}