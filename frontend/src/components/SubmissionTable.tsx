import { Link } from "react-router-dom";
import { formatDate } from "../lib/utils";
import type { ProgressSubmission } from "../types/submission";
import StatusBadge from "./StatusBadge";

interface SubmissionTableProps {
  submissions: ProgressSubmission[];
  detailBasePath: string;
}

export default function SubmissionTable({ submissions, detailBasePath }: SubmissionTableProps) {
  return (
    <div className="overflow-hidden rounded-3xl border border-border bg-white shadow-panel">
      <div className="overflow-x-auto">
        <table className="min-w-full text-left text-sm">
          <thead className="bg-stone-100 text-slate">
            <tr>
              <th className="px-5 py-4 font-semibold">ID</th>
              <th className="px-5 py-4 font-semibold">Title</th>
              <th className="px-5 py-4 font-semibold">Student</th>
              <th className="px-5 py-4 font-semibold">Status</th>
              <th className="px-5 py-4 font-semibold">Updated</th>
              <th className="px-5 py-4 font-semibold">Action</th>
            </tr>
          </thead>
          <tbody>
            {submissions.map((submission) => (
              <tr key={submission.id} className="border-t border-border/70">
                <td className="px-5 py-4 font-medium text-ink">#{submission.id}</td>
                <td className="px-5 py-4 text-ink">{submission.title}</td>
                <td className="px-5 py-4 text-slate">{submission.studentUsername ?? "N/A"}</td>
                <td className="px-5 py-4">
                  <StatusBadge status={submission.status} />
                </td>
                <td className="px-5 py-4 text-slate">{formatDate(submission.updatedAt ?? submission.createdAt)}</td>
                <td className="px-5 py-4">
                  <Link className="font-semibold text-accent" to={`${detailBasePath}/${submission.id}`}>
                    Open
                  </Link>
                </td>
              </tr>
            ))}
            {submissions.length === 0 ? (
              <tr>
                <td colSpan={6} className="px-5 py-10 text-center text-slate">
                  No submissions found.
                </td>
              </tr>
            ) : null}
          </tbody>
        </table>
      </div>
    </div>
  );
}
