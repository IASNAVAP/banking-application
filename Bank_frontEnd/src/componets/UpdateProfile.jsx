import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const token = localStorage.getItem("authToken");

function UpdateProfile() {
  const navigate = useNavigate();
  const location = useLocation();
  const accountInfo = location.state?.accountInfo;

  const [formData, setFormData] = useState({
    accountNumber: accountInfo?.accoutNumber || "", // Use from state but disable editing
    address: "",
    stateOfOrigin: "",
    email: "",
    phoneNumber: "",
    alternativePhoneNumber: "",
  });

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    try {
      const response = await fetch("http://localhost:8081/api/user/updateuser", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error("Failed to update profile. Please try again.");
      }

      const data = await response.text();
      setMessage(data.message || "Profile updated successfully.");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="container mt-5">
      <h2 className="text-center">Update Profile</h2>
      {message && <div className="alert alert-success">{message}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={handleSubmit}>
        {/* Display Account Number */}
        <div className="mb-3">
          <label htmlFor="accountNumber" className="form-label">
            Account Number
          </label>
          <input
            type="text"
            id="accountNumber"
            name="accountNumber"
            className="form-control"
            value={formData.accountNumber}
            readOnly // Makes the field read-only
          />
        </div>
        <div className="mb-3">
          <label htmlFor="address" className="form-label">Address</label>
          <input
            type="text"
            id="address"
            name="address"
            className="form-control"
            value={formData.address}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="stateOfOrigin" className="form-label">State of Origin</label>
          <input
            type="text"
            id="stateOfOrigin"
            name="stateOfOrigin"
            className="form-control"
            value={formData.stateOfOrigin}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="email" className="form-label">Email</label>
          <input
            type="email"
            id="email"
            name="email"
            className="form-control"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="phoneNumber" className="form-label">Phone Number</label>
          <input
            type="text"
            id="phoneNumber"
            name="phoneNumber"
            className="form-control"
            value={formData.phoneNumber}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="alternativePhoneNumber" className="form-label">
            Alternative Phone Number
          </label>
          <input
            type="text"
            id="alternativePhoneNumber"
            name="alternativePhoneNumber"
            className="form-control"
            value={formData.alternativePhoneNumber}
            onChange={handleChange}
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Update Profile
        </button>
        <button
          type="button"
          className="btn btn-secondary ms-3"
          onClick={() => navigate(-1)}
        >
          Back
        </button>
      </form>
    </div>
  );
}

export default UpdateProfile;
