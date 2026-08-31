import React,{useEffect,useMemo,useState} from 'react';
import {useSearchParams} from 'react-router-dom';
import api from '../api/axios';
import './Settings.css';
import { getStoredUser, saveStoredUser } from '../utils/profile';
import { getNotificationPreferences, saveNotificationPreferences } from '../utils/notificationPreferences';

function fileToBase64(file){
  return new Promise((resolve,reject)=>{
    const reader = new FileReader();
    reader.onload = () => resolve(String(reader.result).split(',')[1] || '');
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
}

function Settings(){
  const [searchParams,setSearchParams] = useSearchParams();
  const [activeTab,setActiveTab] = useState(() => searchParams.get('tab') || 'account');
  const [message,setMessage] = useState('');
  const [error,setError] = useState('');
  const [saving,setSaving] = useState(false);
  const [photoFile,setPhotoFile] = useState(null);
  const [photoPreview,setPhotoPreview] = useState('');
  const [profile,setProfile] = useState(()=>{
    const user = getStoredUser();
    return {
      userId: user.id || '',
      firstName: user.firstName || '',
      lastName: user.lastName || '',
      email: user.email || '',
      mobileNumber: user.mobileNumber || '',
      photo: user.photo || ''
    };
  });
  const [passwordForm,setPasswordForm] = useState({
    newPassword: '',
    confirmNewPassword: ''
  });
  const [preferences,setPreferences] = useState(getNotificationPreferences);
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
          mobileNumber: freshProfile.mobileNumber || '',
          photo: freshProfile.photo || ''
        });
        saveStoredUser({
          id: freshProfile.id,
          firstName: freshProfile.firstName,
          lastName: freshProfile.lastName,
          email: freshProfile.email,
          mobileNumber: freshProfile.mobileNumber,
          photo: freshProfile.photo
        });
      })
      .catch((err)=>console.error('Error fetching settings profile:', err));
  },[]);

  useEffect(()=>{
    if(!photoFile){
      setPhotoPreview('');
      return undefined;
    }

    const previewUrl = URL.createObjectURL(photoFile);
    setPhotoPreview(previewUrl);
    return () => URL.revokeObjectURL(previewUrl);
  },[photoFile]);

  function handleProfileChange(e){
    setProfile({
      ...profile,
      [e.target.name]: e.target.value
    });
  }

  function profilePhotoSrc(){
    if(photoPreview){
      return photoPreview;
    }
    if(profile.photo){
      return `data:image/jpeg;base64,${profile.photo}`;
    }
    return '';
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
      const payload = {
        userId: profile.userId,
        firstName: profile.firstName,
        lastName: profile.lastName,
        mobileNumber: profile.mobileNumber
      };
      const body = new FormData();
      body.append('request', new Blob([JSON.stringify(payload)], {type: 'application/json'}));
      if(photoFile){
        body.append('photo', photoFile);
      }
      await api.patch('/users/update-profile', body);
      const savedProfile = {
        id: profile.userId,
        firstName: profile.firstName,
        lastName: profile.lastName,
        email: profile.email,
        mobileNumber: profile.mobileNumber,
        photo: photoPreview ? await fileToBase64(photoFile) : profile.photo
      };
      saveStoredUser(savedProfile);
      setProfile((current)=>({...current, photo: savedProfile.photo}));
      setPhotoFile(null);
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
      const response = await api.patch(`/users/change-password?userEmail=${encodeURIComponent(profile.email)}`,{
        userId: profile.userId,
        newPassword: passwordForm.newPassword,
        confirmNewPassword: passwordForm.confirmNewPassword
      });
      setPasswordForm({newPassword: '', confirmNewPassword: ''});
      setMessage(response.data?.data || 'Password updated successfully. Confirmation email sent.');
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
            {profilePhotoSrc() ? (
              <img className="profile-avatar image" src={profilePhotoSrc()} alt="Profile" />
            ) : (
              <div className="profile-avatar">{initials}</div>
            )}
            <div>
              <h2>{`${profile.firstName} ${profile.lastName}`.trim() || 'User'}</h2>
              <p>{profile.email}</p>
              <label className="profile-photo-upload">
                Change photo
                <input type="file" accept="image/*" onChange={(event)=>setPhotoFile(event.target.files?.[0] || null)} />
              </label>
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
              <strong>Normal notifications</strong>
              <small>Show general membership and account activity notifications.</small>
            </span>
            <input
              type="checkbox"
              checked={preferences.normal}
              onChange={(event)=>{
                const next = {...preferences, normal: event.target.checked};
                setPreferences(next);
                saveNotificationPreferences(next);
              }}
            />
          </div>
          <div className="preference-row">
            <span>
              <strong>Status change notifications</strong>
              <small>Show notifications when family registrations change status.</small>
            </span>
            <input
              type="checkbox"
              checked={preferences.status}
              onChange={(event)=>{
                const next = {...preferences, status: event.target.checked};
                setPreferences(next);
                saveNotificationPreferences(next);
              }}
            />
          </div>
        </section>
      )}
    </div>
  );
}

export default Settings;
