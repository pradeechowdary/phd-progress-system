import { Outlet } from "react-router-dom";
import Navbar from "./Navbar";
import Sidebar from "./Sidebar";

export default function AppShell() {
  return (
    <div className="min-h-screen bg-hero-grid">
      <div className="mx-auto grid min-h-screen max-w-[1600px] gap-6 p-4 lg:grid-cols-[280px_minmax(0,1fr)] lg:p-6">
        <div className="hidden lg:block">
          <Sidebar />
        </div>
        <main className="flex min-h-screen flex-col gap-6">
          <Navbar />
          <Outlet />
        </main>
      </div>
    </div>
  );
}
