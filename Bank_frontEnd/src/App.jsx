import './App.css'
import UserRegistrationForm from './componets/UserRegistrationForm'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from './componets/Home';
import AccountDetails from './componets/AccountDetails';
import Login from './componets/Login';
import DashboardPage from './componets/DashboardPage';
import CreditPage from './componets/CreditPage';
import TransferPage from './componets/TransferPage';
import UpdateProfile from './componets/UpdateProfile';


function App() {


  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/register" element={<UserRegistrationForm />} />
        <Route path="/account-details" element={<AccountDetails />} />
        <Route path="/login" element={<Login />} /> {/* Login page route */}
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/credit" element={<CreditPage />} />
        <Route path="/transfer" element={<TransferPage />} />
        <Route path="/updateprofile" element={<UpdateProfile />} />
        
      </Routes>
    </Router>

   
  ) 
}

export default App
