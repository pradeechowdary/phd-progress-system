import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { getMySubmissions } from "../../api/submissions";
import QueryState from "../../components/QueryState";
import PaginationControls from "../../components/PaginationControls";
import SubmissionTable from "../../components/SubmissionTable";
import type { SubmissionStatus } from "../../types/submission";
import DashboardHeader from "../shared/DashboardHeader";
import SubmissionFilters from "../shared/SubmissionFilters";

export default function AdvisorSubmissionsPage() {
  const [page, setPage] = useState(0);
  const [status, setStatus] = useState<SubmissionStatus | "">("");

  const query = useQuery({
    queryKey: ["advisor-submissions", page, status],
    queryFn: () => getMySubmissions({ page, size: 10, status: status || undefined }),
  });

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="Advisor Queue"
        title="Assigned submissions with workflow-stage filtering."
        description="Open any record to review the uploaded file, inspect the workflow trail, and issue a structured approval or rejection."
      />

      <SubmissionFilters status={status} onStatusChange={setStatus} />

      <QueryState isLoading={query.isLoading} error={query.error}>
        <SubmissionTable submissions={query.data?.content ?? []} detailBasePath="/advisor/submissions" />
        <PaginationControls page={page} totalPages={query.data?.totalPages ?? 0} onPageChange={setPage} />
      </QueryState>
    </div>
  );
}
