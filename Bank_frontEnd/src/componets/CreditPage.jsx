import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

function CreditPage() {
  const [amount, setAmount] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [updatedAccountInfo, setUpdatedAccountInfo] = useState(null);
  const token = localStorage.getItem("authToken");
  const location = useLocation();
  const navigate = useNavigate();

  const accountNumber = location.state?.accountInfo?.accoutNumber;

  const handleCredit = async (e) => {
    e.preventDefault();
    try {
      if (!token) {
        navigate("/login");
        return;
      }

      const response = await fetch("http://localhost:8081/api/user/credit", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ accountNumber, amount: parseFloat(amount) }),
      });

      if (!response.ok) {
        throw new Error("Failed to credit the account.");
      }

      const data = await response.json();
      setUpdatedAccountInfo(data.accountInfo);
      setMessage(`Amount credited successfully! New balance is ₹${data.accountInfo.accountBalance}`);
      setError("");
    } catch (error) {
      setMessage("");
      setError(error.message);
    }
  };

  const handleBackToDashboard = () => {
    navigate("/dashboard", { state: { accountInfo: updatedAccountInfo || location.state?.accountInfo } });
  };

  return (
    <div className="container mt-5">
      {error && <div className="alert alert-danger">{error}</div>}
      {message && <div className="alert alert-success">{message}</div>}
      <h2 className="text-center">Credit Account</h2>
      <form onSubmit={handleCredit} className="mt-4">
        <div className="mb-3">
          <label htmlFor="amount" className="form-label">
            Enter Amount:
          </label>
          <input
            type="number"
            className="form-control"
            id="amount"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
            min="1"
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Add Money
        </button>
        <button
          type="button"
          className="btn btn-secondary ms-3"
          onClick={handleBackToDashboard}
        >
          Back to Dashboard
        </button>
      </form>
      {updatedAccountInfo && (
        <div className="mt-4">
          <h4>Updated Account Details</h4>
          <p><strong>Account Name:</strong> {updatedAccountInfo.accountName}</p>
          <p><strong>Account Number:</strong> {updatedAccountInfo.accoutNumber}</p>
          <p><strong>New Balance:</strong> ₹{updatedAccountInfo.accountBalance}</p>
        </div>
      )}
    </div>
  );
}

export default CreditPage;