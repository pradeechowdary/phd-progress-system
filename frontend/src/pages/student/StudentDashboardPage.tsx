import { useQuery } from "@tanstack/react-query";
import { getMySubmissions } from "../../api/submissions";
import QueryState from "../../components/QueryState";
import SubmissionCard from "../../components/SubmissionCard";
import DashboardHeader from "../shared/DashboardHeader";
import MetricCard from "../shared/MetricCard";

export default function StudentDashboardPage() {
  const submissionsQuery = useQuery({
    queryKey: ["student-dashboard"],
    queryFn: () => getMySubmissions({ page: 0, size: 6 }),
  });

  const submissions = submissionsQuery.data?.content ?? [];

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="Student Workspace"
        title="Manage progress drafts, uploads, and final submission handoff."
        description="Your workspace centers on draft quality, file readiness, and clean submission before advisor review begins."
      />

      <div className="grid gap-4 md:grid-cols-3">
        <MetricCard label="Total submissions" value={submissionsQuery.data?.totalElements ?? 0} />
        <MetricCard
          label="Awaiting review"
          value={submissions.filter((item) => item.status === "SUBMITTED").length}
          tone="accent"
        />
        <MetricCard
          label="Completed"
          value={submissions.filter((item) => item.status === "COMPLETED").length}
          tone="success"
        />
      </div>

      <QueryState isLoading={submissionsQuery.isLoading} error={submissionsQuery.error}>
        <section className="grid gap-4 xl:grid-cols-3">
          {submissions.map((submission) => (
            <SubmissionCard
              key={submission.id}
              submission={submission}
              detailPath={`/student/submissions/${submission.id}`}
            />
          ))}
        </section>
      </QueryState>
    </div>
  );
}
