import MarkdownEditor from "./MardownEditor";
import VoteButtons from "./components/VoteButtons";
import type { TopicCardProps } from "./types.ts";
import "./TopicCard.css";
const HARDCODED_USER = {
  username: 'testuser',
  isAuthenticated: true
};

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
      <div className="topic-content-wrapper">
        <VoteButtons
          itemId={topic.id}
          itemType="topic"
          voteScore={topic.voteScore || 0}
        />
        <div className="topic-main-content">
          <h2 className="topic-title">{topic.title}</h2>
          {topic.category && <div className="topic-category">{topic.category.name}</div>}
          {topic.content && <div className="topic-content">{topic.content}</div>}
          <div className="topic-date">
            Създадена на: {dateToString(topic.createdOn)}
          </div>
          <div className="topic-actions">
            <button
              className="reply-btn"
              disabled={!HARDCODED_USER.isAuthenticated}
              title={HARDCODED_USER.isAuthenticated ? "Отговор" : "Влизането е необходимо за отговор"}
              onMouseOver={(e) => {
                if (HARDCODED_USER.isAuthenticated) {
                  (e.currentTarget as HTMLButtonElement).style.transform =
                    "scale(1.07)";
                  (e.currentTarget as HTMLButtonElement).style.boxShadow =
                    "0 6px 24px #43e97b66, 0 2px 8px #0002";
                }
              }}
              onMouseOut={(e) => {
                if (HARDCODED_USER.isAuthenticated) {
                  (e.currentTarget as HTMLButtonElement).style.transform = "none";
                  (e.currentTarget as HTMLButtonElement).style.boxShadow =
                    "0 4px 16px #43e97b44, 0 1.5px 4px #0001";
                }
              }}
              onClick={() => {
                if (HARDCODED_USER.isAuthenticated) {
                  setReplyingTo(topic.id);
                  setReplyContent("");
                  setSuccess(false);
                  setError("");
                  setTimeout(() => replyInputRef?.current?.focus(), 100);
                }
              }}
            >
              Отговор
            </button>
            {!HARDCODED_USER.isAuthenticated && (
              <small className="auth-required-note">
                Влизането е необходимо за отговор
              </small>
            )}
          </div>
        </div>
      </div>
      <div className="replies-section">
        {replyingTo === topic.id && (
          <form
            onSubmit={(e) => {
              e.preventDefault();
              handleSendReply(topic);
            }}
          >
            <MarkdownEditor
              value={replyContent}
              onChange={setReplyContent}
              inputRef={replyInputRef as React.RefObject<HTMLElement | null>}
            />
            <button type="submit" disabled={sending} className="send-reply-btn">
              {sending ? "Изпращане..." : "Изпрати"}
            </button>
            {success && <div className="success-msg">Отговорът е изпратен!</div>}
            {error && <div className="error-msg">{error}</div>}
          </form>
        )}
        <div className="replies-list">
          {replies.map((reply) => (
            <div key={reply.id} className="reply">
              <p>{reply.replyBody}</p>
              <small>Отговорено на: {new Date(reply.createdOn).toLocaleString()}</small>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
