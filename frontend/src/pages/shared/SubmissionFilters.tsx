import type { SubmissionStatus } from "../../types/submission";

const statuses: Array<{ label: string; value: SubmissionStatus | "" }> = [
  { label: "All statuses", value: "" },
  { label: "Draft", value: "DRAFT" },
  { label: "Submitted", value: "SUBMITTED" },
  { label: "Advisor Approved", value: "ADVISOR_APPROVED" },
  { label: "Advisor Rejected", value: "ADVISOR_REJECTED" },
  { label: "Director Approved", value: "DIRECTOR_APPROVED" },
  { label: "Director Rejected", value: "DIRECTOR_REJECTED" },
  { label: "Completed", value: "COMPLETED" },
];

interface SubmissionFiltersProps {
  status: SubmissionStatus | "";
  onStatusChange: (status: SubmissionStatus | "") => void;
  studentId?: string;
  onStudentIdChange?: (studentId: string) => void;
  advisorQuery?: string;
  onAdvisorQueryChange?: (advisorId: string) => void;
}

export default function SubmissionFilters({
  status,
  onStatusChange,
  studentId,
  onStudentIdChange,
  advisorQuery,
  onAdvisorQueryChange,
}: SubmissionFiltersProps) {
  return (
    <div className="panel flex flex-col gap-3 p-4 lg:flex-row">
      <select className="field lg:max-w-xs" value={status} onChange={(event) => onStatusChange(event.target.value as SubmissionStatus | "")}>
        {statuses.map((item) => (
          <option key={item.label} value={item.value}>
            {item.label}
          </option>
        ))}
      </select>

      {onStudentIdChange ? (
        <input
          className="field lg:max-w-xs"
          placeholder="Filter by student ID"
          value={studentId}
          onChange={(event) => onStudentIdChange(event.target.value)}
        />
      ) : null}

      {onAdvisorQueryChange ? (
        <input
          className="field lg:max-w-xs"
          placeholder="Filter current page by advisor ID"
          value={advisorQuery}
          onChange={(event) => onAdvisorQueryChange(event.target.value)}
        />
      ) : null}
    </div>
  );
}
