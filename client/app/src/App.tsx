import "./App.css";
import { Routes, Route } from "react-router-dom";
import AddTopic from "./AddTopic";
import Topics  from "./Topics";
import SearchPage  from "./SearchPage";

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Topics />} />
        <Route path="/add-topic" element={<AddTopic />} />
        <Route path="/search" element={<SearchPage />} />
      </Routes>
    </>
  );
}

export default App
