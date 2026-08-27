import React from 'react';
import Profile from './Profile.jsx';
import ChangePassword from './ChangePassword.jsx';
import Notification from './Notification.jsx';

function Settings(){
  return(
    <div>
      <Profile />
      <ChangePassword />
      <Notification />
    </div>
  );
}

export default Settings;