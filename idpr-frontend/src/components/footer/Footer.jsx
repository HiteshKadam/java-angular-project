import React from 'react';
import './Footer.css';

const Footer = () => {
  return (
    <footer className="main-footer">
      <div className="footer-content">
        <div className="footer-logo">
          <h2>IDPR</h2>
          <p>Empowering secure digital identities.</p>
        </div>

        <div className="footer-links">
          <h4>Quick Links</h4>
          <ul>
            <li><a href="/">Home</a></li>
            <li><a href="/privacy-policy">Privacy Policy</a></li>
            <li><a href="/learn-more">Learn More</a></li>
            <li><a href="/register">Register</a></li>
            <li><a href="/login">Login</a></li>
          </ul>
        </div>

        <div className="footer-contact">
          <h4>Contact Us</h4>
          <p>Email: support@idpr.com</p>
          <p>Phone: +1 234 567 8901</p>
        </div>
      </div>

      <div className="footer-bottom">
        <p>&copy; 2025 IDPR. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;
