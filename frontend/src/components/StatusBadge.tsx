import { classNames } from "../lib/utils";
import type { SubmissionStatus } from "../types/submission";

const badgeStyles: Record<SubmissionStatus | "UNKNOWN", string> = {
  DRAFT: "bg-stone-100 text-stone-700",
  SUBMITTED: "bg-sky-100 text-sky-700",
  ADVISOR_APPROVED: "bg-emerald-100 text-emerald-700",
  ADVISOR_REJECTED: "bg-rose-100 text-rose-700",
  DIRECTOR_APPROVED: "bg-teal-100 text-teal-700",
  DIRECTOR_REJECTED: "bg-red-100 text-red-700",
  COMPLETED: "bg-moss/15 text-moss",
  UNKNOWN: "bg-slate-100 text-slate-700",
};

export default function StatusBadge({ status }: { status?: SubmissionStatus | null }) {
  const safeStatus = status ?? "UNKNOWN";

  return (
    <span
      className={classNames(
        "inline-flex rounded-full px-3 py-1 text-xs font-semibold tracking-wide",
        badgeStyles[safeStatus],
      )}
    >
      {safeStatus.replaceAll("_", " ")}
    </span>
  );
}
