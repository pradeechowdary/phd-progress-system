import { useQuery } from "@tanstack/react-query";
import { getSubmissions } from "../../api/submissions";
import DashboardHeader from "../shared/DashboardHeader";
import MetricCard from "../shared/MetricCard";

export default function AdminDashboardPage() {
  const query = useQuery({
    queryKey: ["admin-dashboard"],
    queryFn: () => getSubmissions({ page: 0, size: 20 }),
  });

  const submissions = query.data?.content ?? [];

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="Admin Workspace"
        title="Global operational view across every workflow stage."
        description="Monitor system-wide submission throughput, spot bottlenecks, and inspect individual records from a single control plane."
      />

      <div className="grid gap-4 md:grid-cols-4">
        <MetricCard label="Total records" value={query.data?.totalElements ?? 0} />
        <MetricCard label="Submitted" value={submissions.filter((item) => item.status === "SUBMITTED").length} tone="accent" />
        <MetricCard label="Rejected" value={submissions.filter((item) => item.status.includes("REJECTED")).length} />
        <MetricCard label="Completed" value={submissions.filter((item) => item.status === "COMPLETED").length} tone="success" />
      </div>
    </div>
  );
}
