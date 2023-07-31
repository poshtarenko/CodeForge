import {AxiosResponse} from "axios";
import {AuthResponse} from "../models/response/AuthResponse";
import {RegisterRequest} from "../models/request/RegisterRequest";
import $apiUnauthorized from "../http/apiUnauthorized";

export default class AuthService {
    static async login(email: string, password: string): Promise<AxiosResponse<AuthResponse>> {
        return $apiUnauthorized.post<AuthResponse>('/auth/login', {email, password});
    }

    static async refresh(refreshToken: string): Promise<AxiosResponse<AuthResponse>> {
        return $apiUnauthorized.post<AuthResponse>('/auth/refresh', refreshToken, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
    }

    static async register(request: RegisterRequest): Promise<AxiosResponse<AuthResponse>> {
        return $apiUnauthorized.post<AuthResponse>('/auth/register', request);
    }

    static async logout() {
        return $apiUnauthorized.post('/logout');
    }
}