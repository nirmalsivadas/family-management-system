import {BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom'
import './App.css'
import Signup from './components/Signup'
import Login from './components/Login'
import TermsAndConditions from './components/TermsAndConditions'
import Dashboard from './components/Dashboard'
import AppLayout from './components/AppLayout'
import RegisterFamily from './components/RegisterFamily'
import ViewFamilies from './components/ViewFamilies'
import ViewMembers from './components/ViewMembers'
import ViewFamily from './components/ViewFamily'
import Notification from './components/Notification'
import Settings from './components/Settings'
import Profile from './components/Profile'



function App() {

  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/login" element={<Login />} />
          <Route path="/terms-and-conditions" element={<TermsAndConditions />} />
          <Route element={<AppLayout />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/register-family" element={<RegisterFamily />} />
            <Route path="/view-families" element={<ViewFamilies />} />
            <Route path="/view-family/:membershipId" element={<ViewFamily />} />
            <Route path="/view-members" element={<ViewMembers />} />
            <Route path="/notifications" element={<Notification />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="/profile" element={<Profile />} />
          </Route>
        </Routes>
      </Router>
    </>
  )
}

export default App
