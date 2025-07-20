import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

const Navbar: React.FC = () => {
  return (
    <nav className="navbar">
      <div className="navbar-logo">
        <Link to="/">Forum</Link>
      </div>
      <ul className="navbar-links">
        <li><Link to="/">Topics</Link></li>
        <li><Link to="/search">Search</Link></li>
        <li><Link to="/admin">Admin Panel</Link></li>
      </ul>
    </nav>
  );
};

export default Navbar;
