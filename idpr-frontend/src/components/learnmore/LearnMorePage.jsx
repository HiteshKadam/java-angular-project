import React from 'react';
import { useNavigate } from 'react-router-dom';
import './LearnMorePage.css';
import Header from '../header/Header';
import Footer from '../footer/Footer';

const LearnMorePage = () => {
  const navigate = useNavigate();

  const topics = [
    {
      title: 'What is Cyber Security?',
      description: 'Cyber Security refers to protecting systems, networks, and data from digital attacks. It ensures data confidentiality, integrity, and availability.',
      img: 'https://img.freepik.com/free-vector/cyber-security-concept-illustration_114360-856.jpg'
    },
    {
      title: 'Common Threats',
      description: 'Malware, phishing, ransomware, and social engineering are common threats. Learn to recognize and avoid them.',
      img: 'https://img.freepik.com/free-vector/identity-theft-concept-illustration_114360-4394.jpg'
    },
    {
      title: 'How Encryption Works',
      description: 'Encryption converts data into a code to prevent unauthorized access. AES-256 is a popular and secure encryption standard.',
      img: 'https://img.freepik.com/free-vector/data-encryption-concept-illustration_114360-7304.jpg'
    }
  ];

  const bestPractices = [
    'Use strong, unique passwords and enable multi-factor authentication.',
    'Regularly update software and systems to patch vulnerabilities.',
    'Be cautious of suspicious emails and links. Always verify sources.',
    'Back up data regularly to prevent data loss from cyberattacks.',
    'Educate yourself and employees on cyber hygiene and security policies.'
  ];

  return (
    <div className="learn-more-container">
      <Header />

      <header className="learn-header">
        <h1>Learn More About Cyber Security</h1>
        <p>Empower yourself with knowledge to stay safe in the digital world.</p>
        <button onClick={() => navigate('/')} className="back-home-btn">Back to Home</button>
      </header>

      <section className="topic-section">
        <h2>Cyber Security Essentials</h2>
        <div className="topic-cards-container">
          {topics.map((topic, index) => (
            <div className="topic-card" key={index}>
              <img src={topic.img} alt={topic.title} />
              <h3>{topic.title}</h3>
              <p>{topic.description}</p>
            </div>
          ))}
        </div>
      </section>

      <section className="best-practices-section">
        <h2>Best Practices for Staying Secure</h2>
        <ul className="practices-list">
          {bestPractices.map((practice, index) => (
            <li key={index}>{practice}</li>
          ))}
        </ul>
      </section>

      <section className="cta-section">
        <h2>Take the Next Step</h2>
        <p>Protect your personal data and stay informed about the latest security trends.</p>
        <button onClick={() => navigate('/register')} className="get-started-btn">Get Started Now</button>
      </section>

      <Footer />
    </div>
  );
};

export default LearnMorePage;
