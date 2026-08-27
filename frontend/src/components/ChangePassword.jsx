import React,{useState,useEffect} from "react";
import {Link,Navigate} from 'react-router-dom';

function ChangePassword(){
  return(
    <div>
      <form>
        <label htmlFor="newPassword">New Password: </label>
        <input type="text" id="newPassword" name="newPassword"></input>
        <label htmlFor="confirmNewPassword">Confirm New Password: </label>
        <input type="text" id="confirmNewPassword" name="confirmNewPassword"></input>
        <button>Update Password</button>
      </form>
    </div>
  );
}

export default ChangePassword;

