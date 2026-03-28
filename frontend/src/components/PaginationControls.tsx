interface PaginationControlsProps {
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export default function PaginationControls({
  page,
  totalPages,
  onPageChange,
}: PaginationControlsProps) {
  return (
    <div className="flex items-center justify-between rounded-2xl border border-border bg-white px-4 py-3">
      <p className="text-sm text-slate">
        Page {totalPages === 0 ? 0 : page + 1} of {totalPages}
      </p>
      <div className="flex gap-2">
        <button className="btn-secondary" disabled={page === 0} onClick={() => onPageChange(page - 1)}>
          Previous
        </button>
        <button
          className="btn-secondary"
          disabled={totalPages === 0 || page >= totalPages - 1}
          onClick={() => onPageChange(page + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
}
