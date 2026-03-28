import type { SessionUser, UserRole } from "../types/auth";

interface JwtPayload {
  sub: string;
  roles?: string[];
}

export function decodeToken(token: string): SessionUser | null {
  try {
    const [, payload] = token.split(".");
    const normalized = payload.replace(/-/g, "+").replace(/_/g, "/");
    const parsed = JSON.parse(window.atob(normalized)) as JwtPayload;
    const roles = (parsed.roles ?? [])
      .map((role) => role.replace("ROLE_", "") as UserRole)
      .filter(Boolean);

    if (!parsed.sub || roles.length === 0) {
      return null;
    }

    return {
      username: parsed.sub,
      roles,
      primaryRole: roles[0],
    };
  } catch {
    return null;
  }
}
