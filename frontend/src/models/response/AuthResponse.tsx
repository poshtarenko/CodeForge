export interface AuthResponse {
    token: string,
    refreshToken: string,
    roles: string[],
}