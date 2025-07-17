import "./App.css";
import { Routes, Route } from "react-router-dom";
import AddTopic from "./AddTopic";
import Topics  from "./Topics";
import SearchPage  from "./SearchPage";
import './App.css'
import AdminPanel from './components/AdminPanel.tsx';

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Topics />} />
        <Route path="/add-topic" element={<AddTopic />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/admin" element={<AdminPanel/>}/>
      </Routes>
    </>
  );
}

export default App
