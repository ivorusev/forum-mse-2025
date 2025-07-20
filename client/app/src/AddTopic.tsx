import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import * as React from "react";
import type { Category } from "./types";

const HARDCODED_USER = {
  username: 'testuser',
  isAuthenticated: true // Set to false to test disabled state
};

export default function AddTopic() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/api/categories")
        .then(res => res.json())
        .then(data => {
            setCategories(data);
            if (data.length > 0) {
                setSelectedCategory(data[0].id);
            }
        })
        .catch(err => console.error("Failed to fetch categories", err));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    // Check if user is authenticated before allowing topic creation
    if (!HARDCODED_USER.isAuthenticated) {
      setError("Трябва да влезете в профила си, за да добавите тема");
      return;
    }

    setLoading(true);
    setError("");
    try {
      const username = HARDCODED_USER.username;
      
      const res = await fetch("http://localhost:8080/topics", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, username, categoryId: selectedCategory, body: content }),
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
      {!HARDCODED_USER.isAuthenticated && (
        <div className="auth-warning">
          <p>Трябва да влезете в профила си, за да добавите нова тема.</p>
          {/* login logic */}
        </div>
      )}
      <form onSubmit={handleSubmit} className="add-topic-form">
        <input
          type="text"
          placeholder="Заглавие"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
          className="add-topic-input"
          disabled={!HARDCODED_USER.isAuthenticated}
        />
        <select
            value={selectedCategory || ''}
            onChange={(e) => setSelectedCategory(Number(e.target.value))}
            required
            className="add-topic-select"
            disabled={!HARDCODED_USER.isAuthenticated}
        >
            {categories.map(category => (
                <option key={category.id} value={category.id}>{category.name}</option>
            ))}
        </select>
        <textarea
          placeholder="Съдържание"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
          rows={5}
          className="add-topic-textarea"
          disabled={!HARDCODED_USER.isAuthenticated}
        />
        <button 
          type="submit" 
          className="add-topic-submit" 
          disabled={loading || !HARDCODED_USER.isAuthenticated}
        >
          {loading ? "Добавяне..." : "Добави"}
        </button>
        {error && <div className="add-topic-error">{error}</div>}
        {HARDCODED_USER.isAuthenticated && (
          <div className="current-user-info">
            <small>Влизате като: {HARDCODED_USER.username}</small>
          </div>
        )}
      </form>
    </div>
  );
}
