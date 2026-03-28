import { api } from "./axios";
import type { PageResponse } from "../types/api";
import type { UserResponse } from "../types/user";

interface UserQueryParams {
  page?: number;
  size?: number;
}

export async function getUsers(params: UserQueryParams = {}) {
  const { data } = await api.get<PageResponse<UserResponse>>("/users", { params });
  return data;
}
