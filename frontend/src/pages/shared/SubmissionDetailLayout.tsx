import type { ReactNode } from "react";
import { Link } from "react-router-dom";
import { formatDate } from "../../lib/utils";
import type { ProgressSubmission } from "../../types/submission";
import StatusBadge from "../../components/StatusBadge";
import WorkflowTimeline from "../../components/WorkflowTimeline";
import FileUploadSection from "../../components/FileUploadSection";

interface SubmissionDetailLayoutProps {
  submission: ProgressSubmission;
  backPath: string;
  canUpload?: boolean;
  isUploading?: boolean;
  onUpload?: (file: File) => void;
  onDownload?: () => void;
  children?: ReactNode;
}

export default function SubmissionDetailLayout({
  submission,
  backPath,
  canUpload,
  isUploading,
  onUpload,
  onDownload,
  children,
}: SubmissionDetailLayoutProps) {
  return (
    <div className="grid gap-6 xl:grid-cols-[minmax(0,1fr)_380px]">
      <div className="space-y-6">
        <section className="panel space-y-6 p-6">
          <div className="flex flex-wrap items-center justify-between gap-4">
            <div>
              <Link className="text-sm font-semibold text-accent" to={backPath}>
                Back to list
              </Link>
              <p className="mt-3 text-xs uppercase tracking-[0.25em] text-slate">Submission #{submission.id}</p>
              <h1 className="mt-2 text-3xl font-extrabold text-ink">{submission.title}</h1>
            </div>
            <StatusBadge status={submission.status} />
          </div>

          <div className="grid gap-4 md:grid-cols-3">
            <div className="rounded-2xl bg-stone-50 p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-slate">Student</p>
              <p className="mt-2 font-semibold text-ink">{submission.studentUsername ?? "N/A"}</p>
            </div>
            <div className="rounded-2xl bg-stone-50 p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-slate">Created</p>
              <p className="mt-2 font-semibold text-ink">{formatDate(submission.createdAt)}</p>
            </div>
            <div className="rounded-2xl bg-stone-50 p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-slate">Last Updated</p>
              <p className="mt-2 font-semibold text-ink">{formatDate(submission.updatedAt)}</p>
            </div>
          </div>

          <div>
            <h2 className="text-lg font-semibold text-ink">Description</h2>
            <p className="mt-3 whitespace-pre-wrap text-sm leading-7 text-slate">{submission.description}</p>
          </div>

          {children}
        </section>

        <FileUploadSection
          submission={submission}
          canUpload={Boolean(canUpload && onUpload)}
          isUploading={isUploading}
          onUpload={(file) => onUpload?.(file)}
          onDownload={onDownload}
        />
      </div>

      <WorkflowTimeline submission={submission} />
    </div>
  );
}
