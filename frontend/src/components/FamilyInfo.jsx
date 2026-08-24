import React from "react";
import {Link} from "react-router-dom";

function FamilyInfo(){
  return(
    <div>
      <h1>Family Information</h1>
      <form>
        <label htmlFor="familyName">Family Name:</label>
        <input type="text" id="familyName" name="familyName" />
        <label htmlFor="numberOfMembers">Number of Members:</label>
        <input type="number" id="numberOfMembers" name="numberOfMembers" />
        <label htmlFor="memberShipType">Membership Type:</label>
        <select id="memberShipType">
          <option>Standard</option>
          <option>Premium</option>
          <option>Senior Citizen</option>
        </select>
        <label htmlFor="registrationCategory">Registration Category:</label>
        <select id="registrationCategory">
          <option>General</option>
          <option>OBC</option>
          <option>SC/ST</option>
          <option>EWS</option>
        </select>
        <button type="button">Cancel</button>
        <button type="submit"><Link to="/family-head-info">Continue</Link></button>
      </form>
    </div>
  );
}

export default FamilyInfo;