import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import toast from "react-hot-toast";
import { downloadSubmissionFile, getSubmission, submitSubmission, uploadSubmissionFile } from "../../api/submissions";
import QueryState from "../../components/QueryState";
import SubmissionDetailLayout from "../shared/SubmissionDetailLayout";

function saveBlob(blob: Blob, fileName: string) {
  const url = URL.createObjectURL(blob);
  const anchor = document.createElement("a");
  anchor.href = url;
  anchor.download = fileName;
  anchor.click();
  URL.revokeObjectURL(url);
}

export default function StudentSubmissionDetailPage() {
  const { id } = useParams();
  const submissionId = Number(id);
  const queryClient = useQueryClient();

  const submissionQuery = useQuery({
    queryKey: ["student-submission", submissionId],
    queryFn: () => getSubmission(submissionId),
    enabled: Number.isFinite(submissionId),
  });

  const uploadMutation = useMutation({
    mutationFn: (file: File) => uploadSubmissionFile(submissionId, file),
    onSuccess() {
      toast.success("File uploaded");
      queryClient.invalidateQueries({ queryKey: ["student-submission", submissionId] });
      queryClient.invalidateQueries({ queryKey: ["student-submissions"] });
    },
    onError() {
      toast.error("Upload failed");
    },
  });

  const submitMutation = useMutation({
    mutationFn: () => submitSubmission(submissionId),
    onSuccess() {
      toast.success("Submission sent for advisor review");
      queryClient.invalidateQueries({ queryKey: ["student-submission", submissionId] });
      queryClient.invalidateQueries({ queryKey: ["student-submissions"] });
    },
    onError() {
      toast.error("Submit action failed");
    },
  });

  return (
    <QueryState isLoading={submissionQuery.isLoading} error={submissionQuery.error}>
      {submissionQuery.data ? (
        <SubmissionDetailLayout
          submission={submissionQuery.data}
          backPath="/student/submissions"
          canUpload={submissionQuery.data.status === "DRAFT"}
          isUploading={uploadMutation.isPending}
          onUpload={(file) => uploadMutation.mutate(file)}
          onDownload={async () => {
            const response = await downloadSubmissionFile(submissionId);
            saveBlob(response.data, submissionQuery.data?.fileName ?? `submission-${submissionId}`);
          }}
        >
          <section className="space-y-4 rounded-3xl border border-border bg-stone-50 p-5">
            <h2 className="text-lg font-semibold text-ink">Student Actions</h2>
            <p className="text-sm text-slate">
              Drafts can be edited and uploaded. Once the file is ready, submit the record into the advisor queue.
            </p>
            <button
              className="btn-primary"
              disabled={submissionQuery.data.status !== "DRAFT" || submitMutation.isPending}
              onClick={() => submitMutation.mutate()}
            >
              {submitMutation.isPending ? "Submitting..." : "Submit for review"}
            </button>
          </section>
        </SubmissionDetailLayout>
      ) : null}
    </QueryState>
  );
}
