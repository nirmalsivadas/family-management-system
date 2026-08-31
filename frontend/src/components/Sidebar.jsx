import React,{useEffect,useState} from "react";
import { NavLink } from "react-router-dom";
import { getStoredUser, profileInitials, profileName, profilePhotoSrc } from "../utils/profile";

function Sidebar(){
  const [user,setUser] = useState(getStoredUser);
  const name = profileName(user);
  const email = user?.email || '';
  const initials = profileInitials(user);
  const photoSrc = profilePhotoSrc(user);

  useEffect(()=>{
    function syncProfile(event){
      setUser(event.detail || getStoredUser());
    }

    window.addEventListener('user-profile-updated', syncProfile);
    window.addEventListener('storage', syncProfile);
    return () => {
      window.removeEventListener('user-profile-updated', syncProfile);
      window.removeEventListener('storage', syncProfile);
    };
  },[]);

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
        {photoSrc ? (
          <img className="avatar avatar-image" src={photoSrc} alt="Profile" />
        ) : (
          <span className="avatar">{initials}</span>
        )}
        <span>
          <strong>{name}</strong>
          <small>{email}</small>
        </span>
      </NavLink>
    </aside>
  );
}

export default Sidebar;
