import React, { useState } from "react";

function Address() {
  return(
    <div>
      <h1>Address Information</h1>
      <form>
        <label htmlFor="addressLine1">Address Line 1:</label>
        <input type="text" id="addressLine1" name="addressLine1" />
        <label htmlFor="addressLine2">Address Line 2:</label>
        <input type="text" id="addressLine2" name="addressLine2" />
        <label htmlFor="country">Country:</label>
        <input type="text" id="country" name="country" />
        <label htmlFor="city">City:</label>
        <input type="text" id="city" name="city" />
        <label htmlFor="state">State:</label>
        <input type="text" id="state" name="state" />
        <label htmlFor="pinCode">Pin Code:</label>
        <input type="text" id="pinCode" name="pinCode" />
        <button>Cancel</button>
        <button>Continue</button>
      </form>
    </div>
  )
}

export default Address;