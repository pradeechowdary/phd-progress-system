import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { getMySubmissions } from "../../api/submissions";
import QueryState from "../../components/QueryState";
import SubmissionTable from "../../components/SubmissionTable";
import PaginationControls from "../../components/PaginationControls";
import type { SubmissionStatus } from "../../types/submission";
import DashboardHeader from "../shared/DashboardHeader";
import SubmissionFilters from "../shared/SubmissionFilters";

export default function StudentSubmissionsPage() {
  const [page, setPage] = useState(0);
  const [status, setStatus] = useState<SubmissionStatus | "">("");

  const query = useQuery({
    queryKey: ["student-submissions", page, status],
    queryFn: () => getMySubmissions({ page, size: 10, status: status || undefined }),
  });

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="Student Submissions"
        title="Track every draft, review decision, and file attachment."
        description="Use filters to focus on the current workflow stage and open any submission for file upload or final submission."
      />

      <SubmissionFilters status={status} onStatusChange={setStatus} />

      <QueryState isLoading={query.isLoading} error={query.error}>
        <SubmissionTable submissions={query.data?.content ?? []} detailBasePath="/student/submissions" />
        <PaginationControls
          page={page}
          totalPages={query.data?.totalPages ?? 0}
          onPageChange={setPage}
        />
      </QueryState>
    </div>
  );
}
