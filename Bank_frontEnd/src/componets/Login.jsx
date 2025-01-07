import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8081/api/user/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });


      if (!response.ok) {
        throw new Error("Login failed! Please check your credentials.");
      }

      const data = await response.json();
      console.info(data)
      const token = data.responseMessage; // JWT Token
      const accountInfo = data.accountInfo;
      

      // Store the token in localStorage
      localStorage.setItem("authToken", token);

      console.info(accountInfo)
      // Navigate to the dashboard with account info
      navigate("/dashboard", { state: { accountInfo } });
    } catch (error) {
      setErrorMessage(error.message);
    }
  };

  return (
    <div className="container mt-5">
      <h2 className="text-center">Login</h2>
      <form className="mt-4" onSubmit={handleLogin}>
        {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
        <div className="mb-3">
          <label htmlFor="email" className="form-label">Email</label>
          <input
            type="email"
            id="email"
            className="form-control"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="password" className="form-label">Password</label>
          <input
            type="password"
            id="password"
            className="form-control"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="btn btn-primary w-100">Login</button>
      </form>
    </div>
  );
}

export default LoginPage;
