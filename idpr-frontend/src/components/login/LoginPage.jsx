import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';

const LoginPage = () => {
  const navigate = useNavigate();

  const [credentials, setCredentials] = useState({
    username: '',
    password: ''
  });

  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials(prev => ({ ...prev, [name]: value }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
      });

      if (!response.ok) {
        throw new Error('Login failed');
      }

      const data = await response.json();
      localStorage.setItem('jwtToken', data.jwtToken);
      navigate('/home');
    } catch (err) {
      setError(err.message);
      setTimeout(() => {
        navigate('/register');
      }, 2000); // Redirect after showing error for 2s
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleLogin} className="login-form">
        <div className="form-group">
          <label>Username</label>
          <input
            type="text"
            name="username"
            value={credentials.username}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={credentials.password}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit" className="login-btn">Login</button>
      </form>

      <div>
        <button onClick={() => navigate('/home')}>
          Skip login
        </button>
        </div>
      {error && <div className="error-message">{error}</div>}

      <p>
        Don't have an account?{' '}
        <button className="register-now-btn" onClick={() => navigate('/register')}>
          Register Now
        </button>
      </p>
    </div>
  );
};

export default LoginPage;
