import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { Topics } from './Topics'
import { TopicDetail } from './TopicDetail'

function App() {
  return (
    <Router>
      <div className="app">
        <header className="app-header">
          <h1>Forum</h1>
        </header>
        <main className="app-main">
          <Routes>
            <Route path="/" element={<Topics />} />
            <Route path="/topic/:id" element={<TopicDetail />} />
          </Routes>
        </main>
      </div>
    </Router>
  )
}

export default App
