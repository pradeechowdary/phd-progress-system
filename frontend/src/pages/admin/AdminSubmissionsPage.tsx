import { useMemo, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { getSubmissions } from "../../api/submissions";
import QueryState from "../../components/QueryState";
import PaginationControls from "../../components/PaginationControls";
import SubmissionTable from "../../components/SubmissionTable";
import type { SubmissionStatus } from "../../types/submission";
import DashboardHeader from "../shared/DashboardHeader";
import SubmissionFilters from "../shared/SubmissionFilters";

export default function AdminSubmissionsPage() {
  const [page, setPage] = useState(0);
  const [status, setStatus] = useState<SubmissionStatus | "">("");
  const [studentId, setStudentId] = useState("");
  const [advisorQuery, setAdvisorQuery] = useState("");

  const query = useQuery({
    queryKey: ["admin-submissions", page, status, studentId],
    queryFn: () =>
      getSubmissions({
        page,
        size: 10,
        status: status || undefined,
        studentId: studentId ? Number(studentId) : undefined,
      }),
  });

  const filteredRows = useMemo(() => {
    const rows = query.data?.content ?? [];
    if (!advisorQuery) return rows;
    return rows.filter((item) => String(item.advisorId ?? "").includes(advisorQuery));
  }, [advisorQuery, query.data?.content]);

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="Admin Submissions"
        title="Full submission registry with paging and operational filters."
        description="Status and student filters are server-backed. Advisor ID filtering is applied to the currently loaded page because the current backend API does not expose an advisor filter parameter."
      />

      <SubmissionFilters
        status={status}
        onStatusChange={setStatus}
        studentId={studentId}
        onStudentIdChange={setStudentId}
        advisorQuery={advisorQuery}
        onAdvisorQueryChange={setAdvisorQuery}
      />

      <QueryState isLoading={query.isLoading} error={query.error}>
        <SubmissionTable submissions={filteredRows} detailBasePath="/admin/submissions" />
        <PaginationControls page={page} totalPages={query.data?.totalPages ?? 0} onPageChange={setPage} />
      </QueryState>
    </div>
  );
}
