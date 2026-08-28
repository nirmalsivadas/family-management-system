import React from "react";
import { NavLink } from "react-router-dom";

function Sidebar(){
  const user = JSON.parse(localStorage.getItem('user'));
  const name = `${user?.firstName || ''} ${user?.lastName || ''}`.trim() || user?.userName || 'User';
  const email = user?.email || '';
  const initials = name
    .split(' ')
    .map((part)=>part[0])
    .join('')
    .slice(0,2)
    .toUpperCase();

  return(
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="sidebar-logo">FM</div>
        <div>
          <strong>FamilyMgmt</strong>
          <p>Membership Portal</p>
        </div>
      </div>
      <nav className="sidebar-nav">
        <NavLink to="/dashboard"><span>[]</span>Dashboard</NavLink>
        <NavLink to="/register-family"><span>+</span>Register Family</NavLink>
        <NavLink to="/view-families"><span>^</span>Families</NavLink>
        <NavLink to="/view-members"><span>o</span>Members</NavLink>
        <NavLink to="/notifications"><span>!</span>Notifications</NavLink>
        <NavLink to="/settings"><span>*</span>Settings</NavLink>
      </nav>
      <NavLink className="sidebar-user" to="/profile">
        <span className="avatar">{initials}</span>
        <span>
          <strong>{name}</strong>
          <small>{email}</small>
        </span>
      </NavLink>
    </aside>
  );
}

export default Sidebar;
