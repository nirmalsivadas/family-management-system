import React from 'react';
import {Link} from 'react-router-dom';

function Login(){
  return(
    <div>
      <form>
        <input type="email" />
        <label htmlFor="email">Email</label>
        <input type="password" />
        <label htmlFor="password">Password</label>
        <button type="submit">Login</button>
      </form>
      <div>Don't have an account? <Link to="/signup">Sign Up</Link></div>
    </div>
  )
}

export default Login;