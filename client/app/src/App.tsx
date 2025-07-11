import "./App.css";
import { Routes, Route } from "react-router-dom";
import AddTopic from "./AddTopic";
import Topics  from "./Topics";

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Topics />} />
        <Route path="/add-topic" element={<AddTopic />} />
      </Routes>
    </>
  );
}

export default App;
