import { NavLink } from "react-router-dom";
import { navigationItems } from "../config/navigation";
import { classNames } from "../lib/utils";
import { useAuth } from "../context/AuthContext";

export default function Sidebar() {
  const { user } = useAuth();

  return (
    <aside className="panel flex h-full flex-col gap-6 p-6">
      <div>
        <p className="text-xs uppercase tracking-[0.3em] text-accent">PhD Progress</p>
        <h1 className="mt-2 text-2xl font-extrabold text-ink">Workflow Console</h1>
      </div>

      <nav className="flex flex-1 flex-col gap-2">
        {navigationItems
          .filter((item) => item.roles.includes(user?.primaryRole ?? "STUDENT"))
          .map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                classNames(
                  "rounded-2xl px-4 py-3 text-sm font-semibold transition",
                  isActive ? "bg-ink text-white" : "text-slate hover:bg-stone-100 hover:text-ink",
                )
              }
            >
              {item.label}
            </NavLink>
          ))}
      </nav>
    </aside>
  );
}
