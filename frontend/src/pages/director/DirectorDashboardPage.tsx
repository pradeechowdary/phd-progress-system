import { useQuery } from "@tanstack/react-query";
import { getMySubmissions } from "../../api/submissions";
import QueryState from "../../components/QueryState";
import SubmissionCard from "../../components/SubmissionCard";
import DashboardHeader from "../shared/DashboardHeader";
import MetricCard from "../shared/MetricCard";

export default function DirectorDashboardPage() {
  const query = useQuery({
    queryKey: ["director-dashboard"],
    queryFn: () => getMySubmissions({ page: 0, size: 6 }),
  });

  const submissions = query.data?.content ?? [];

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="Director Workspace"
        title="Resolve final approvals with full workflow context."
        description="The director queue should be concise, high-signal, and focused on submissions already cleared by advisor review."
      />

      <div className="grid gap-4 md:grid-cols-3">
        <MetricCard label="Final review queue" value={query.data?.totalElements ?? 0} />
        <MetricCard
          label="Awaiting final decision"
          value={submissions.filter((item) => item.status === "ADVISOR_APPROVED").length}
          tone="accent"
        />
        <MetricCard label="Completed" value={submissions.filter((item) => item.status === "COMPLETED").length} tone="success" />
      </div>

      <QueryState isLoading={query.isLoading} error={query.error}>
        <section className="grid gap-4 xl:grid-cols-3">
          {submissions.map((submission) => (
            <SubmissionCard
              key={submission.id}
              submission={submission}
              detailPath={`/director/submissions/${submission.id}`}
            />
          ))}
        </section>
      </QueryState>
    </div>
  );
}
