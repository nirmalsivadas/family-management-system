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
      <h1>Family Management System</h1>
      <form className="signup-form" onSubmit={handleSubmit}>
        <h2>Create Account</h2>
        {error && <div className="error">{error}</div>}
        <div className='form-group'>
          <label htmlFor="firstName">First Name</label>
          <input type="text" id='firstName' name='firstName' value={form.firstName} onChange={handleChange}/>
        </div>
        <div className='form-group'>
          <label htmlFor="lastName">Last Name</label>
          <input type="text" id='lastName' name='lastName' value={form.lastName} onChange={handleChange}/>
        </div>
        <div className='form-group'>
          <label htmlFor="email">Email</label>
          <input type="email" id='email' name='email' value={form.email} onChange={handleChange}/>
        </div>
        <div className='form-group'>
          <label htmlFor="mobileNumber">Mobile Number</label>
          <input type="text" id='mobileNumber' name='mobileNumber' value={form.mobileNumber} onChange={handleChange}/>
        </div>
        <div className='form-group'>
          <label htmlFor="password">Password</label>
          <input type="password" id='password' name='password' value={form.password} onChange={handleChange}/>
        </div>
        <div className='form-group'>
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input type="password" id='changePassword' name='confirmPassword' value={form.confirmPassword} onChange={handleChange}/>
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Submitting...' : 'Submit'}
        </button>
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