import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import hljs from "highlight.js";
import "highlight.js/styles/github-dark.css";
import Pagination from "./Pagination";
import TopicCard from "./TopicCard";
import type { Topic, Reply } from "./types.ts";

// TODO: Remove this hardcoded user when real authentication is implemented
const HARDCODED_USER = {
  username: 'testuser',
  isAuthenticated: true // Set to false to test disabled state
};

export default function Topics() {
  const navigate = useNavigate();
  const [topics, setTopics] = useState<Topic[]>([]);
  const [replies, setReplies] = useState<Record<number, Reply[]>>({});
  const [replyingTo, setReplyingTo] = useState<number | null>(null);
  const [replyContent, setReplyContent] = useState("");
  const [sending, setSending] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [pageSize] = useState(5);
  const [accordionOpen, setAccordionOpen] = useState<Record<number, boolean>>(
    {}
  );
  const replyInputRef = useRef<HTMLTextAreaElement | null>(null);

  useEffect(() => {
    fetch(`http://localhost:8080/topics?page=${page}&size=${pageSize}`)
      .then((response) => response.json())
      .then((data) => {
        const topicsData = data.content.map((t: Topic) => ({
          ...t,
          createdOn: new Date(t.createdOn),
          modifiedOn: new Date(t.modifiedOn),
        }));
        setTopics(topicsData);
        setTotalPages(data.totalPages);
        topicsData.forEach((topic: Topic) => {
          fetch(`http://localhost:8080/replies/topic/${topic.id}`)
            .then((res) => {
              if (!res.ok)
                throw new Error("Грешка при зареждане на отговорите");
              return res.json();
            })
            .then((replyData) => {
              setReplies((prev) => ({
                ...prev,
                [topic.id]: replyData.content || [],
              }));
            })
            .catch(() => {
              setReplies((prev) => ({ ...prev, [topic.id]: [] }));
            });
        });
      })
      .catch((error) => console.error("Error fetching topics:", error));
  }, [page, pageSize]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      document
        .querySelectorAll(".ql-code-block, .ql-syntax")
        .forEach((block) => {
          if (
            block instanceof HTMLElement &&
            (block.tagName === "DIV" || block.tagName === "PRE") &&
            !block.querySelector("button")
          ) {
            if (
              !block.textContent ||
              block.textContent.replace(/[\s\n\r]+/g, "") === ""
            ) {
              return;
            }
            try {
              hljs.highlightElement(block);
            } catch (e) {
              if (window && window.console)
                console.error("HLJS error for block:", block.outerHTML, e);
            }
          }
        });
    }, 100);
    return () => clearTimeout(timeout);
  }, [replies, accordionOpen, replyingTo, success]);

  const handleSendReply = async (topic: Topic) => {
    // Check if user is authenticated before allowing reply
    if (!HARDCODED_USER.isAuthenticated) {
      setError("Трябва да влезете в профила си, за да изпратите отговор");
      return;
    }

    setSending(true);
    setError("");
    setSuccess(false);
    try {
      const res = await fetch("http://localhost:8080/replies", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          topicTitle: topic.title,
          userId: null,
          replyBody: replyContent,
        }),
      });
      if (!res.ok) throw new Error("Грешка при изпращане на отговор");
      fetch(`http://localhost:8080/replies/topic/${topic.id}`)
        .then((res) => {
          if (!res.ok) throw new Error("Грешка при зареждане на отговорите");
          return res.json();
        })
        .then((replyData) => {
          setReplies((prev) => ({
            ...prev,
            [topic.id]: replyData.content || [],
          }));
        });
      setSuccess(true);
      setReplyContent("");
      setTimeout(() => setReplyingTo(null), 1200);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Грешка");
    } finally {
      setSending(false);
    }
  };

  return (
    <div className="topics-container">
      <button className="add-topic-btn" onClick={() => navigate("/add-topic")}>
        Добави нова тема
      </button>
      <button className="search-btn" onClick={() => navigate("/search")}>
        Търсене
      </button>
      <button className="admin-panel-btn" onClick={() => navigate("/admin")}>
        Административен панел
      </button>
      {topics.length > 0 && (
        <Pagination
          page={page}
          totalPages={totalPages}
          setPage={setPage}
          position="top"
        />
      )}
      <div>
        {topics.length === 0 ? (
          <div className="no-topics">Няма публикувани теми</div>
        ) : (
          topics.map((topic) => (
            <TopicCard
              key={topic.id}
              topic={topic}
              replies={replies[topic.id] || []}
              replyingTo={replyingTo}
              setReplyingTo={setReplyingTo}
              replyContent={replyContent}
              setReplyContent={setReplyContent}
              sending={sending}
              success={success}
              error={error}
              handleSendReply={handleSendReply}
              setSuccess={setSuccess}
              setError={setError}
              accordionOpen={accordionOpen}
              setAccordionOpen={setAccordionOpen}
              replyInputRef={replyInputRef}
            />
          ))
        )}
      </div>
      {topics.length > 0 && (
        <Pagination
          page={page}
          totalPages={totalPages}
          setPage={setPage}
          position="bottom"
        />
      )}
    </div>
  );
}
