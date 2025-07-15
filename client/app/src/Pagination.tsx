import type { PaginationProps } from "./types.ts";

export default function Pagination({
  page,
  totalPages,
  setPage,
  position,
}: PaginationProps) {
  return (
    <div
      className="pagination"
      style={position === "bottom" ? { margin: "32px 0 0 0" } : {}}
    >
      <button
        className="pagination-btn"
        onClick={() => setPage(Math.max(0, page - 1))}
        disabled={page === 0}
      >
        ← Предишна
      </button>
      <span className="pagination-info">
        Страница {page + 1} от {totalPages}
      </span>
      <button
        className="pagination-btn"
        onClick={() => setPage(Math.min(totalPages - 1, page + 1))}
        disabled={page >= totalPages - 1}
      >
        Следваща →
      </button>
    </div>
  );
}
