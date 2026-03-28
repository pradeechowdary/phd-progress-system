export default function MetricCard({
  label,
  value,
  tone,
}: {
  label: string;
  value: string | number;
  tone?: "default" | "accent" | "success";
}) {
  const toneMap = {
    default: "bg-white",
    accent: "bg-accent/10",
    success: "bg-moss/10",
  } as const;

  return (
    <div className={`panel p-5 ${toneMap[tone ?? "default"]}`}>
      <p className="text-sm text-slate">{label}</p>
      <p className="mt-4 text-3xl font-extrabold text-ink">{value}</p>
    </div>
  );
}
