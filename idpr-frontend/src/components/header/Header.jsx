import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {jwtDecode} from 'jwt-decode';
import './Header.css';

const Header = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [userId, setUserId] = useState(null);
  const [username, setUsername] = useState('');

  useEffect(() => {
    validateJwtAndSetUser();
  }, []);

  // ✅ Validate JWT and extract userId/username
  const validateJwtAndSetUser = () => {
    const jwt = localStorage.getItem('jwtToken');

    if (!jwt) {
      setUserId(null);
      setUsername('');
      return;
    }

    try {
      const decoded = jwtDecode(jwt);

      if (decoded && decoded.userId) {
        setUserId(decoded.userId);
        setUsername(decoded.username || ''); // Optional: Assuming you have username in JWT
      } else {
        setUserId(null);
        setUsername('');
      }
    } catch (error) {
      setUserId(null);
      setUsername('');
    }
  };

  // ✅ Logout Handler
  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    setUserId(null);
    setUsername('');
    navigate('/login');
  };

  return (
    <div className="nav-header">
      <div className="logo" onClick={() => navigate('/')}>
        IDPR
      </div>

      <div className="nav-links">
        <button
          className={location.pathname === '/' ? 'active' : ''}
          onClick={() => navigate('/')}
        >
          Home
        </button>

        <button
          className={location.pathname === '/privacy-policy' ? 'active' : ''}
          onClick={() => navigate('/privacy-policy')}
        >
          Privacy Policy
        </button>

        {/* ✅ Show Edit Profile, User Documents, and Logout when logged in */}
        {userId ? (
          <>
            <button
              className={location.pathname === `/user/${userId}` ? 'active' : ''}
              onClick={() => navigate(`/user/${userId}`)}
            >
              Edit Profile
            </button>

            <button
              className={location.pathname === '/user/documents' ? 'active' : ''}
              onClick={() => navigate('/user/documents')}
            >
              User Documents
            </button>

            <button className="logout-btn" onClick={handleLogout}>
              Logout {username && `(${username})`}
            </button>
          </>
        ) : (
          <>
            {/* ✅ Show Login/Register if NOT logged in */}
            <button
              className={location.pathname === '/login' ? 'active' : ''}
              onClick={() => navigate('/login')}
            >
              Login
            </button>

            <button
              className={location.pathname === '/register' ? 'active' : ''}
              onClick={() => navigate('/register')}
            >
              Register
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default Header;
