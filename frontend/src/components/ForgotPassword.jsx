import React,{useState} from 'react';
import {Link,useNavigate} from 'react-router-dom';
import './Login.css';
import api from '../api/axios';

function ForgotPassword(){
  const navigate = useNavigate();
  const [email,setEmail] = useState('');
  const [error,setError] = useState('');
  const [message,setMessage] = useState('');
  const [loading,setLoading] = useState(false);

  async function handleSubmit(e){
    e.preventDefault();
    setError('');
    setMessage('');
    if(!email.trim()){
      setError('Email is required.');
      return;
    }
    setLoading(true);
    try{
      const response = await api.post('/users/forgot-password',{email: email.trim()});
      setMessage(response.data?.data || 'Temporary password sent to your email.');
    }catch(err){
      setError(err.response?.data?.message || 'Unable to reset password.');
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
        <h2>Reset your password</h2>
        <p className="auth-switch">Enter your account email and we will send a temporary password.</p>
        {error && <div className="error">{error}</div>}
        {message && <div className="error" style={{background:'#ecfdf3',borderColor:'#abefc6',color:'#067647'}}>{message}</div>}
        <div className="form-group">
          <label htmlFor="email">Email address<span>*</span></label>
          <input id="email" type="email" name="email" value={email} onChange={(e)=>setEmail(e.target.value)} placeholder="you@example.com"/>
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Sending...' : 'Send temporary password'}
        </button>
        <p className="auth-switch">
          Remembered it? <Link to="/login">Back to sign in</Link>
        </p>
        {message && (
          <button type="button" onClick={()=>navigate('/login')}>
            Go to sign in
          </button>
        )}
      </form>
    </div>
  );
}

export default ForgotPassword;
