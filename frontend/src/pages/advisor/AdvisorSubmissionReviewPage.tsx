import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import toast from "react-hot-toast";
import { advisorApprove, advisorReject, downloadSubmissionFile, getSubmission } from "../../api/submissions";
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

export default function AdvisorSubmissionReviewPage() {
  const { id } = useParams();
  const submissionId = Number(id);
  const [comments, setComments] = useState("");
  const queryClient = useQueryClient();

  const query = useQuery({
    queryKey: ["advisor-submission", submissionId],
    queryFn: () => getSubmission(submissionId),
    enabled: Number.isFinite(submissionId),
  });

  const approveMutation = useMutation({
    mutationFn: () => advisorApprove(submissionId, { comments }),
    onSuccess() {
      toast.success("Submission approved");
      queryClient.invalidateQueries({ queryKey: ["advisor-submission", submissionId] });
      queryClient.invalidateQueries({ queryKey: ["advisor-submissions"] });
    },
    onError() {
      toast.error("Advisor approval failed");
    },
  });

  const rejectMutation = useMutation({
    mutationFn: () => advisorReject(submissionId, { comments }),
    onSuccess() {
      toast.success("Submission rejected");
      queryClient.invalidateQueries({ queryKey: ["advisor-submission", submissionId] });
      queryClient.invalidateQueries({ queryKey: ["advisor-submissions"] });
    },
    onError() {
      toast.error("Advisor rejection failed");
    },
  });

  return (
    <QueryState isLoading={query.isLoading} error={query.error}>
      {query.data ? (
        <SubmissionDetailLayout
          submission={query.data}
          backPath="/advisor/submissions"
          onDownload={async () => {
            const response = await downloadSubmissionFile(submissionId);
            saveBlob(response.data, query.data?.fileName ?? `submission-${submissionId}`);
          }}
        >
          <section className="space-y-4 rounded-3xl border border-border bg-stone-50 p-5">
            <h2 className="text-lg font-semibold text-ink">Advisor Review</h2>
            <CommentBox value={comments} onChange={setComments} placeholder="Document your review decision" />
            <div className="flex flex-wrap gap-3">
              <button className="btn-primary" disabled={approveMutation.isPending} onClick={() => approveMutation.mutate()}>
                Approve
              </button>
              <button className="btn-danger" disabled={rejectMutation.isPending} onClick={() => rejectMutation.mutate()}>
                Reject
              </button>
            </div>
          </section>
        </SubmissionDetailLayout>
      ) : null}
    </QueryState>
  );
}
