import React from 'react';
import {Link} from 'react-router-dom';
import './Login.css';

function Login(){
  return(
    <div className="login-container">
      <h1>Family Management System</h1>
      <form className="login-form">
        <h2>Login</h2>
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input type="email" />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input type="password" />
        </div>
        <button type="submit">Login</button>
        <Link to="/forgot-password" className="forgot-password">
          Forgot Password?
        </Link>
      </form>
      <div>Don't have an account? <Link to="/signup">Sign Up</Link></div>
    </div>
  )
}

export default Login;