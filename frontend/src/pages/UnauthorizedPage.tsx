import { Link } from "react-router-dom";

export default function UnauthorizedPage() {
  return (
    <div className="flex min-h-screen items-center justify-center p-6">
      <div className="panel max-w-xl space-y-4 p-10 text-center">
        <p className="text-xs uppercase tracking-[0.3em] text-accent">Access Restricted</p>
        <h1 className="text-3xl font-extrabold text-ink">You do not have permission to open this area.</h1>
        <p className="text-sm text-slate">Use the workspace that matches your assigned backend role.</p>
        <Link className="btn-primary" to="/">
          Return to dashboard
        </Link>
      </div>
    </div>
  );
}
