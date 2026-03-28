import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function RoleLandingPage() {
  const { user } = useAuth();

  switch (user?.primaryRole) {
    case "ADVISOR":
      return <Navigate to="/advisor/dashboard" replace />;
    case "DIRECTOR":
      return <Navigate to="/director/dashboard" replace />;
    case "ADMIN":
      return <Navigate to="/admin/dashboard" replace />;
    default:
      return <Navigate to="/student/dashboard" replace />;
  }
}
