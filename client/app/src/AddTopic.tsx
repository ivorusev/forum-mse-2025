import { useState } from "react";
import { useNavigate } from "react-router-dom";
import * as React from "react";

export default function AddTopic() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      const res = await fetch("http://localhost:8080/topics", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, content }),
      });
      if (!res.ok) throw new Error("Грешка при добавяне на тема");
      navigate("/");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Грешка");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="add-topic-container">
      <h2>Добави нова тема</h2>
      <form onSubmit={handleSubmit} className="add-topic-form">
        <input
          type="text"
          placeholder="Заглавие"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
          className="add-topic-input"
        />
        <textarea
          placeholder="Съдържание"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
          rows={5}
          className="add-topic-textarea"
        />
        <button type="submit" className="add-topic-submit" disabled={loading}>
          {loading ? "Добавяне..." : "Добави"}
        </button>
        {error && <div className="add-topic-error">{error}</div>}
      </form>
    </div>
  );
}
