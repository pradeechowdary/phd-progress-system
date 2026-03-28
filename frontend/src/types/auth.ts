export type UserRole = "STUDENT" | "ADVISOR" | "DIRECTOR" | "ADMIN";

export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
}

export interface SessionUser {
  username: string;
  roles: UserRole[];
  primaryRole: UserRole;
}
