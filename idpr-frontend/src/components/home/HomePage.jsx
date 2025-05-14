/* eslint-disable jsx-a11y/no-distracting-elements */
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';
import Header from '../header/Header';
import Footer from '../footer/Footer';

const HomePage = () => {
  const navigate = useNavigate();

  const sliderData = [
    {
      title: 'Protect Personal Data',
      description: 'Data security ensures your personal information stays private and protected from unauthorized access.',
      img: 'https://img.freepik.com/free-vector/privacy-policy-concept-illustration_114360-7873.jpg'
    },
    {
      title: 'Prevent Identity Theft',
      description: 'Safeguarding sensitive data helps prevent identity theft and fraud activities.',
      img: 'https://img.freepik.com/free-vector/identity-theft-concept-illustration_114360-3580.jpg'
    },
    {
      title: 'Ensure Trust & Compliance',
      description: 'Organizations build trust and comply with data privacy laws by securing data effectively.',
      img: 'https://img.freepik.com/free-vector/secure-server-concept-illustration_114360-494.jpg'
    }
  ];

  return (
    <div className="home-container">
      <Header />
      <marquee className="marquee-text" behavior="scroll" direction="left">
        🔐 Your Data, Our Priority! | Secure, Reliable & Trusted 🔐
      </marquee>

      <header className="header">
        <h1>Welcome to IDPR</h1>
        <p>Protecting Your Data with Next-Gen Security Solutions</p>
        <button onClick={() => navigate('/login')} className="get-started-btn">
          Get Started
        </button>
      </header>

      <section className="info-section">
        <h2>Why is Data Security Important?</h2>
        <div className="info-cards">
          <div className="info-card">
            <h3>Privacy Protection</h3>
            <p>Data security helps keep your personal and financial information safe from cybercriminals.</p>
          </div>
          <div className="info-card">
            <h3>Compliance & Regulations</h3>
            <p>Secure data management ensures your organization complies with GDPR, HIPAA, and other regulations.</p>
          </div>
          <div className="info-card">
            <h3>Business Integrity</h3>
            <p>Protecting customer data builds trust and maintains your brand’s integrity in the digital landscape.</p>
          </div>
        </div>
      </section>

      <section className="slider-section">
        <h2>How We Keep You Secure</h2>
        <div className="slider-container">
          {sliderData.map((item, index) => (
            <div className="slide" key={index}>
              <img src={item.img} alt={item.title} />
              <h3>{item.title}</h3>
              <p>{item.description}</p>
            </div>
          ))}
        </div>
      </section>

      <Footer />
    </div>
  );
};

export default HomePage;
