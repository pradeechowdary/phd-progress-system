import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import axios from "axios";
import { createSubmission } from "../../api/submissions";
import DashboardHeader from "../shared/DashboardHeader";

function parseOptionalPositiveInteger(value: string) {
  const trimmed = value.trim();
  if (!trimmed) {
    return undefined;
  }

  if (!/^\d+$/.test(trimmed)) {
    throw new Error("Advisor ID and Director ID must be numeric database IDs.");
  }

  const parsed = Number(trimmed);
  if (parsed <= 0) {
    throw new Error("Advisor ID and Director ID must be positive numbers.");
  }

  return parsed;
}

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
    onError(error) {
      if (axios.isAxiosError(error)) {
        const message =
          (error.response?.data as { message?: string } | undefined)?.message ??
          "Failed to create submission";
        toast.error(message);
        return;
      }

      toast.error(error instanceof Error ? error.message : "Failed to create submission");
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
          try {
            mutation.mutate({
              title: title.trim(),
              description: description.trim(),
              advisorId: parseOptionalPositiveInteger(advisorId),
              directorId: parseOptionalPositiveInteger(directorId),
            });
          } catch (error) {
            toast.error(error instanceof Error ? error.message : "Invalid form values");
          }
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
            <p className="mt-2 text-xs text-slate">Optional. Must be a numeric user ID, not a username.</p>
          </div>

          <div>
            <label className="mb-2 block text-sm font-semibold text-ink">Director ID</label>
            <input className="field" value={directorId} onChange={(event) => setDirectorId(event.target.value)} />
            <p className="mt-2 text-xs text-slate">Optional. Must be a numeric user ID, not a username.</p>
          </div>
        </div>

        <button className="btn-primary justify-self-start" disabled={mutation.isPending} type="submit">
          {mutation.isPending ? "Creating..." : "Create draft"}
        </button>
      </form>
    </div>
  );
}
