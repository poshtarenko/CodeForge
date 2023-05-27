import axios from "axios";
import {API_URL} from "./api";

const $apiUnauthorized = axios.create({
    baseURL: API_URL
});

export default $apiUnauthorized;