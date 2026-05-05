import {inject} from "vue";
import axios, {type AxiosInstance} from "axios";

export function useHttp() {
    const config: any = inject('config');

    const http: AxiosInstance = axios.create({
        baseURL: config.API_URL,
    });

    const httpGet = (url: string, headers: Record<string, any>) => {
        if (headers) {
            return http.get(url, {headers: headers});
        } else {
            return http.get(url);
        }
    };

    const httpPost = (url: string, data: object, headers: Record<string, any>) => {
        if (headers) {
            if (data) {
                return http.post(url, data, {headers: headers});
            } else {
                return http.post(url, null, {headers: headers});
            }
        } else {
            if (data) {
                return http.post(url, data);
            } else {
                return http.post(url);
            }
        }
    };

    const httpPut = (url: string, data: object, headers: Record<string, any>) => {
        if (headers) {
            if (data) {
                return http.put(url, data, {headers: headers});
            } else {
                return http.put(url, null, {headers: headers});
            }
        } else {
            if (data) {
                return http.put(url, data);
            } else {
                return http.put(url);
            }
        }
    };

    const httpDelete = (url: string, headers: Record<string, any>) => {
        if (headers) {
            return http.delete(url, {headers: headers});
        } else {
            return http.delete(url);
        }
    };

    return {httpGet, httpPost, httpPut, httpDelete};
}