import { Navigate, Route, Routes } from "react-router-dom";
import AppShell from "./components/AppShell";
import ProtectedRoute from "./components/ProtectedRoute";
import LoginPage from "./pages/LoginPage";
import RoleLandingPage from "./pages/RoleLandingPage";
import UnauthorizedPage from "./pages/UnauthorizedPage";
import StudentDashboardPage from "./pages/student/StudentDashboardPage";
import StudentSubmissionsPage from "./pages/student/StudentSubmissionsPage";
import CreateSubmissionPage from "./pages/student/CreateSubmissionPage";
import StudentSubmissionDetailPage from "./pages/student/StudentSubmissionDetailPage";
import AdvisorDashboardPage from "./pages/advisor/AdvisorDashboardPage";
import AdvisorSubmissionsPage from "./pages/advisor/AdvisorSubmissionsPage";
import AdvisorSubmissionReviewPage from "./pages/advisor/AdvisorSubmissionReviewPage";
import DirectorDashboardPage from "./pages/director/DirectorDashboardPage";
import DirectorSubmissionsPage from "./pages/director/DirectorSubmissionsPage";
import DirectorSubmissionReviewPage from "./pages/director/DirectorSubmissionReviewPage";
import AdminDashboardPage from "./pages/admin/AdminDashboardPage";
import AdminSubmissionsPage from "./pages/admin/AdminSubmissionsPage";
import AdminSubmissionDetailPage from "./pages/admin/AdminSubmissionDetailPage";
import AdminUsersPage from "./pages/admin/AdminUsersPage";

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/unauthorized" element={<UnauthorizedPage />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<AppShell />}>
          <Route path="/" element={<RoleLandingPage />} />

          <Route element={<ProtectedRoute allowedRoles={["STUDENT"]} />}>
            <Route path="/student/dashboard" element={<StudentDashboardPage />} />
            <Route path="/student/submissions" element={<StudentSubmissionsPage />} />
            <Route path="/student/submissions/new" element={<CreateSubmissionPage />} />
            <Route path="/student/submissions/:id" element={<StudentSubmissionDetailPage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={["ADVISOR"]} />}>
            <Route path="/advisor/dashboard" element={<AdvisorDashboardPage />} />
            <Route path="/advisor/submissions" element={<AdvisorSubmissionsPage />} />
            <Route path="/advisor/submissions/:id" element={<AdvisorSubmissionReviewPage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={["DIRECTOR"]} />}>
            <Route path="/director/dashboard" element={<DirectorDashboardPage />} />
            <Route path="/director/submissions" element={<DirectorSubmissionsPage />} />
            <Route path="/director/submissions/:id" element={<DirectorSubmissionReviewPage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={["ADMIN"]} />}>
            <Route path="/admin/dashboard" element={<AdminDashboardPage />} />
            <Route path="/admin/submissions" element={<AdminSubmissionsPage />} />
            <Route path="/admin/submissions/:id" element={<AdminSubmissionDetailPage />} />
            <Route path="/admin/users" element={<AdminUsersPage />} />
          </Route>
        </Route>
      </Route>
    </Routes>
  );
}
