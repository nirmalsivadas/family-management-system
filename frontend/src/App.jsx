import {BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom'
import './App.css'
import Signup from './components/Signup'
import Login from './components/Login'
import TermsAndConditions from './components/TermsAndConditions'
import Dashboard from './components/Dashboard'



function App() {

  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/login" element={<Login />} />
          <Route path="/terms-and-conditions" element={<TermsAndConditions />} />
          <Route path="/dashboard" element={<Dashboard />} />
        </Routes>
      </Router>
    </>
  )
}

export default App
