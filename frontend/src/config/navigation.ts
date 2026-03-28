import type { UserRole } from "../types/auth";

export interface NavigationItem {
  label: string;
  to: string;
  roles: UserRole[];
}

export const navigationItems: NavigationItem[] = [
  { label: "Student Dashboard", to: "/student/dashboard", roles: ["STUDENT"] },
  { label: "My Submissions", to: "/student/submissions", roles: ["STUDENT"] },
  { label: "Create Submission", to: "/student/submissions/new", roles: ["STUDENT"] },
  { label: "Advisor Dashboard", to: "/advisor/dashboard", roles: ["ADVISOR"] },
  { label: "Assigned Reviews", to: "/advisor/submissions", roles: ["ADVISOR"] },
  { label: "Director Dashboard", to: "/director/dashboard", roles: ["DIRECTOR"] },
  { label: "Final Queue", to: "/director/submissions", roles: ["DIRECTOR"] },
  { label: "Admin Dashboard", to: "/admin/dashboard", roles: ["ADMIN"] },
  { label: "All Submissions", to: "/admin/submissions", roles: ["ADMIN"] },
];
