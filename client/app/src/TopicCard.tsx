import DOMPurify from "dompurify";
import MarkdownEditor from "./MardownEditor";
import type { TopicCardProps } from "./types.ts";

export default function TopicCard({
  topic,
  replies,
  replyingTo,
  setReplyingTo,
  replyContent,
  setReplyContent,
  sending,
  success,
  error,
  handleSendReply,
  setSuccess,
  setError,
  accordionOpen,
  setAccordionOpen,
  replyInputRef,
}: TopicCardProps) {
  function dateToString(date: Date) {
    const pad = (n: number) => (n < 10 ? "0" + n : n);
    return (
      pad(date.getDate()) +
      "." +
      pad(date.getMonth() + 1) +
      "." +
      date.getFullYear() +
      " | " +
      pad(date.getHours()) +
      ":" +
      pad(date.getMinutes())
    );
  }
  return (
    <div className="topic-card">
      <h2 className="topic-title">{topic.title}</h2>
      {topic.content && <div className="topic-content">{topic.content}</div>}
      <div className="topic-date">
        Създадена на: {dateToString(topic.createdOn)}
      </div>
      <div className="topic-actions">
        <button
          className="reply-btn"
          onMouseOver={(e) => {
            (e.currentTarget as HTMLButtonElement).style.transform =
              "scale(1.07)";
            (e.currentTarget as HTMLButtonElement).style.boxShadow =
              "0 6px 24px #43e97b66, 0 2px 8px #0002";
          }}
          onMouseOut={(e) => {
            (e.currentTarget as HTMLButtonElement).style.transform = "none";
            (e.currentTarget as HTMLButtonElement).style.boxShadow =
              "0 4px 16px #43e97b44, 0 1.5px 4px #0001";
          }}
          onClick={() => {
            setReplyingTo(topic.id);
            setReplyContent("");
            setSuccess(false);
            setError("");
            setTimeout(() => replyInputRef?.current?.focus(), 100);
          }}
        >
          Отговор
        </button>
      </div>
      {replyingTo === topic.id && (
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSendReply(topic);
          }}
          className="reply-form"
        >
          <MarkdownEditor
            value={replyContent}
            onEditorChange={setReplyContent}
          />
          <div className="reply-form-actions">
            <button
              type="submit"
              className="reply-form-submit"
              disabled={sending || !replyContent.trim()}
            >
              {sending ? "Изпращане..." : "Изпрати"}
            </button>
            <button
              type="button"
              className="reply-form-cancel"
              onClick={() => setReplyingTo(null)}
            >
              Отказ
            </button>
          </div>
          {success && (
            <div className="reply-success">Успешно изпратен отговор!</div>
          )}
          {error && <div className="reply-error">{error}</div>}
        </form>
      )}
      <div className="accordion">
        <div
          className="accordion-header"
          onClick={() =>
            setAccordionOpen((prev) => ({
              ...prev,
              [topic.id]: !prev[topic.id],
            }))
          }
        >
          <span
            className={`accordion-arrow${
              accordionOpen[topic.id] ? " open" : ""
            }`}
          >
            ▶
          </span>
          Отговори ({replies.length})
        </div>
        {accordionOpen[topic.id] &&
          (replies.length > 0 ? (
            <div className="replies-list">
              {replies.map((r, idx) => (
                <div key={r.id ?? idx} className="reply-card">
                  <div
                    className="reply-body"
                    dangerouslySetInnerHTML={{
                      __html: DOMPurify.sanitize(r.replyBody),
                    }}
                  />
                  <div className="reply-date">
                    Създаден на: {new Date(r.createdOn).toLocaleString("bg-BG")}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="no-replies">Няма отговори</div>
          ))}
      </div>
    </div>
  );
}
