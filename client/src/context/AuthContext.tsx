import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
  type ReactNode,
} from 'react'
import { authenticate as apiAuthenticate } from '../api/auth'
import { clearStoredToken, getStoredToken } from '../api/client'
import { extractErrorMessage } from '../api/client'

interface AuthContextValue {
  token: string | null
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  authError: string | null
  clearAuthError: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => getStoredToken())
  const [authError, setAuthError] = useState<string | null>(null)

  const login = useCallback(async (email: string, password: string) => {
    setAuthError(null)
    try {
      const jwt = await apiAuthenticate({ email, pass: password })
      setToken(jwt)
    } catch (err) {
      const message = extractErrorMessage(err)
      setAuthError(message)
      throw err
    }
  }, [])

  const logout = useCallback(() => {
    clearStoredToken()
    setToken(null)
  }, [])

  const clearAuthError = useCallback(() => setAuthError(null), [])

  const value = useMemo(
    () => ({
      token,
      isAuthenticated: Boolean(token),
      login,
      logout,
      authError,
      clearAuthError,
    }),
    [token, login, logout, authError, clearAuthError],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
