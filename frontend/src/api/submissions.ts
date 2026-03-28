import { api } from "./axios";
import type { PageResponse } from "../types/api";
import type {
  ApprovalActionRequest,
  ProgressSubmission,
  ProgressSubmissionRequest,
  SubmissionStatus,
} from "../types/submission";

interface SubmissionQueryParams {
  page?: number;
  size?: number;
  status?: SubmissionStatus;
  studentId?: number;
}

export async function getMySubmissions(params: SubmissionQueryParams) {
  const { data } = await api.get<PageResponse<ProgressSubmission>>("/submissions/my", {
    params,
  });
  return data;
}

export async function getSubmissions(params: SubmissionQueryParams) {
  const { data } = await api.get<PageResponse<ProgressSubmission>>("/submissions", {
    params,
  });
  return data;
}

export async function getSubmission(id: number) {
  const { data } = await api.get<ProgressSubmission>(`/submissions/${id}`);
  return data;
}

export async function createSubmission(payload: ProgressSubmissionRequest) {
  const { data } = await api.post<ProgressSubmission>("/submissions", payload);
  return data;
}

export async function updateSubmission(id: number, payload: ProgressSubmissionRequest) {
  const { data } = await api.put<ProgressSubmission>(`/submissions/${id}`, payload);
  return data;
}

export async function submitSubmission(id: number) {
  const { data } = await api.put<ProgressSubmission>(`/submissions/${id}/submit`);
  return data;
}

export async function advisorApprove(id: number, payload: ApprovalActionRequest) {
  const { data } = await api.put<ProgressSubmission>(`/submissions/${id}/advisor-approve`, payload);
  return data;
}

export async function advisorReject(id: number, payload: ApprovalActionRequest) {
  const { data } = await api.put<ProgressSubmission>(`/submissions/${id}/advisor-reject`, payload);
  return data;
}

export async function directorApprove(id: number, payload: ApprovalActionRequest) {
  const { data } = await api.put<ProgressSubmission>(`/submissions/${id}/director-approve`, payload);
  return data;
}

export async function directorReject(id: number, payload: ApprovalActionRequest) {
  const { data } = await api.put<ProgressSubmission>(`/submissions/${id}/director-reject`, payload);
  return data;
}

export async function uploadSubmissionFile(id: number, file: File) {
  const formData = new FormData();
  formData.append("file", file);
  const { data } = await api.post<ProgressSubmission>(`/submissions/${id}/upload`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return data;
}

export async function downloadSubmissionFile(id: number) {
  const response = await api.get<Blob>(`/submissions/${id}/download`, {
    responseType: "blob",
  });
  return response;
}
