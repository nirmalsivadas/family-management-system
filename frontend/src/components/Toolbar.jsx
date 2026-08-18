import React from "react";
import {Link} from "react-router-dom";
import ProfileIcon from "./ProfileIcon";

function Toolbar(){
  return(
    <div>
      <RegisterFamily />
      <ViewFamilies />
      <ViewMembers />
      <UpdateProfile />
      <Notifications />
      <Settings />
      <ProfileIcon />
    </div>
  )
}