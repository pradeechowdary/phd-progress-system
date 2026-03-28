import type { UserRole } from "./auth";

export interface UserResponse {
  id: number;
  username: string;
  email: string;
  role: UserRole;
  createdAt?: string;
  updatedAt?: string;
}
