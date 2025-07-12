import './App.css'
import AdminPanel from './components/AdminPanel.tsx';
import {BrowserRouter as Router, Link, Route, Routes} from 'react-router-dom';


function Navigation() {
    return (
        <nav style={{padding: '10px', background: '#eee'}}>
            <Link to="/admin">Административен панел</Link>
        </nav>
    );
}

function App() {
    return (
        <Router>
            <Navigation/>
            <Routes>
                <Route path="/admin" element={<AdminPanel/>}/>
            </Routes>
        </Router>
    )
}

export default App
