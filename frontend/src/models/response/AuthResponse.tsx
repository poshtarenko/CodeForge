export interface AuthResponse {
    userId: number,
    token: string,
    refreshToken: string,
    roles: string[],
}