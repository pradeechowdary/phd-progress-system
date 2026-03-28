import type { ReactNode } from "react";

interface QueryStateProps {
  isLoading: boolean;
  error?: unknown;
  children: ReactNode;
}

export default function QueryState({ isLoading, error, children }: QueryStateProps) {
  if (isLoading) {
    return <div className="panel p-10 text-center text-slate">Loading...</div>;
  }

  if (error) {
    return <div className="panel p-10 text-center text-red-700">Failed to load data.</div>;
  }

  return <>{children}</>;
}
