import { useQuery } from "@tanstack/react-query";
import { getMySubmissions } from "../../api/submissions";
import QueryState from "../../components/QueryState";
import SubmissionCard from "../../components/SubmissionCard";
import DashboardHeader from "../shared/DashboardHeader";
import MetricCard from "../shared/MetricCard";

export default function AdvisorDashboardPage() {
  const query = useQuery({
    queryKey: ["advisor-dashboard"],
    queryFn: () => getMySubmissions({ page: 0, size: 6 }),
  });

  const submissions = query.data?.content ?? [];

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="Advisor Workspace"
        title="Review assigned student submissions and progress them decisively."
        description="Use this queue to move records forward with clear decisions and short, actionable review comments."
      />

      <div className="grid gap-4 md:grid-cols-3">
        <MetricCard label="Assigned reviews" value={query.data?.totalElements ?? 0} />
        <MetricCard label="Ready now" value={submissions.filter((item) => item.status === "SUBMITTED").length} tone="accent" />
        <MetricCard label="Approved" value={submissions.filter((item) => item.status === "ADVISOR_APPROVED").length} tone="success" />
      </div>

      <QueryState isLoading={query.isLoading} error={query.error}>
        <section className="grid gap-4 xl:grid-cols-3">
          {submissions.map((submission) => (
            <SubmissionCard key={submission.id} submission={submission} detailPath={`/advisor/submissions/${submission.id}`} />
          ))}
        </section>
      </QueryState>
    </div>
  );
}
