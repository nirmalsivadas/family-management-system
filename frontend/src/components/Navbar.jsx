import React from "react";
import {Link} from "react-router-dom";
import './Navbar.css';
import Notification from "./Notification";
import ProfileIcon from "./ProfileIcon";
import RecentNotification from "./RecentNotification";

function Navbar(){
  return(
    <div className="navbar">
      <input type="text" placeholder="Search families, members..." className="search-input" />
      <button><Link to={RecentNotification}>🔔</Link></button>
      <button><Link to={ProfileIcon}></Link>👤</button>
    </div>
  )
}

export default Navbar;