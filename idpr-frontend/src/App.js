import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './components/login/LoginPage';
import UsersPage from './components/users/UserPage';
import EditUserPage from './components/users/EditUserPage';
import RegistrationPage from './components/register/RegistrationPage';
import HomePage from './components/home/HomePage';
import PrivacyPolicyPage from './components/privacypolicy/PrivacyPolicyPage';
import LearnMorePage from './components/learnmore/LearnMorePage';
import UserDocumentsPage from './components/userdocuments/UserDocumentsPage';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/user/documents" element={<UserDocumentsPage />} />
        <Route path="/user/all" element={<UsersPage />} />
        <Route path="/user/:id" element={<EditUserPage />} />
        <Route path="/privacy-policy" element={<PrivacyPolicyPage />} />
        <Route path="/learn-more" element={<LearnMorePage />} />
      </Routes>
    </Router>
  );
};

export default App;
