import {makeAutoObservable} from "mobx";
import AuthService from "../services/AuthService";
import {RegisterRequest} from "../models/request/RegisterRequest";

export default class Store {

    isAuth = false;
    isLoading = false;
    role = "";

    constructor() {
        makeAutoObservable(this);
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

    async login(email: string, password: string) {
        try {
            const response = await AuthService.login(email, password);
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('refreshToken', response.data.refreshToken);
            this.setAuth(true);
        } catch (e) {
            // @ts-ignore
            console.log(e.response?.data?.message)
        }
    }

    async register(request: RegisterRequest) {
        try {
            const response = await AuthService.register(request);
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('refreshToken', response.data.refreshToken);
            this.setAuth(true);
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
            localStorage.setItem('token', response.data.token);
            this.setAuth(true);
            this.setRole(response.data.roles[0]);
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