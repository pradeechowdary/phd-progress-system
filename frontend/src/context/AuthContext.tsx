import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
  type PropsWithChildren,
} from "react";
import { TOKEN_STORAGE_KEY } from "../api/axios";
import { login as loginRequest } from "../api/auth";
import { decodeToken } from "../lib/jwt";
import type { AuthRequest, SessionUser, UserRole } from "../types/auth";

interface AuthContextValue {
  token: string | null;
  user: SessionUser | null;
  isAuthenticated: boolean;
  login: (payload: AuthRequest) => Promise<SessionUser>;
  logout: () => void;
  hasRole: (roles: UserRole[]) => boolean;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: PropsWithChildren) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem(TOKEN_STORAGE_KEY));
  const [user, setUser] = useState<SessionUser | null>(() => {
    const storedToken = localStorage.getItem(TOKEN_STORAGE_KEY);
    return storedToken ? decodeToken(storedToken) : null;
  });

  useEffect(() => {
    if (!token) {
      setUser(null);
      return;
    }

    const decoded = decodeToken(token);
    if (!decoded) {
      localStorage.removeItem(TOKEN_STORAGE_KEY);
      setToken(null);
      setUser(null);
      return;
    }

    setUser(decoded);
  }, [token]);

  const value = useMemo<AuthContextValue>(
    () => ({
      token,
      user,
      isAuthenticated: Boolean(token && user),
      async login(payload) {
        const response = await loginRequest(payload);
        const decoded = decodeToken(response.accessToken);
        if (!decoded) {
          throw new Error("Invalid token");
        }
        localStorage.setItem(TOKEN_STORAGE_KEY, response.accessToken);
        setToken(response.accessToken);
        setUser(decoded);
        return decoded;
      },
      logout() {
        localStorage.removeItem(TOKEN_STORAGE_KEY);
        setToken(null);
        setUser(null);
      },
      hasRole(roles) {
        return roles.some((role) => user?.roles.includes(role));
      },
    }),
    [token, user],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }

  return context;
}
