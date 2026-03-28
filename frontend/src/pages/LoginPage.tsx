import { useState } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { useAuth } from "../context/AuthContext";

function getHomePath(role: string) {
  switch (role) {
    case "ADVISOR":
      return "/advisor/dashboard";
    case "DIRECTOR":
      return "/director/dashboard";
    case "ADMIN":
      return "/admin/dashboard";
    default:
      return "/student/dashboard";
  }
}

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsSubmitting(true);

    try {
      const session = await login({ username, password });
      const role = session.primaryRole;
      toast.success("Login successful");
      navigate(getHomePath(role));
    } catch {
      toast.error("Login failed. Check your credentials.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-hero-grid p-6">
      <div className="grid w-full max-w-5xl overflow-hidden rounded-[2rem] border border-border bg-panel shadow-panel lg:grid-cols-[1.1fr_0.9fr]">
        <div className="hidden bg-ink px-10 py-12 text-white lg:flex lg:flex-col lg:justify-between">
          <div>
            <p className="text-xs uppercase tracking-[0.35em] text-white/60">PhD Progress System</p>
            <h1 className="mt-5 max-w-md text-5xl font-extrabold leading-tight">
              Enterprise workflow for academic progress reviews.
            </h1>
          </div>
          <div className="grid gap-4">
            <div className="rounded-3xl border border-white/15 bg-white/5 p-5">
              <p className="text-sm text-white/70">Role-aware workspace</p>
              <p className="mt-2 font-semibold">Student, Advisor, Director, and Admin paths stay isolated.</p>
            </div>
            <div className="rounded-3xl border border-white/15 bg-white/5 p-5">
              <p className="text-sm text-white/70">Workflow transparency</p>
              <p className="mt-2 font-semibold">Submission status, files, comments, and review history in one console.</p>
            </div>
          </div>
        </div>

        <div className="px-8 py-12 lg:px-12">
          <p className="text-xs uppercase tracking-[0.25em] text-accent">Secure Access</p>
          <h2 className="mt-3 text-3xl font-extrabold text-ink">Sign in</h2>
          <p className="mt-3 max-w-md text-sm text-slate">
            Use your backend-issued credentials. JWT is stored locally and attached automatically to protected API calls.
          </p>

          <form className="mt-10 space-y-5" onSubmit={handleSubmit}>
            <div>
              <label className="mb-2 block text-sm font-semibold text-ink">Username</label>
              <input className="field" value={username} onChange={(event) => setUsername(event.target.value)} />
            </div>

            <div>
              <label className="mb-2 block text-sm font-semibold text-ink">Password</label>
              <input
                className="field"
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
              />
            </div>

            <button className="btn-primary w-full" disabled={isSubmitting} type="submit">
              {isSubmitting ? "Signing in..." : "Sign in"}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
