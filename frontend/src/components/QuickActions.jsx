import React from 'react';
import {Link} from 'react-router-dom';
import './QuickActions.css';

function QuickActions(){
  return (
    <div className="quick-actions">
      <h2>Quick Actions</h2>
      <div className="quick-actions-grid">
        <Link to="/register-family"><span>+</span>Register Family</Link>
        <Link to="/view-families"><span>^</span>View Families</Link>
        <Link to="/view-members"><span>o</span>View Members</Link>
        <Link to="/profile"><span>Edit</span>Update Profile</Link>
      </div>
    </div>
  )
}

export default QuickActions;
