import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './RegistrationPage.css';

const RegistrationPage = () => {
  const navigate = useNavigate();
  const [toast, setToast] = useState({ show: false, message: '', type: '' });
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    aadharNumber: '',
    panNumber: '',
    mobileNumber: ''
  });

  const [formErrors, setFormErrors] = useState({});
  const [isPolicyOpen, setIsPolicyOpen] = useState(false);
  const [isPolicyAgreed, setIsPolicyAgreed] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    setFormErrors((prev) => ({ ...prev, [name]: '' })); // Clear error for the field dynamically
  };

  const validateForm = () => {
    const errors = {};
    if (!formData.username.trim()) errors.username = 'Username is required.';
    if (!formData.password.trim()) errors.password = 'Password is required.';
    if (!formData.aadharNumber.trim()) errors.aadharNumber = 'Aadhar Number is required.';
    if (!formData.panNumber.trim()) errors.panNumber = 'PAN Number is required.';
    if (!formData.mobileNumber.trim()) errors.mobileNumber = 'Mobile Number is required.';
    if (!isPolicyAgreed) errors.policy = 'You must agree to the privacy policy.';
    setFormErrors(errors);
    return Object.keys(errors).length === 0; // Return true if no errors
  };

  const handleRegister = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return; // Stop if validation fails
    }

    try {
      const response = await fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        throw new Error('Registration failed');
      }

      showToast('Registration successful!', 'success');

      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err) {
      showToast(err.message, 'error');
    }
  };
  const showToast = (message, type) => {
    setToast({ show: true, message, type });
    setTimeout(() => {
      setToast({ show: false, message: '', type: '' });
    }, 2000);
  };
  
  const handlePolicyAgree = () => {
    setFormErrors((prev) => ({ ...prev, policy: '' })); 
    setIsPolicyAgreed(true);
    setIsPolicyOpen(false); 
  };

  const handleCloseModal = () => {
    setIsPolicyOpen(false); // Close the modal without agreeing
  };

  const handleCheckboxChange = (e) => {
    setIsPolicyAgreed(e.target.checked); // Update state when the checkbox is toggled
  };

  return (
    <div className="register-container">
      <h2>Register</h2>

      <form onSubmit={handleRegister} className="register-form">
        <div className="form-group">
          <label>Username</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            className={formErrors.username ? 'error-input' : ''}
          />
          {formErrors.username && <span className="error">{formErrors.username}</span>}
        </div>

        <div className="form-group">
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            className={formErrors.password ? 'error-input' : ''}
          />
          {formErrors.password && <span className="error">{formErrors.password}</span>}
        </div>

        <div className="form-group">
          <label>Aadhar Number</label>
          <input
            type="text"
            name="aadharNumber"
            value={formData.aadharNumber}
            onChange={handleChange}
            className={formErrors.aadharNumber ? 'error-input' : ''}
          />
          {formErrors.aadharNumber && <span className="error">{formErrors.aadharNumber}</span>}
        </div>

        <div className="form-group">
          <label>PAN Number</label>
          <input
            type="text"
            name="panNumber"
            value={formData.panNumber}
            onChange={handleChange}
            className={formErrors.panNumber ? 'error-input' : ''}
          />
          {formErrors.panNumber && <span className="error">{formErrors.panNumber}</span>}
        </div>

        <div className="form-group">
          <label>Mobile Number</label>
          <input
            type="text"
            name="mobileNumber"
            value={formData.mobileNumber}
            onChange={handleChange}
            className={formErrors.mobileNumber ? 'error-input' : ''}
          />
          {formErrors.mobileNumber && <span className="error">{formErrors.mobileNumber}</span>}
        </div>

        <button
          type="button"
          className="register-btn"
          onClick={() => setIsPolicyOpen(true)} // Open the modal
        >
          Open Privacy Policy
        </button>
        {formErrors.policy && <span className="error">{formErrors.policy}</span>}

        <button type="submit" className="register-btn" disabled={!isPolicyAgreed} >
          Register
        </button>
        <button
          type="button"
          className="cancel-btn"
          onClick={() => navigate('/login')}
        >
          Back to Login
        </button>
      </form>
      {toast.show && (
        <div className={`toast ${toast.type}`}>
          {toast.message}
        </div>
      )}
      {isPolicyOpen && (
        <div className="modal">
          <div className="modal-content">
            <center><h1>Privacy Policy</h1></center>
            <div className="privacy-policy">
              <p>
              At IDPR, we value your privacy and are committed to protecting your personal data.
              We collect and process information such as Aadhaar, PAN, mobile numbers and uploaded files solely for authentication and security purposes.
              All data is encrypted during storage and transmission, ensuring maximum protection.
              We do not sell or share your information with third parties except for legal compliance and security audits.
              You have the right to access, update, or request deletion of your data under GDPR & CCPA regulations.
              We retain information only as long as necessary for service functionality and compliance.
              By using our platform, you agree to this Privacy Policy, which may be updated periodically.
              </p>
              <p>
                For more details, please contact our support team or visit our website.
              </p>
            </div>
            <center>
            <div className="form-check">
              <input
                type="checkbox"
                id="policy-agree"
                checked={isPolicyAgreed}
                onChange={handleCheckboxChange}
              />
              <label htmlFor="policy-agree">I agree to the Privacy Policy</label>
            </div>
            </center>
            <div className="modal-buttons">
              <button
                className="register-btn"
                onClick={handlePolicyAgree}
                disabled={!isPolicyAgreed} // Disable button until checkbox is checked
              >
                Agree and Close
              </button>
              <button
                className="cancel-btn"
                onClick={handleCloseModal} // Close the modal without agreeing
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default RegistrationPage;