import {AxiosResponse} from "axios";
import $api from "../http/api";
import {ICategory} from "../models/entity/ICategory";

export default class CategoryService {

    static async getAllCategories(): Promise<AxiosResponse<ICategory[]>> {
        return $api.get('/category/all');
    }

}