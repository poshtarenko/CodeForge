import {makeAutoObservable} from "mobx";
import AuthService from "../services/AuthService";
import {RegisterRequest} from "../models/request/RegisterRequest";
import {AuthResponse} from "../models/response/AuthResponse";

export default class Store {

    userId = 0;
    isAuth = false;
    isLoading = false;
    role = "";

    constructor() {
        makeAutoObservable(this);
    }

    setUserId(id: number) {
        this.userId = id;
    }

    setAuth(bool: boolean) {
        this.isAuth = bool;
    }

    setIsLoading(bool: boolean) {
        this.isLoading = bool;
    }

    setRole(role: string){
        this.role = role;
    }

    private updateData(data : AuthResponse) {
        localStorage.setItem('token', data.token);
        localStorage.setItem('refreshToken', data.refreshToken);
        this.setUserId(data.userId);
        this.setRole(data.roles[0])
        this.setAuth(true);
    }

    async login(email: string, password: string) {
        try {
            const response = await AuthService.login(email, password);
            this.updateData(response.data);
        } catch (e) {
            // @ts-ignore
            console.log(e.response?.data?.message)
        }
    }

    async register(request: RegisterRequest) {
        try {
            const response = await AuthService.register(request);
            this.updateData(response.data);
        } catch (e) {
            // @ts-ignore
            console.log(e.response?.data?.message)
        }
    }

    async logout() {
        localStorage.removeItem('token');
        this.setAuth(false);
    }

    async checkAuth() {
        this.isLoading = true;
        try {
            const response = await AuthService.refresh(localStorage.getItem('refreshToken')!);
            this.updateData(response.data);
        } catch (e) {
            // @ts-ignore
            console.log(e.response?.data?.message);
            this.setAuth(false);
            this.setRole("");
        } finally {
            this.isLoading = false;
        }
    }

}