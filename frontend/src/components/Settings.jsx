import React,{useEffect,useMemo,useState} from 'react';
import {useSearchParams} from 'react-router-dom';
import api from '../api/axios';
import './Settings.css';

function getStoredUser(){
  return JSON.parse(localStorage.getItem('user')) || {};
}

function Settings(){
  const [searchParams,setSearchParams] = useSearchParams();
  const [activeTab,setActiveTab] = useState(() => searchParams.get('tab') || 'account');
  const [message,setMessage] = useState('');
  const [error,setError] = useState('');
  const [saving,setSaving] = useState(false);
  const [profile,setProfile] = useState(()=>{
    const user = getStoredUser();
    return {
      userId: user.id || '',
      firstName: user.firstName || '',
      lastName: user.lastName || '',
      email: user.email || '',
      mobileNumber: user.mobileNumber || ''
    };
  });
  const [passwordForm,setPasswordForm] = useState({
    newPassword: '',
    confirmNewPassword: ''
  });
  const [preferences,setPreferences] = useState(()=>{
    try{
      return JSON.parse(localStorage.getItem('notificationPreferences')) || {
        email: true,
        status: true
      };
    }catch{
      return {email: true, status: true};
    }
  });
  const initials = useMemo(()=>{
    const source = `${profile.firstName} ${profile.lastName}`.trim() || profile.email || 'User';
    return source
      .split(' ')
      .filter(Boolean)
      .map((part)=>part[0])
      .join('')
      .slice(0,2)
      .toUpperCase();
  },[profile.firstName,profile.lastName,profile.email]);

  useEffect(()=>{
    setActiveTab(searchParams.get('tab') || 'account');
  },[searchParams]);

  useEffect(()=>{
    const user = getStoredUser();
    if(!user.id){
      return;
    }

    api.get(`/users/${user.id}`)
      .then((response)=>{
        const freshProfile = response.data?.data ?? response.data;
        setProfile({
          userId: freshProfile.id,
          firstName: freshProfile.firstName || '',
          lastName: freshProfile.lastName || '',
          email: freshProfile.email || '',
          mobileNumber: freshProfile.mobileNumber || ''
        });
        localStorage.setItem('user', JSON.stringify({
          id: freshProfile.id,
          firstName: freshProfile.firstName,
          lastName: freshProfile.lastName,
          email: freshProfile.email,
          mobileNumber: freshProfile.mobileNumber
        }));
      })
      .catch((err)=>console.error('Error fetching settings profile:', err));
  },[]);

  function handleProfileChange(e){
    setProfile({
      ...profile,
      [e.target.name]: e.target.value
    });
  }

  function handlePasswordChange(e){
    setPasswordForm({
      ...passwordForm,
      [e.target.name]: e.target.value
    });
  }

  async function handleProfileSubmit(e){
    e.preventDefault();
    setError('');
    setMessage('');
    setSaving(true);
    try{
      await api.patch('/users/update-profile',{
        userId: profile.userId,
        firstName: profile.firstName,
        lastName: profile.lastName,
        mobileNumber: profile.mobileNumber
      });
      localStorage.setItem('user', JSON.stringify({
        id: profile.userId,
        firstName: profile.firstName,
        lastName: profile.lastName,
        email: profile.email,
        mobileNumber: profile.mobileNumber
      }));
      setMessage('Profile updated successfully.');
    }catch(err){
      setError(err.response?.data?.message || 'Profile update failed.');
    }finally{
      setSaving(false);
    }
  }

  async function handlePasswordSubmit(e){
    e.preventDefault();
    setError('');
    setMessage('');
    if(passwordForm.newPassword !== passwordForm.confirmNewPassword){
      setError('Passwords do not match.');
      return;
    }
    setSaving(true);
    try{
      await api.patch(`/users/change-password?userEmail=${encodeURIComponent(profile.email)}`,{
        userId: profile.userId,
        newPassword: passwordForm.newPassword,
        confirmNewPassword: passwordForm.confirmNewPassword
      });
      setPasswordForm({newPassword: '', confirmNewPassword: ''});
      setMessage('Password updated successfully.');
    }catch(err){
      setError(err.response?.data?.message || 'Password update failed.');
    }finally{
      setSaving(false);
    }
  }

  return(
    <div className="settings-page">
      <div className="page-heading">
        <div>
          <h1>Settings</h1>
          <p>Manage your account preferences and security.</p>
        </div>
      </div>

      <div className="settings-tabs">
        <button className={activeTab === 'account' ? 'active' : ''} onClick={()=>{setActiveTab('account'); setSearchParams({});}}>Account</button>
        <button className={activeTab === 'security' ? 'active' : ''} onClick={()=>{setActiveTab('security'); setSearchParams({tab: 'security'});}}>Security</button>
        <button className={activeTab === 'notifications' ? 'active' : ''} onClick={()=>{setActiveTab('notifications'); setSearchParams({tab: 'notifications'});}}>Notifications</button>
      </div>

      {message && <div className="settings-message success">{message}</div>}
      {error && <div className="settings-message error">{error}</div>}

      {activeTab === 'account' && (
        <form className="settings-card" onSubmit={handleProfileSubmit}>
          <div className="settings-profile-header">
            <div className="profile-avatar">{initials}</div>
            <div>
              <h2>{`${profile.firstName} ${profile.lastName}`.trim() || 'User'}</h2>
              <p>{profile.email}</p>
              <button type="button" disabled>Photo upload coming in a later update</button>
            </div>
          </div>

          <div className="settings-form-grid">
            <label>
              First Name
              <input name="firstName" value={profile.firstName} onChange={handleProfileChange} />
            </label>
            <label>
              Last Name
              <input name="lastName" value={profile.lastName} onChange={handleProfileChange} />
            </label>
            <label className="full">
              Email Address
              <input name="email" value={profile.email} disabled />
            </label>
            <label className="full">
              Mobile Number
              <input name="mobileNumber" value={profile.mobileNumber} onChange={handleProfileChange} />
            </label>
          </div>

          <button className="settings-primary" type="submit" disabled={saving}>
            {saving ? 'Saving...' : 'Save changes'}
          </button>
        </form>
      )}

      {activeTab === 'security' && (
        <form className="settings-card narrow" onSubmit={handlePasswordSubmit}>
          <h2>Change Password</h2>
          <div className="settings-form-grid single">
            <label>
              New Password
              <input type="password" name="newPassword" value={passwordForm.newPassword} onChange={handlePasswordChange} />
            </label>
            <label>
              Confirm New Password
              <input type="password" name="confirmNewPassword" value={passwordForm.confirmNewPassword} onChange={handlePasswordChange} />
            </label>
          </div>
          <button className="settings-primary" type="submit" disabled={saving}>
            {saving ? 'Updating...' : 'Update password'}
          </button>
        </form>
      )}

      {activeTab === 'notifications' && (
        <section className="settings-card narrow">
          <h2>Notification Preferences</h2>
          <div className="preference-row">
            <span>
              <strong>Email notifications</strong>
              <small>Receive membership updates by email.</small>
            </span>
            <input
              type="checkbox"
              checked={preferences.email}
              onChange={(event)=>{
                const next = {...preferences, email: event.target.checked};
                setPreferences(next);
                localStorage.setItem('notificationPreferences', JSON.stringify(next));
              }}
            />
          </div>
          <div className="preference-row">
            <span>
              <strong>Status updates</strong>
              <small>Notify me when family registrations change status.</small>
            </span>
            <input
              type="checkbox"
              checked={preferences.status}
              onChange={(event)=>{
                const next = {...preferences, status: event.target.checked};
                setPreferences(next);
                localStorage.setItem('notificationPreferences', JSON.stringify(next));
              }}
            />
          </div>
        </section>
      )}
    </div>
  );
}

export default Settings;
