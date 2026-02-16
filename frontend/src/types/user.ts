/* 로그인 */
export interface LoginRequest {
  username: string
  password: string
}

export type LoginRequestDto = LoginRequest

/* 회원가입 */
export interface SignupRequest {
  username: string
  password: string
  apiKey: string
}

export type SignupRequestDto = SignupRequest
