import React,{useState} from 'react';
import {Link,useNavigate} from 'react-router-dom';
import './Login.css';
import api from '../api/axios';

function Login(){
  const navigate = useNavigate();
  const [form,setForm] = useState({
    email: '',
    password: '',
  })

  const [error,setError] = useState('');
  const [loading,setLoading] = useState(false);

  function handleChange(e){
    setForm({
      ...form,
      [e.target.name]: e.target.value
    })
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    setLoading(true);
    try{
      const response = await api.post('/auth/login',form);
      const loggedInUser = response.data?.data ?? response.data;
      localStorage.setItem('user',JSON.stringify(loggedInUser));
      navigate('/dashboard');
    }catch(err){
      const message = err.response?.data?.message || "Login failed";
      setError(message);
    }finally{
      setLoading(false);
    }
  }



  return(
    <div className="login-container">
      <div className="auth-brand">
        <div className="auth-logo">FM</div>
        <h1>FamilyMgmt Portal</h1>
        <p>Family Membership Management System</p>
      </div>
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Sign in to your account</h2>
        {error && <div className="error">{error}</div>}
        <div className="form-group">
          <label htmlFor="email">Email address<span>*</span></label>
          <input id="email" type="email" name='email' value={form.email} onChange={handleChange} placeholder="you@example.com"/>
        </div>
        <div className="form-group">
          <label htmlFor="password">Password<span>*</span></label>
          <input id="password" type="password" name='password' value={form.password} onChange={handleChange} placeholder="Enter your password"/>
        </div>
        <div className="login-options">
          <label>
            <input type="checkbox" />
            Remember me
          </label>
          <Link to="/forgot-password" className="forgot-password">
            Forgot password?
          </Link>
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Signing in...' : 'Sign in'}
        </button>
        <p className="auth-switch">Don't have an account? <Link to="/signup">Create account</Link></p>
      </form>
    </div>
  )
}

export default Login;
