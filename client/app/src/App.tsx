import { Routes, Route, Link } from 'react-router-dom';
import './App.css'
import {Topics} from './Topics.tsx'
import Home from './Home';
import ErrorPage from "./pages/ErrorPage";

function App() {
  return (
    <div>
          <nav>
            <Link to="/">Home</Link>&nbsp;<span className="text-muted">|</span>&nbsp;
            <Link to="/topics">Topics</Link>
          </nav>

          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/topics" element={<Topics />} />
            <Route path="*" element={<ErrorPage statusCode={404} />} />
          </Routes>
        </div>
  )
}

export default App;
