export function formatDate(value?: string) {
  if (!value) return "N/A";

  return new Intl.DateTimeFormat("en-US", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(value));
}

export function formatBytes(bytes?: number) {
  if (!bytes) return "0 B";

  const units = ["B", "KB", "MB", "GB"];
  let current = bytes;
  let unitIndex = 0;

  while (current >= 1024 && unitIndex < units.length - 1) {
    current /= 1024;
    unitIndex += 1;
  }

  return `${current.toFixed(current >= 10 ? 0 : 1)} ${units[unitIndex]}`;
}

export function classNames(...values: Array<string | false | null | undefined>) {
  return values.filter(Boolean).join(" ");
}
