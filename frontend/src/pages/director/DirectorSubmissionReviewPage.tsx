import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import toast from "react-hot-toast";
import { directorApprove, directorReject, downloadSubmissionFile, getSubmission } from "../../api/submissions";
import CommentBox from "../../components/CommentBox";
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

export default function DirectorSubmissionReviewPage() {
  const { id } = useParams();
  const submissionId = Number(id);
  const [comments, setComments] = useState("");
  const queryClient = useQueryClient();

  const query = useQuery({
    queryKey: ["director-submission", submissionId],
    queryFn: () => getSubmission(submissionId),
    enabled: Number.isFinite(submissionId),
  });

  const approveMutation = useMutation({
    mutationFn: () => directorApprove(submissionId, { comments }),
    onSuccess() {
      toast.success("Final approval recorded");
      queryClient.invalidateQueries({ queryKey: ["director-submission", submissionId] });
      queryClient.invalidateQueries({ queryKey: ["director-submissions"] });
    },
    onError() {
      toast.error("Final approval failed");
    },
  });

  const rejectMutation = useMutation({
    mutationFn: () => directorReject(submissionId, { comments }),
    onSuccess() {
      toast.success("Final rejection recorded");
      queryClient.invalidateQueries({ queryKey: ["director-submission", submissionId] });
      queryClient.invalidateQueries({ queryKey: ["director-submissions"] });
    },
    onError() {
      toast.error("Final rejection failed");
    },
  });

  return (
    <QueryState isLoading={query.isLoading} error={query.error}>
      {query.data ? (
        <SubmissionDetailLayout
          submission={query.data}
          backPath="/director/submissions"
          onDownload={async () => {
            const response = await downloadSubmissionFile(submissionId);
            saveBlob(response.data, query.data?.fileName ?? `submission-${submissionId}`);
          }}
        >
          <section className="space-y-4 rounded-3xl border border-border bg-stone-50 p-5">
            <h2 className="text-lg font-semibold text-ink">Director Decision</h2>
            <CommentBox value={comments} onChange={setComments} placeholder="Record final decision details" />
            <div className="flex flex-wrap gap-3">
              <button className="btn-primary" disabled={approveMutation.isPending} onClick={() => approveMutation.mutate()}>
                Final approve
              </button>
              <button className="btn-danger" disabled={rejectMutation.isPending} onClick={() => rejectMutation.mutate()}>
                Final reject
              </button>
            </div>
          </section>
        </SubmissionDetailLayout>
      ) : null}
    </QueryState>
  );
}
