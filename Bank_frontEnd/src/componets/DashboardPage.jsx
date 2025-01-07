import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import TranscationHistory from "./TransactionHistory";

function DashboardPage() {
  const [accountInfo, setAccountInfo] = useState(null);
  const [error, setError] = useState("");
  const location = useLocation();
  const navigate = useNavigate();
  const token = localStorage.getItem("authToken");

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        if (!token) {
          navigate("/login");
          return;
        }

        const accountNumber = location.state?.accountInfo?.accoutNumber;
        const response = await fetch(
          "http://localhost:8081/api/user/userinfo",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({ accountNumber }),
          }
        );

        if (!response.ok) {
          throw new Error(
            "Failed to fetch account details. Please log in again."
          );
        }

        const data = await response.json();
        setAccountInfo(data);
      } catch (error) {
        setError(error.message);
      }
    };

    fetchUserInfo();
  }, [token, navigate, location.state]);

  const handleLogout = () => {
    localStorage.removeItem("authToken");
    navigate("/");
  };

  return (
    <div className="container mt-5">
      {error ? (
        <div className="alert alert-danger">{error}</div>
      ) : accountInfo ? (
        <div>
          <h2 className="text-center">Welcome, {accountInfo.accountName}</h2>
          <div className="mt-4">
            <p>
              <strong>Account Number:</strong> {accountInfo.accoutNumber}
            </p>
            <p>
              <strong>Account Balance:</strong> ₹{accountInfo.accountBalance}
            </p>
          </div>
          <button
            className="btn btn-success mt-4 me-3"
            onClick={() => navigate("/credit", { state: { accountInfo } })}
          >
            Add Money
          </button>

          <button
            className="btn btn-warning mt-4 me-3"
            onClick={() => navigate("/transfer", { state: { accountInfo } })}
          >
            Transfer Money
          </button>
          <button
            className="btn btn-primary mt-4 me-3"
            onClick={() =>
              navigate("/updateprofile", { state: { accountInfo } })
            }
          >
            Update Profile
          </button>
          <button className="btn btn-danger mt-4" onClick={handleLogout}>
            Logout
          </button>
          <TranscationHistory
            accountNumber={accountInfo.accoutNumber}
            token={token}
          />
        </div>
      ) : (
        <div className="text-center">Loading account details...</div>
      )}
    </div>
  );
}

export default DashboardPage;
