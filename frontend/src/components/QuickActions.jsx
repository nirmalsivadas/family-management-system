import React from 'react';
import {Link} from 'react-router-dom';

function QuickActions(){
  return (
    <div>
      <button>Register Family <Link to="/register-family"></Link></button>
      <button>View Families <Link to="/view-families"></Link></button>
      <button>View Members <Link to="/view-members"></Link></button>
      <button>Update Profile <Link to="/update-profile"></Link></button>
    </div>
  )
}

export default QuickActions;