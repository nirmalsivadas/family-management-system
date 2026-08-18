import React from "react";
import {Link} from "react-router-dom";

function Navbar(){
  return(
    <div>
      <input type="text" placeholder="Search families, members..." />
      <Notification />
      <ProfileIcon />
    </div>
  )
}