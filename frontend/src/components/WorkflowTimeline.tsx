import { formatDate } from "../lib/utils";
import type { ProgressSubmission } from "../types/submission";

const steps = [
  { key: "createdAt", label: "Draft Created" },
  { key: "submittedAt", label: "Submitted" },
  { key: "advisorApprovedAt", label: "Advisor Approved" },
  { key: "advisorRejectedAt", label: "Advisor Rejected" },
  { key: "directorApprovedAt", label: "Director Approved" },
  { key: "directorRejectedAt", label: "Director Rejected" },
  { key: "completedAt", label: "Completed" },
] as const;

export default function WorkflowTimeline({ submission }: { submission: ProgressSubmission }) {
  return (
    <section className="panel space-y-4 p-5">
      <h3 className="text-lg font-semibold text-ink">Workflow Timeline</h3>
      <div className="space-y-3">
        {steps.map((step) => {
          const value = submission[step.key];
          return (
            <div key={step.key} className="flex items-center justify-between rounded-2xl bg-stone-50 px-4 py-3">
              <span className="font-medium text-ink">{step.label}</span>
              <span className="text-sm text-slate">{typeof value === "string" ? formatDate(value) : "Pending"}</span>
            </div>
          );
        })}
      </div>
    </section>
  );
}
