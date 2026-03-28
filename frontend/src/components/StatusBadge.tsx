import { classNames } from "../lib/utils";
import type { SubmissionStatus } from "../types/submission";

const badgeStyles: Record<SubmissionStatus, string> = {
  DRAFT: "bg-stone-100 text-stone-700",
  SUBMITTED: "bg-sky-100 text-sky-700",
  ADVISOR_APPROVED: "bg-emerald-100 text-emerald-700",
  ADVISOR_REJECTED: "bg-rose-100 text-rose-700",
  DIRECTOR_APPROVED: "bg-teal-100 text-teal-700",
  DIRECTOR_REJECTED: "bg-red-100 text-red-700",
  COMPLETED: "bg-moss/15 text-moss",
};

export default function StatusBadge({ status }: { status: SubmissionStatus }) {
  return (
    <span
      className={classNames(
        "inline-flex rounded-full px-3 py-1 text-xs font-semibold tracking-wide",
        badgeStyles[status],
      )}
    >
      {status.replaceAll("_", " ")}
    </span>
  );
}
