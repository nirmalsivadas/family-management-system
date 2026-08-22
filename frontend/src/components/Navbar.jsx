import React from "react";
import {Link} from "react-router-dom";
import './Navbar.css';
import Notification from "./Notification";
import ProfileIcon from "./ProfileIcon";

function Navbar(){
  return(
    <div className="navbar">
      <input type="text" placeholder="Search families, members..." className="search-input" />
      <Notification className="notification" />
      <ProfileIcon className="profile-icon" />
    </div>
  )
}

export default Navbar;