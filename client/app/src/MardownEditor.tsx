import { useEffect, useRef } from "react";
import Quill from "quill";
import "quill/dist/quill.snow.css";
// @ts-expect-error: quilljs-markdown has no types
import QuillMarkdown from "quilljs-markdown";
import hljs from "highlight.js";
import "highlight.js/styles/github-dark.css";

export default function MarkdownEditor({
  value,
  onEditorChange,
}: {
  value: string;
  onEditorChange: (value: string) => void;
}) {
  const editorRef = useRef<HTMLDivElement>(null);
  const quillRef = useRef<Quill | null>(null);

  useEffect(() => {
    if (editorRef.current && !quillRef.current) {
      const toolbarOptions = [
        [{ header: [1, 2, false] }],
        [{ align: [] }],
        ["bold", "italic", "underline"],
        [{ list: "ordered" }, { list: "bullet" }],
        ["link"],
        ["code-block"],
      ];

      const quill = new Quill(editorRef.current, {
        theme: "snow",
        modules: {
          toolbar: toolbarOptions,
        },
      });

      quillRef.current = quill;

      new QuillMarkdown(quill, {});

      if (value) {
        quill.root.innerHTML = value;
      }

      quill.on("text-change", () => {
        const content = quill.root.innerHTML;
        onEditorChange(content);
      });
    }
  }, [onEditorChange, value]);

  useEffect(() => {
    if (quillRef.current) {
      const editor = quillRef.current;
      const isFocused = document.activeElement === editor.root;
      const currentHTML = editor.root.innerHTML;

      if (!isFocused && value !== currentHTML) {
        editor.root.innerHTML = value;
      }
    }
  }, [value]);

  useEffect(() => {
    if (quillRef.current) {
      setTimeout(() => {
        if (!quillRef.current) return;
        const editor = quillRef.current.root;
        editor
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
                  console.error(
                    "HLJS error for block (editor):",
                    block.outerHTML,
                    e
                  );
              }
            }
          });
      }, 50);
    }
  }, [value]);

  return (
    <div className="markdown-editor">
      <div ref={editorRef} data-test="upload-material-content-editor" />
    </div>
  );
}
