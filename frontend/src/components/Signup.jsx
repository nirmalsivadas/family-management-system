import React,{useState} from 'react';
import {Link,useNavigate} from 'react-router-dom';
import './Signup.css';
import api from '../api/axios';

function Signup(){
  const navigate = useNavigate();
  const [form,setForm] = useState({
    firstName: '',
    lastName: '',
    mobileNumber: '',
    email: '',
    password: '',
    confirmPassword: '',
  })
  const [error,setError] = useState('');
  const [accepted,setAccepted] = useState(false);
  const [loading,setLoading] = useState(false);

  function handleChange(e){
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  }

  async function handleSubmit(e){
    e.preventDefault();
    setError('');
    if(form.password !== form.confirmPassword){
      setError('Passwords do not match');
      return;
    }
    if(!accepted){
      setError('Please accept the terms and conditions');
      return;
    }
    setLoading(true);

    try{
      await api.post('/auth/signup',{
        firstName: form.firstName,
        lastName: form.lastName,
        mobileNumber: form.mobileNumber,
        email: form.email,
        password: form.password
      });
      navigate('/login');
    }
    catch(err){
      const message = err.response?.data?.message || "Signup failed";
      setError(message);
    }
    finally{
      setLoading(false);
    }
  }

  return(
    <div className="signup-container">
      <div className="auth-brand">
        <div className="auth-logo">FM</div>
        <h1>FamilyMgmt Portal</h1>
        <p>Family Membership Management System</p>
      </div>
      <form className="signup-form" onSubmit={handleSubmit}>
        <h2>Create your account</h2>
        {error && <div className="error">{error}</div>}
        <div className='form-group'>
          <label htmlFor="firstName">First name<span>*</span></label>
          <input type="text" id='firstName' name='firstName' value={form.firstName} onChange={handleChange} placeholder="Ravi"/>
        </div>
        <div className='form-group'>
          <label htmlFor="lastName">Last name<span>*</span></label>
          <input type="text" id='lastName' name='lastName' value={form.lastName} onChange={handleChange} placeholder="Verma"/>
        </div>
        <div className='form-group'>
          <label htmlFor="email">Email address<span>*</span></label>
          <input type="email" id='email' name='email' value={form.email} onChange={handleChange} placeholder="you@example.com"/>
        </div>
        <div className='form-group'>
          <label htmlFor="mobileNumber">Mobile number<span>*</span></label>
          <input type="text" id='mobileNumber' name='mobileNumber' value={form.mobileNumber} onChange={handleChange} placeholder="9876543210"/>
        </div>
        <div className='form-group'>
          <label htmlFor="password">Password<span>*</span></label>
          <input type="password" id='password' name='password' value={form.password} onChange={handleChange} placeholder="Create a password"/>
        </div>
        <div className='form-group'>
          <label htmlFor="confirmPassword">Confirm password<span>*</span></label>
          <input type="password" id='confirmPassword' name='confirmPassword' value={form.confirmPassword} onChange={handleChange} placeholder="Confirm password"/>
        </div>
        <div className="terms-and-conditions">
          <input id="termsAndConditions" type="checkbox" checked={accepted} onChange={(e)=>setAccepted(e.target.checked)} />
          <label htmlFor="termsAndConditions">I agree to the <Link to="/terms-and-conditions">terms and conditions</Link></label>
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Creating account...' : 'Create account'}
        </button>
        <p className="auth-switch">Already have an account? <Link to="/login">Sign in</Link></p>
      </form>
    </div>
  )
}

export default Signup;
