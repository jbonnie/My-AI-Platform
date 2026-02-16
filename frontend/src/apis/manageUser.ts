import axios from 'axios'
import type { LoginRequestDto, SignupRequestDto } from '../types/user'

const API_BASE_URL = import.meta.env.API_BASE_URL || 'http://localhost:8080'

export const login = async (loginRequestDto: LoginRequestDto): Promise<string> => {
  const response = await axios.post(`${API_BASE_URL}/api/auth/login`, requestBody, {
    headers: {
      'Content-Type': 'application/json',
    },
  })

  return response.data
}

export const signup = async (signupRequestDto: SignupRequestDto): Promise<string> => {
  const response = await axios.post(`${API_BASE_URL}/api/auth/signup`, requestBody, {
    headers: {
      'Content-Type': 'application/json',
    },
  })

  return response.data
}

export const logout = async (): Promise<> => {
  const response = await axios.post(`${API_BASE_URL}/api/auth/logout` {
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

export const withdraw = async (): Promise<> => {
  const response = await axios.delete(`${API_BASE_URL}/api/auth/withdraw` {
    headers: {
      'Content-Type': 'application/json',
    },
  })
}