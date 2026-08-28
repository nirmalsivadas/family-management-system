import {BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom'
import './App.css'
import Signup from './components/Signup'
import Login from './components/Login'
import ForgotPassword from './components/ForgotPassword'
import TermsAndConditions from './components/TermsAndConditions'
import Dashboard from './components/Dashboard'
import AppLayout from './components/AppLayout'
import RegisterFamily from './components/RegisterFamily'
import ViewFamilies from './components/ViewFamilies'
import ViewMembers from './components/ViewMembers'
import ViewFamily from './components/ViewFamily'
import UpdateFamily from './components/UpdateFamily'
import Notification from './components/Notification'
import Settings from './components/Settings'
import Profile from './components/Profile'

function getStoredUser(){
  try{
    return JSON.parse(localStorage.getItem('user'));
  }catch{
    return null;
  }
}

function ProtectedLayout(){
  const user = getStoredUser();
  if(!user?.id){
    return <Navigate to="/login" replace />;
  }
  return <AppLayout />;
}

function PublicOnly({children}){
  const user = getStoredUser();
  if(user?.id){
    return <Navigate to="/dashboard" replace />;
  }
  return children;
}

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/signup" element={<PublicOnly><Signup /></PublicOnly>} />
          <Route path="/login" element={<PublicOnly><Login /></PublicOnly>} />
          <Route path="/forgot-password" element={<PublicOnly><ForgotPassword /></PublicOnly>} />
          <Route path="/terms-and-conditions" element={<TermsAndConditions />} />
          <Route element={<ProtectedLayout />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/register-family" element={<RegisterFamily />} />
            <Route path="/view-families" element={<ViewFamilies />} />
            <Route path="/view-family/:membershipId" element={<ViewFamily />} />
            <Route path="/update-family/:membershipId" element={<UpdateFamily />} />
            <Route path="/view-members" element={<ViewMembers />} />
            <Route path="/notifications" element={<Notification />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="/profile" element={<Profile />} />
          </Route>
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </Router>
    </>
  )
}

export default App
