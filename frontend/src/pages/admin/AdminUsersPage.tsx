import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { getUsers } from "../../api/users";
import PaginationControls from "../../components/PaginationControls";
import QueryState from "../../components/QueryState";
import DashboardHeader from "../shared/DashboardHeader";

function formatDate(value?: string) {
  if (!value) return "Unavailable";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "Unavailable";
  return date.toLocaleString();
}

export default function AdminUsersPage() {
  const [page, setPage] = useState(0);

  const query = useQuery({
    queryKey: ["admin-users", page],
    queryFn: () => getUsers({ page, size: 10 }),
  });

  const users = query.data?.content ?? [];

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="Admin Users"
        title="User directory with explicit IDs for assignment and auth checks."
        description="Use this view to confirm seeded accounts, inspect roles, and copy the numeric IDs required by the current student submission form."
      />

      <QueryState isLoading={query.isLoading} error={query.error}>
        <section className="panel space-y-4 p-5">
          <div className="rounded-2xl border border-stone-200 bg-stone-50 px-4 py-3 text-sm text-slate">
            Student submission assignment currently expects numeric advisor and director IDs. This page gives you the exact values from the database.
          </div>

          <div className="overflow-hidden rounded-3xl border border-border bg-white shadow-panel">
            <table className="min-w-full divide-y divide-stone-200 text-left text-sm">
              <thead className="bg-stone-50 text-xs uppercase tracking-[0.2em] text-slate">
                <tr>
                  <th className="px-4 py-3">ID</th>
                  <th className="px-4 py-3">Username</th>
                  <th className="px-4 py-3">Email</th>
                  <th className="px-4 py-3">Role</th>
                  <th className="px-4 py-3">Created</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-stone-100 bg-white">
                {users.map((user) => (
                  <tr key={user.id} className="text-slate">
                    <td className="px-4 py-3 font-semibold text-ink">{user.id}</td>
                    <td className="px-4 py-3">{user.username}</td>
                    <td className="px-4 py-3">{user.email}</td>
                    <td className="px-4 py-3">{user.role}</td>
                    <td className="px-4 py-3">{formatDate(user.createdAt)}</td>
                  </tr>
                ))}
                {users.length === 0 ? (
                  <tr>
                    <td className="px-4 py-6 text-center text-slate" colSpan={5}>
                      No users found.
                    </td>
                  </tr>
                ) : null}
              </tbody>
            </table>
          </div>

          <PaginationControls page={page} totalPages={query.data?.totalPages ?? 0} onPageChange={setPage} />
        </section>
      </QueryState>
    </div>
  );
}
