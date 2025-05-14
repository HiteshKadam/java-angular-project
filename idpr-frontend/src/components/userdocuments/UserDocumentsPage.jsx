/* eslint-disable react-hooks/exhaustive-deps */
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {jwtDecode} from "jwt-decode";
import Header from "../header/Header";
import Footer from "../footer/Footer";
import "./UserDocumentsPage.css";
import { FaDownload, FaEye, FaTrash } from "react-icons/fa"; // Added FaTrash for delete icon

const UserDocumentsPage = () => {
  const navigate = useNavigate();
  const jwt = localStorage.getItem("jwtToken");
  const [userId, setUserId] = useState(null);
  const [documents, setDocuments] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [fileType, setFileType] = useState("");
  const [toast, setToast] = useState({ show: false, message: "", type: "" }); // Toast state

  useEffect(() => {
    if (jwt) {
      try {
        const decoded = jwtDecode(jwt);
        setUserId(decoded.userId);
        fetchDocuments(decoded.userId);
      } catch (err) {
        navigate("/login");
      }
    } else {
      navigate("/login");
    }
  }, [jwt, navigate]);

  const showToast = (message, type) => {
    setToast({ show: true, message, type });
    setTimeout(() => {
      setToast({ show: false, message: '', type: '' });
    }, 2000);
  };

  const fetchDocuments = async (userId) => {
    try {
      const response = await fetch(`http://localhost:8080/api/files/list/${userId}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setDocuments(data);
      } else if(response.status === 404) {
        setDocuments([]);
        showToast("No documents found for this user.", "warn");
      }
        else {
        showToast("Failed to fetch documents.", "error");
      }
    } catch (error) {
      console.error("Error fetching documents:", error);
      showToast("An error occurred while fetching documents.", "error");
    }
  };

  const handleUpload = async () => {
    if (!selectedFile || !fileType) {
      showToast("Please select a file and specify file type!", "error");
      return;
    }

    const formData = new FormData();
    formData.append("file", selectedFile);
    formData.append("userId", userId);
    formData.append("fileType", fileType);

    try {
      const response = await fetch("http://localhost:8080/api/files/upload", {
        method: "POST",
        body: formData,
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });

      const result = await response.text();

      if (response.ok) {
        showToast(result, "success");
        fetchDocuments(userId);
      } else {
        showToast(`Upload failed: ${result}`, "error");
      }
    } catch (error) {
      console.error("Upload error:", error);
      showToast("An unexpected error occurred during the upload!", "error");
    }
  };

  const handleDownload = async (fileName) => {
    try {
      const response = await fetch(`http://localhost:8080/api/files/download/${userId}/${fileName}`, {
        method: "GET",
        headers: { Authorization: `Bearer ${jwt}` },
      });

      if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = fileName;
        document.body.appendChild(link);
        link.click();
        link.remove();
        showToast("File downloaded successfully!", "success");
      } else {
        showToast("Error downloading file.", "error");
      }
    } catch (error) {
      console.error("Download error:", error);
      showToast("An error occurred while downloading the file.", "error");
    }
  };

  const handleViewFile = async (fileName) => {
    try {
      const response = await fetch(`http://localhost:8080/api/files/view/${userId}/${fileName}`, {
        method: "GET",
        headers: { Authorization: `Bearer ${jwt}` },
      });

      if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        window.open(url, "_blank"); // Opens the file in a new tab
        showToast("File opened successfully!", "success");
      } else {
        showToast("Error viewing file.", "error");
      }
    } catch (error) {
      console.error("View error:", error);
      showToast("An error occurred while viewing the file.", "error");
    }
  };

  const handleDelete = async (fileName) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this file?");
    if (!confirmDelete) return;

    try {
      const response = await fetch(`http://localhost:8080/api/files/delete/${userId}/${fileName}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${jwt}` },
      });

      if (response.ok) {
        showToast("File deleted successfully!", "success");
        setTimeout(() => fetchDocuments(userId), 3000);
      } else {
        showToast("Error deleting file.", "error");
      }
    } catch (error) {
      console.error("Delete error:", error);
      showToast("An error occurred while deleting the file.", "error");
    }
  };

  return (
    <div className="documents-container">
      <Header />
      <h2>User Documents</h2>
      <div className="upload-section">
        <input type="file" onChange={(e) => setSelectedFile(e.target.files[0])} />
        <select
          value={fileType}
          onChange={(e) => setFileType(e.target.value)}
          className="file-type-dropdown"
        >
          <option value="">Select File Type</option>
          <option value="AadhaarCard">Aadhaar Card</option>
          <option value="PanCard">PAN Card</option>
          <option value="DateOfBirth">Date of Birth</option>
          <option value="Other">Other</option>
        </select>
        <button onClick={handleUpload} className="upload-btn">
          Upload
        </button>
      </div>
      <div className="documents-list">
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Type</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {documents.map((doc) => (
              <tr key={doc.id}>
                <td>{doc.fileName}</td>
                <td>{doc.fileType}</td>
                <td>
                  <button
                    onClick={() => handleDownload(doc.fileName)}
                    className="action-btn download-btn"
                  >
                    <FaDownload />
                  </button>
                  <button
                    onClick={() => handleViewFile(doc.fileName)}
                    className="action-btn view-btn"
                  >
                    <FaEye />
                  </button>
                  <button
                    onClick={() => handleDelete(doc.fileName)}
                    className="action-btn delete-btn"
                  >
                    <FaTrash />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {toast.show && (
        <div className={`toast ${toast.type}`}>
          {toast.message}
        </div>
      )}
      <Footer />
    </div>
  );
};

export default UserDocumentsPage;