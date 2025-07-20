import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import hljs from "highlight.js";
import "highlight.js/styles/github-dark.css";
import Pagination from "./Pagination";
import TopicCard from "./TopicCard";
import type { Topic, Reply, Category } from "./types.ts";

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
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
  const [accordionOpen, setAccordionOpen] = useState<Record<number, boolean>>(
    {}
  );
  const replyInputRef = useRef<HTMLTextAreaElement | null>(null);

  useEffect(() => {
    fetch(`http://localhost:8080/api/categories`)
        .then((response) => response.json())
        .then((data) => setCategories(data))
        .catch((error) => console.error("Error fetching categories:", error));
  }, []);

  useEffect(() => {
    let url = `http://localhost:8080/topics?page=${page}&size=${pageSize}`;
    if (selectedCategory) {
      url += `&categoryId=${selectedCategory}`;
    }
    fetch(url)
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
  }, [page, pageSize, selectedCategory]);

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
      const response = await fetch("http://localhost:8080/replies", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          replyBody: replyContent,
          topicTitle: topic.title,
          username: HARDCODED_USER.username,
        }),
      });

      if (!response.ok) {
        throw new Error("Грешка при изпращане на отговор");
      }

      const newReply = await response.json();
      setReplies((prev) => ({
        ...prev,
        [topic.id]: [...(prev[topic.id] || []), newReply],
      }));
      setReplyContent("");
      setReplyingTo(null);
      setSuccess(true);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Възникна неочаквана грешка"
      );
    } finally {
      setSending(false);
    }
  };

  return (
    <div className="container">
      <div className="navigation">
        <button onClick={() => navigate("/add-topic")}>Добави тема</button>
        <div className="filters">
          <select onChange={(e) => setSelectedCategory(Number(e.target.value))} value={selectedCategory || ''}>
            <option value="">Всички категории</option>
            {categories.map(category => (
              <option key={category.id} value={category.id}>{category.name}</option>
            ))}
          </select>
        </div>
      </div>
      <Pagination page={page} totalPages={totalPages} setPage={setPage} />
      <div className="topics-list">
        {topics.map((topic) => (
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
        ))}
      </div>
      <Pagination
        page={page}
        totalPages={totalPages}
        setPage={setPage}
        position="bottom"
      />
    </div>
  );
}
