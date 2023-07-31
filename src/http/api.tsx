import axios from "axios";

export const API_URL = 'http://localhost:8080'

const $api = axios.create({
    baseURL: API_URL,
});

$api.interceptors.request.use((config) => {
    config.headers.Authorization = `Bearer ${localStorage.getItem('token')}`;
    console.log("REQUEST: " + config.url)
    return config;
})

export default $api;