import { useState } from "react";
import { formatBytes, formatDate } from "../lib/utils";
import type { ProgressSubmission } from "../types/submission";

interface FileUploadSectionProps {
  submission: ProgressSubmission;
  canUpload: boolean;
  isUploading?: boolean;
  onUpload: (file: File) => void;
  onDownload?: () => void;
}

export default function FileUploadSection({
  submission,
  canUpload,
  isUploading,
  onUpload,
  onDownload,
}: FileUploadSectionProps) {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  return (
    <section className="panel space-y-4 p-5">
      <div className="flex items-start justify-between">
        <div>
          <h3 className="text-lg font-semibold text-ink">Submission File</h3>
          <p className="text-sm text-slate">Allowed file types: PDF, DOCX. Max size 10MB.</p>
        </div>
        {submission.fileName && onDownload ? (
          <button className="btn-secondary" onClick={onDownload}>
            Download file
          </button>
        ) : null}
      </div>

      {submission.fileName ? (
        <div className="rounded-2xl border border-border bg-stone-50 p-4 text-sm text-slate">
          <p className="font-semibold text-ink">{submission.fileName}</p>
          <p>Type: {submission.fileType ?? "Unknown"}</p>
          <p>Size: {formatBytes(submission.fileSize)}</p>
          <p>Uploaded: {formatDate(submission.uploadedAt)}</p>
        </div>
      ) : (
        <div className="rounded-2xl border border-dashed border-border bg-stone-50 p-6 text-sm text-slate">
          No file uploaded yet.
        </div>
      )}

      {canUpload ? (
        <div className="flex flex-col gap-3 md:flex-row md:items-center">
          <input
            type="file"
            accept=".pdf,.docx,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            onChange={(event) => setSelectedFile(event.target.files?.[0] ?? null)}
          />
          <button
            className="btn-primary"
            disabled={!selectedFile || isUploading}
            onClick={() => selectedFile && onUpload(selectedFile)}
          >
            {isUploading ? "Uploading..." : "Upload file"}
          </button>
        </div>
      ) : null}
    </section>
  );
}
