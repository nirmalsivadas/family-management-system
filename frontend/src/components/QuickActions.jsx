import React from 'react';
import {Link} from 'react-router-dom';
import './QuickActions.css';

function QuickActions(){
  return (
    <div className="quick-actions">
      <div>
        <button>Register Family <Link to="/family-info"></Link></button>
        <button>View Families <Link to="/view-families"></Link></button>
      </div>
      <div>
        <button>View Members <Link to="/view-members"></Link></button>
        <button>Update Profile <Link to="/update-profile"></Link></button>
      </div>
    </div>
  )
}

export default QuickActions;