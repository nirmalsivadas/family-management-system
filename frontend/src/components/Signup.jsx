import React from 'react';
import {Link} from 'react-router-dom';
import './Signup.css';

function Signup(){
  return(
    <div className="signup-container">
      <h1>Family Management System</h1>
      <form className="signup-form">
        <h2>Create Account</h2>
        <div className='form-group'>
          <label htmlFor="firstName">First Name</label>
          <input type="text" />
        </div>
        <div className='form-group'>
          <label htmlFor="lastName">Last Name</label>
          <input type="text" />
        </div>
        <div className='form-group'>
          <label htmlFor="email">Email</label>
          <input type="email" />
        </div>
        <div className='form-group'>
          <label htmlFor="mobileNumber">Mobile Number</label>
          <input type="number" />
        </div>
        <div className='form-group'>
          <label htmlFor="password">Password</label>
          <input type="password" />
        </div>
        <div className='form-group'>
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input type="password" />
        </div>
        <button type="submit">Submit</button>
        <div className="terms-and-conditions">
          <input type="checkbox" />
          <label htmlFor="termsAndConditions">I agree to the <Link to="/terms-and-conditions">terms and conditions</Link></label>
        </div>
      </form>
      <div>Already have an account? <Link to="/login">Login</Link></div>
    </div>
  )
}

export default Signup;