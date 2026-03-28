export type SubmissionStatus =
  | "DRAFT"
  | "SUBMITTED"
  | "ADVISOR_APPROVED"
  | "ADVISOR_REJECTED"
  | "DIRECTOR_APPROVED"
  | "DIRECTOR_REJECTED"
  | "COMPLETED";

export interface ProgressSubmissionRequest {
  title: string;
  description: string;
  studentId?: number;
  advisorId?: number;
  directorId?: number;
}

export interface ProgressSubmission {
  id: number;
  title: string;
  description: string;
  status: SubmissionStatus;
  advisorStatus?: string;
  directorStatus?: string;
  advisorComments?: string;
  directorComments?: string;
  fileName?: string;
  fileType?: string;
  fileSize?: number;
  uploadedAt?: string;
  createdBy?: number;
  advisorId?: number;
  directorId?: number;
  createdAt?: string;
  updatedAt?: string;
  submittedAt?: string;
  advisorApprovedAt?: string;
  advisorRejectedAt?: string;
  directorApprovedAt?: string;
  directorRejectedAt?: string;
  completedAt?: string;
  studentId?: number;
  studentUsername?: string;
}

export interface ApprovalActionRequest {
  comments: string;
}
