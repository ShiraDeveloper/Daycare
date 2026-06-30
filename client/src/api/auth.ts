import { apiClient, clearStoredToken, setStoredToken } from './client'
import type { AuthenticateRequest, NannyDto, RegisterRequest } from '../types'

export async function authenticate(
  credentials: AuthenticateRequest,
): Promise<string> {
  const { data } = await apiClient.post<string>(
    '/api/auth/authenticate',
    credentials,
  )
  setStoredToken(data)
  return data
}

export async function register(request: RegisterRequest): Promise<NannyDto> {
  const { data } = await apiClient.post<NannyDto>('/api/auth/register', request)
  return data
}

export function logout(): void {
  clearStoredToken()
}
