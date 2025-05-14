import React from 'react';
import { useNavigate } from 'react-router-dom';
import './PrivacyPolicyPage.css';
import Header from '../header/Header';
import Footer from '../footer/Footer';

const PrivacyPolicyPage = () => {
  const navigate = useNavigate();

  const policyPoints = [
    {
      title: 'Data Collection',
      frontText: 'What data do we collect?',
      backText: 'We collect personal data such as your name, email, and usage data to enhance your experience.'
    },
    {
      title: 'Data Usage',
      frontText: 'How do we use your data?',
      backText: 'Your data is used for authentication, improving services, and ensuring secure access to our platform.'
    },
    {
      title: 'Data Protection',
      frontText: 'How do we protect your data?',
      backText: 'We implement encryption (AES-256), firewalls, and regular security audits to safeguard your data.'
    },
    {
      title: 'Third-Party Sharing',
      frontText: 'Do we share your data?',
      backText: 'We do not sell your data. We may share data with trusted partners who comply with strict privacy policies.'
    }
  ];

  return (
    <div className="privacy-container">
      <Header />

      <header className="privacy-header">
        <h1>Privacy Policy</h1>
        <p>Your privacy is important to us. Learn how we handle your data securely and responsibly.</p>
        <button onClick={() => navigate('/')} className="back-home-btn">Back to Home</button>
      </header>

      <section className="policy-cards-section">
        <h2>Our Privacy Practices</h2>
        <div className="flip-card-container">
          {policyPoints.map((point, index) => (
            <div className="flip-card" key={index}>
              <div className="flip-card-inner">
                <div className="flip-card-front">
                  <h3>{point.title}</h3>
                  <p>{point.frontText}</p>
                </div>
                <div className="flip-card-back">
                  <h3>{point.title}</h3>
                  <p>{point.backText}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* NEW CYBER SECURITY SECTION */}
      <section className="cyber-security-section">
        <h2>Cyber Security Awareness</h2>
        <p>
          In today’s digital world, staying informed about cyber threats is crucial. We take comprehensive
          steps to ensure your data is secure, but your awareness and participation play a vital role too.
        </p>

        <div className="cyber-security-tips">
          <div className="tip">
            <h3>Strong Passwords</h3>
            <p>Use complex passwords with a mix of letters, numbers, and special characters. Avoid common words and update passwords regularly.</p>
          </div>
          <div className="tip">
            <h3>Enable Two-Factor Authentication</h3>
            <p>Add an extra layer of protection to your accounts by enabling 2FA wherever possible.</p>
          </div>
          <div className="tip">
            <h3>Beware of Phishing</h3>
            <p>Don’t click on suspicious links or attachments in emails. Always verify the sender before sharing sensitive information.</p>
          </div>
          <div className="tip">
            <h3>Keep Software Updated</h3>
            <p>Regularly update your operating system, apps, and antivirus software to protect against known vulnerabilities.</p>
          </div>
        </div>

        <button className="learn-more-btn" onClick={() => navigate('/learn-more')}>
          Learn More About Cyber Security
        </button>
      </section>

      <Footer />
    </div>
  );
};

export default PrivacyPolicyPage;
