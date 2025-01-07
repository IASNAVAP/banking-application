import React from "react";
import { useNavigate, useLocation } from "react-router-dom";

function AccountDetails() {
  const navigate = useNavigate();
  const location = useLocation();
  const accountInfo = location.state?.accountInfo;

  return (
    <div className="container mt-5 text-center">
      {accountInfo ? (
        <div>
          <h2>Account Created Successfully!</h2>
          <p className="mt-3">
            Welcome, <strong>{accountInfo.accountName}</strong>! Your account has been created.
          </p>
          <button
            className="btn btn-primary mt-4"
            onClick={() => navigate("/login")}
          >
            Go to Login
          </button>
        </div>
      ) : (
        <div>
          <h2>Error: No Account Information</h2>
          <p>Please try registering again or log in.</p>
          <button
            className="btn btn-primary mt-4"
            onClick={() => navigate("/login")}
          >
            Go to Login
          </button>
        </div>
      )}
    </div>
  );
}

export default AccountDetails;
