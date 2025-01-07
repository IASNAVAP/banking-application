import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

function TransferPage() {
  const [destinationAccountNumber, setDestinationAccountNumber] = useState("");
  const [amount, setAmount] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [updatedAccountInfo, setUpdatedAccountInfo] = useState(null);
  const token = localStorage.getItem("authToken");
  const location = useLocation();
  const navigate = useNavigate();

  // Extract account info from location state or redirect if not found
  const accountInfo = location.state?.accountInfo;
  if (!accountInfo) {
    navigate("/login"); // If no account info found, redirect to login
    return;
  }

  const sourceAccountNumber = accountInfo.accoutNumber; // Automatically use user's account number as source

  const handleTransfer = async (e) => {
    e.preventDefault();
    try {
      if (!token) {
        navigate("/login");
        return;
      }

      const response = await fetch("http://localhost:8081/api/user/transfer", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          sourceAccountNumber,
          destinationAccountNumber,
          amount: parseFloat(amount),
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to transfer the amount.");
      }

      const data = await response.json();
      setUpdatedAccountInfo(data.accountInfo);
      setMessage(`Transfer successful! New balance is ₹${data.accountInfo.accountBalance}`);
      setError("");
    } catch (error) {
      setMessage("");
      setError(error.message);
    }
  };

  const handleBackToDashboard = () => {
    navigate("/dashboard", { state: { accountInfo: updatedAccountInfo || accountInfo } });
  };

  return (
    <div className="container mt-5">
      {error && <div className="alert alert-danger">{error}</div>}
      {message && <div className="alert alert-success">{message}</div>}
      <h2 className="text-center">Transfer Money</h2>
      <form onSubmit={handleTransfer} className="mt-4">
        <div className="mb-3">
          <label htmlFor="sourceAccountNumber" className="form-label">
            Source Account Number:
          </label>
          {/* Make the source account number uneditable */}
          <input
            type="text"
            className="form-control"
            id="sourceAccountNumber"
            value={sourceAccountNumber}
            disabled // Disable the input so it is not changeable
          />
        </div>
        <div className="mb-3">
          <label htmlFor="destinationAccountNumber" className="form-label">
            Destination Account Number:
          </label>
          <input
            type="text"
            className="form-control"
            id="destinationAccountNumber"
            value={destinationAccountNumber}
            onChange={(e) => setDestinationAccountNumber(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="amount" className="form-label">
            Amount:
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
          Transfer Money
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

export default TransferPage;
