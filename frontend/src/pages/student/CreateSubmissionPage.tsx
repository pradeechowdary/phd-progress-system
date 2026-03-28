import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { createSubmission } from "../../api/submissions";
import DashboardHeader from "../shared/DashboardHeader";

export default function CreateSubmissionPage() {
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [advisorId, setAdvisorId] = useState("");
  const [directorId, setDirectorId] = useState("");

  const mutation = useMutation({
    mutationFn: createSubmission,
    onSuccess(data) {
      toast.success("Submission draft created");
      navigate(`/student/submissions/${data.id}`);
    },
    onError() {
      toast.error("Failed to create submission");
    },
  });

  return (
    <div className="space-y-6">
      <DashboardHeader
        eyebrow="New Submission"
        title="Create a workflow draft before file upload and review submission."
        description="This backend currently expects advisor and director assignment by numeric ID. Enter the assignees provided by your program administration."
      />

      <form
        className="panel grid gap-5 p-6"
        onSubmit={(event) => {
          event.preventDefault();
          mutation.mutate({
            title,
            description,
            advisorId: advisorId ? Number(advisorId) : undefined,
            directorId: directorId ? Number(directorId) : undefined,
          });
        }}
      >
        <div>
          <label className="mb-2 block text-sm font-semibold text-ink">Title</label>
          <input className="field" value={title} onChange={(event) => setTitle(event.target.value)} />
        </div>

        <div>
          <label className="mb-2 block text-sm font-semibold text-ink">Description</label>
          <textarea
            className="field min-h-40"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
          />
        </div>

        <div className="grid gap-5 md:grid-cols-2">
          <div>
            <label className="mb-2 block text-sm font-semibold text-ink">Advisor ID</label>
            <input className="field" value={advisorId} onChange={(event) => setAdvisorId(event.target.value)} />
          </div>

          <div>
            <label className="mb-2 block text-sm font-semibold text-ink">Director ID</label>
            <input className="field" value={directorId} onChange={(event) => setDirectorId(event.target.value)} />
          </div>
        </div>

        <button className="btn-primary justify-self-start" disabled={mutation.isPending} type="submit">
          {mutation.isPending ? "Creating..." : "Create draft"}
        </button>
      </form>
    </div>
  );
}
