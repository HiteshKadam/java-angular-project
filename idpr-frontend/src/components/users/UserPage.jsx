import React, { useState, useEffect } from 'react';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import './EditUserPage.css';

const EditUserPage = () => {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const jwt = localStorage.getItem('jwtToken');
  const passedUser = location.state?.user;

  const [userData, setUserData] = useState({
    id: '',
    username: '',
    aadharNumber: '',
    panNumber: '',
    mobileNumber: ''
  });

  const [toast, setToast] = useState({ show: false, message: '', type: '' });
  const [isAuthorized, setIsAuthorized] = useState(false);

  useEffect(() => {
    validateJwtAndSetAccess();
    if (passedUser) {
      setUserData(passedUser);
    } else {
      fetchUserById();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const validateJwtAndSetAccess = () => {
    if (!jwt) {
      setIsAuthorized(false);
      return;
    }

    try {
      const decoded = jwtDecode(jwt);

      if (decoded && decoded.userId && String(decoded.userId) === String(id)) {
        setIsAuthorized(true);
      } else {
        setIsAuthorized(false);
      }

    } catch (error) {
      setIsAuthorized(false);
    }
  };

  const fetchUserById = async () => {
    try {
      const response = await fetch(`http://localhost:8080/user/${id}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${jwt}`,
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) throw new Error('Failed to fetch user');

      const user = await response.json();
      setUserData(user);

    } catch (err) {
      showToast('Failed to fetch user.', 'error');
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();

    if (!isAuthorized) {
      showToast('Unauthorized action.', 'error');
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/user/${id}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${jwt}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
      });

      const resultMessage = await response.text();

      if (response.ok && resultMessage === 'User updated successfully!') {
        showToast(resultMessage, 'success');
        setTimeout(() => navigate('/user/all'), 2000);
      } else {
        showToast(resultMessage || 'Failed to update user.', 'error');
      }

    } catch (err) {
      showToast(err.message || 'An unexpected error occurred.', 'error');
    }
  };

  // ✅ New Delete Handler
  const handleDelete = async () => {
    if (!isAuthorized) {
      showToast('Unauthorized action.', 'error');
      return;
    }

    const confirmDelete = window.confirm('Are you sure you want to delete this user?');
    if (!confirmDelete) return;

    try {
      const response = await fetch(`http://localhost:8080/user/${id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${jwt}`
        }
      });

      const resultMessage = await response.text();

      if (!response.ok) {
        showToast(resultMessage || 'Failed to delete user.', 'error');
        return;
      }

      showToast('User deleted successfully!', 'success');
      setTimeout(() => navigate('/user/all'), 2000);

    } catch (err) {
      showToast(err.message || 'An unexpected error occurred.', 'error');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData(prev => ({ ...prev, [name]: value }));
  };

  const showToast = (message, type) => {
    setToast({ show: true, message, type });
    setTimeout(() => {
      setToast({ show: false, message: '', type: '' });
    }, 2000);
  };

  return (
    <div className="edit-user-container">
      <h2>Edit User</h2>

      <form onSubmit={handleUpdate} className="edit-user-form">
        <div className="form-group">
          <label htmlFor="id">ID:</label>
          <input type="text" id="id" value={userData.id} disabled />
        </div>

        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            name="username"
            value={userData.username}
            onChange={handleChange}
            required
            readOnly={!isAuthorized}
          />
        </div>

        <div className="form-group">
          <label htmlFor="aadharNumber">Aadhar Number:</label>
          <input
            type="text"
            id="aadharNumber"
            name="aadharNumber"
            value={userData.aadharNumber}
            onChange={handleChange}
            required
            readOnly={!isAuthorized}
          />
        </div>

        <div className="form-group">
          <label htmlFor="panNumber">PAN Number:</label>
          <input
            type="text"
            id="panNumber"
            name="panNumber"
            value={userData.panNumber}
            onChange={handleChange}
            required
            readOnly={!isAuthorized}
          />
        </div>

        <div className="form-group">
          <label htmlFor="mobileNumber">Mobile Number:</label>
          <input
            type="text"
            id="mobileNumber"
            name="mobileNumber"
            value={userData.mobileNumber}
            onChange={handleChange}
            required
            readOnly={!isAuthorized}
          />
        </div>

        <div className="form-actions">
          <button
            type="submit"
            className={`btn update-btn ${!isAuthorized ? 'disabled-btn' : ''}`}
            disabled={!isAuthorized}
          >
            {isAuthorized ? 'Update User' : 'Unauthorized'}
          </button>

          <button
            type="button"
            className="btn cancel-btn-edit"
            onClick={() => navigate('/user/all')}
          >
            Cancel
          </button>

          {/* ✅ Delete Button */}
          {isAuthorized && (
            <button
              type="button"
              className="btn delete-btn-edit"
              onClick={handleDelete}
            >
              Delete User
            </button>
          )}
        </div>
      </form>

      {toast.show && (
        <div className={`toast ${toast.type}`}>
          {toast.message}
        </div>
      )}
    </div>
  );
};

export default EditUserPage;
