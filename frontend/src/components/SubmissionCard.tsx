import { Link } from "react-router-dom";
import { formatDate } from "../lib/utils";
import type { ProgressSubmission } from "../types/submission";
import StatusBadge from "./StatusBadge";

export default function SubmissionCard({ submission, detailPath }: { submission: ProgressSubmission; detailPath: string }) {
  return (
    <article className="panel space-y-4 p-5">
      <div className="flex items-start justify-between gap-4">
        <div>
          <p className="text-xs uppercase tracking-[0.2em] text-slate">Submission #{submission.id}</p>
          <h3 className="mt-1 text-lg font-semibold text-ink">{submission.title}</h3>
        </div>
        <StatusBadge status={submission.status} />
      </div>
      <p className="max-h-16 overflow-hidden text-sm text-slate">{submission.description}</p>
      <div className="flex items-center justify-between text-sm text-slate">
        <span>{submission.studentUsername ?? "Unknown student"}</span>
        <span>{formatDate(submission.updatedAt ?? submission.createdAt)}</span>
      </div>
      <Link className="btn-secondary w-full" to={detailPath}>
        View details
      </Link>
    </article>
  );
}
