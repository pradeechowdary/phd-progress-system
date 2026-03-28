import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import { downloadSubmissionFile, getSubmission } from "../../api/submissions";
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

export default function AdminSubmissionDetailPage() {
  const { id } = useParams();
  const submissionId = Number(id);

  const query = useQuery({
    queryKey: ["admin-submission", submissionId],
    queryFn: () => getSubmission(submissionId),
    enabled: Number.isFinite(submissionId),
  });

  return (
    <QueryState isLoading={query.isLoading} error={query.error}>
      {query.data ? (
        <SubmissionDetailLayout
          submission={query.data}
          backPath="/admin/submissions"
          onDownload={async () => {
            const response = await downloadSubmissionFile(submissionId);
            saveBlob(response.data, query.data?.fileName ?? `submission-${submissionId}`);
          }}
        />
      ) : null}
    </QueryState>
  );
}
