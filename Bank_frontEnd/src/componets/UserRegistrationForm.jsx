import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function UserRegistrationForm() {
  const [userData, setUserData] = useState({
    firstName: "",
    lastName: "",
    otherName: "",
    gender: "",
    address: "",
    stateOfOrigin: "",
    email: "",
    password: "",
    phoneNumber: "",
    alternativePhoneNumber: "",
  });
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData({ ...userData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8081/api/user", userData);
      navigate("/account-details", { state: { accountInfo: res.data.accountInfo } });
 // Pass response data
    } catch (err) {
      console.error("Error creating user:", err);
      setError("Failed to create account. Please try again.");
    }
  };

  return (
    <div className="container mt-4">
      <h2>User Registration</h2>
      <form onSubmit={handleSubmit}>
        {Object.keys(userData).map((field) => (
          <div className="mb-3" key={field}>
            <label className="form-label" htmlFor={field}>
              {field.charAt(0).toUpperCase() + field.slice(1)}:
            </label>
            <input
              type={field === "password" ? "password" : "text"}
              className="form-control"
              id={field}
              name={field}
              value={userData[field]}
              onChange={handleInputChange}
              required
            />
          </div>
        ))}
        <button type="submit" className="btn btn-primary">
          Register
        </button>
      </form>

      {error && (
        <div className="alert alert-danger mt-4" role="alert">
          {error}
        </div>
      )}
    </div>
  );
}

export default UserRegistrationForm;
