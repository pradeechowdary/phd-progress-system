import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import { getSubmissions } from "../../api/submissions";
import { getUsers } from "../../api/users";
import DashboardHeader from "../shared/DashboardHeader";
import MetricCard from "../shared/MetricCard";

export default function AdminDashboardPage() {
  const submissionsQuery = useQuery({
    queryKey: ["admin-dashboard"],
    queryFn: () => getSubmissions({ page: 0, size: 20 }),
  });

  const usersQuery = useQuery({
    queryKey: ["admin-dashboard-users"],
    queryFn: () => getUsers({ page: 0, size: 5 }),
  });

  const submissions = submissionsQuery.data?.content ?? [];
  const users = usersQuery.data?.content ?? [];

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="Admin Workspace"
        title="Global operational view across every workflow stage."
        description="Monitor system-wide submission throughput, spot bottlenecks, and inspect individual records from a single control plane."
      />

      <div className="grid gap-4 md:grid-cols-4">
        <MetricCard label="Total records" value={submissionsQuery.data?.totalElements ?? 0} />
        <MetricCard label="Submitted" value={submissions.filter((item) => item.status === "SUBMITTED").length} tone="accent" />
        <MetricCard label="Rejected" value={submissions.filter((item) => item.status?.includes("REJECTED")).length} />
        <MetricCard label="Completed" value={submissions.filter((item) => item.status === "COMPLETED").length} tone="success" />
      </div>

      <section className="panel space-y-4 p-5">
        <div className="flex items-start justify-between gap-4">
          <div>
            <p className="text-xs uppercase tracking-[0.25em] text-accent">User Directory</p>
            <h2 className="mt-2 text-xl font-bold text-ink">Visible user IDs for workflow testing.</h2>
            <p className="mt-2 max-w-2xl text-sm text-slate">
              Use these IDs when assigning advisors and directors from the student submission form.
            </p>
          </div>
          <Link className="btn-secondary" to="/admin/users">
            Open users
          </Link>
        </div>

        <div className="overflow-hidden rounded-3xl border border-border bg-white shadow-panel">
          <table className="min-w-full divide-y divide-stone-200 text-left text-sm">
            <thead className="bg-stone-50 text-xs uppercase tracking-[0.2em] text-slate">
              <tr>
                <th className="px-4 py-3">ID</th>
                <th className="px-4 py-3">Username</th>
                <th className="px-4 py-3">Role</th>
                <th className="px-4 py-3">Email</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-stone-100 bg-white">
              {users.map((user) => (
                <tr key={user.id} className="text-slate">
                  <td className="px-4 py-3 font-semibold text-ink">{user.id}</td>
                  <td className="px-4 py-3">{user.username}</td>
                  <td className="px-4 py-3">{user.role}</td>
                  <td className="px-4 py-3">{user.email}</td>
                </tr>
              ))}
              {!usersQuery.isLoading && users.length === 0 ? (
                <tr>
                  <td className="px-4 py-6 text-center text-slate" colSpan={4}>
                    No users available.
                  </td>
                </tr>
              ) : null}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}
