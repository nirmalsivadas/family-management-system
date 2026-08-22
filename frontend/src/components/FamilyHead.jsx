import React from "react";

function FamilyHead(){
  return(
    <div>
      <h1>Family Head Information</h1>
      <form>
        <label htmlFor="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" />
        <label htmlFor="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" />
        <label htmlFor="dateOfBirth">Date of Birth:</label>
        <input type="date" id="dateOfBirth" name="dateOfBirth" />
        <label htmlFor="gender">Gender:</label>
        <input type="text" id="gender" name="gender" />
        <label htmFor="maritalStatus">Marital Status:</label>
        <input type="text" id="maritalStatus" name="maritalStatus" />
        <label htmFor="bloodGroup">Blood Group:</label>
        <input type="text" id="bloodGroup" name="bloodGroup" />
        <label htmlFor="mobileNumber">Mobile Number:</label>
        <input type="number" id="mobileNumber" name="mobileNumber" />
        <label htmlFor="email">Email:</label>
        <input type="email" id="email" name="email" />
        <label htmlFor="occupation">Occupation:</label>
        <input type="text" id="occupation" name="occupation" />
        <label htmlFor="employmentType">Employment Type:</label>
        <input type="text" id="employmentType" name="employmentType" />
        <label htmlFor="organization">Organization:</label>
        <input type="text" id="organization" name="organization" />
        <label htmlFor="memberShipType">Designation:</label>
        <input type="text" id="designation" name="designation" />
        <label htmlFor="annualIncome">Annual Income:</label>
        <input type="number" id="annualIncome" name="annualIncome" />
        <button>Cancel</button>
        <button>Continue</button>
      </form>
    </div>
  );
}

export default FamilyHead;