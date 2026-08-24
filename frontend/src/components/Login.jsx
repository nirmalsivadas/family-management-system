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
      localStorage.setItem('user',JSON.stringify(response.data.data));
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
      <h1>Family Management System</h1>
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Login</h2>
        {error && <div className="error">{error}</div>}
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input type="email" name='email' value={form.email} onChange={handleChange}/>
        </div>
        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input type="password" name='password' value={form.password} onChange={handleChange}/>
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Logging in...' : 'Login'}
        </button>
        <Link to="/forgot-password" className="forgot-password">
          Forgot Password?
        </Link>
      </form>
      <div>Don't have an account? <Link to="/signup">Sign Up</Link></div>
    </div>
  )
}

export default Login;