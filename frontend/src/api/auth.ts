import { api } from "./axios";
import type { AuthRequest, AuthResponse } from "../types/auth";

export async function login(payload: AuthRequest) {
  const { data } = await api.post<AuthResponse>("/auth/login", payload);
  return data;
}
