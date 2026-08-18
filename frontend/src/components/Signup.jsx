import React from 'react';
import {Link} from 'react-router-dom';
import Login from './Login.jsx';

function Signup(){
  return(
    <div>
      <form>
        <input type="text" />
        <label htmlFor="firstName">First Name</label>
        <input type="text" />
        <label htmlFor="lastName">Last Name</label>
        <input type="email" />
        <label htmlFor="email">Email</label>
        <input type="number" />
        <label htmlFor="mobileNumber">Mobile Number</label>
        <input type="password" />
        <label htmlFor="password">Password</label>
        <input type="password" />
        <label htmlFor="confirmPassword">Confirm Password</label>
        <button type="submit">Submit</button>
        <input type="checkbox" />
        <label htmlFor="termsAndConditions">I agree to the <Link to="/terms-and-conditions">terms and conditions</Link></label>
      </form>
      <div>Already have an account? <Link to="/login">Login</Link></div>
    </div>
  )
}

export default Signup;