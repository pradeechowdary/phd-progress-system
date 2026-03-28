export default function DashboardHeader({
  eyebrow,
  title,
  description,
}: {
  eyebrow: string;
  title: string;
  description: string;
}) {
  return (
    <section className="panel overflow-hidden p-6">
      <div className="rounded-[2rem] bg-hero-grid p-6">
        <p className="text-xs uppercase tracking-[0.3em] text-accent">{eyebrow}</p>
        <h1 className="mt-3 text-3xl font-extrabold text-ink">{title}</h1>
        <p className="mt-3 max-w-2xl text-sm text-slate">{description}</p>
      </div>
    </section>
  );
}
