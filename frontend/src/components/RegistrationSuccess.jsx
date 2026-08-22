import React from "react";
import {Link} from "react-router-dom";

function RegistrationSuccess() {
  return (
    <div>
      <h1>Registration Successful!</h1>
      <div>
        <h6>Membership Number</h6>
        <p>123456789</p>
      </div>
            <div>
        <h6>Family Name</h6>
        <p>Sharma Family</p>
      </div>
            <div>
        <h6>Registration Date</h6>
        <p>123456789</p>
      </div>
      <div>
        <h6>Members</h6>
        <p>123456789</p>
      </div>
      <div>
        <h6>Status</h6>
        <p>Pending</p>
      </div>
      <div>
        <button>View Family Profile</button>
        <button><Link to="/dashboard">Go to Dashboard</Link></button>
      </div>
    </div>
  );
}

export default RegistrationSuccess;