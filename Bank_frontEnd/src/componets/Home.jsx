import React from "react";
import { useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();

  return (
    <div className="container mt-5 text-center">
      <h1>Welcome to the Banking Application</h1>
      <p>Please select an option below:</p>
      <div className="mt-4">
        <button
          className="btn btn-primary btn-lg mx-2"
          onClick={() => navigate("/register")}
        >
          Register
        </button>
        <button
          className="btn btn-secondary btn-lg mx-2"
          onClick={() => navigate("/login")}
        >
          Login
        </button>
      </div>
    </div>
  );
}

export default Home;
