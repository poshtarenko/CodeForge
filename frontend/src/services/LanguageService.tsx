import {AxiosResponse} from "axios";
import $api from "../http/api";
import {ILanguage} from "../models/entity/ILanguage";

export default class LanguageService {

    static async getAllLanguages(): Promise<AxiosResponse<ILanguage[]>> {
        return $api.get('/languages/all');
    }

}