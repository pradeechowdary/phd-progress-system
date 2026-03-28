import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <header className="panel flex items-center justify-between px-5 py-4">
      <div>
        <p className="text-xs uppercase tracking-[0.25em] text-slate">Active Role</p>
        <h2 className="text-lg font-bold text-ink">{user?.primaryRole ?? "Guest"}</h2>
      </div>

      <div className="flex items-center gap-4">
        <div className="text-right">
          <p className="text-sm font-semibold text-ink">{user?.username}</p>
          <p className="text-xs uppercase tracking-[0.2em] text-slate">{user?.roles.join(", ")}</p>
        </div>
        <button
          className="btn-secondary"
          onClick={() => {
            logout();
            navigate("/login");
          }}
        >
          Logout
        </button>
      </div>
    </header>
  );
}
