import React from "react";
import {Link} from "react-router-dom";
import './Overview.css';

function Overview(){
  return(
    <div className="overview-container">
      <h1>Overview</h1>
      <p>abcdef</p>
      <div>Total Families</div>
      <div>Total Members</div>
      <div>Pending</div>
      <div>Confirmed</div>
    </div>
  )
}

export default Overview;