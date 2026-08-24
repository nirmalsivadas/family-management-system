import React from 'react'
import FamilyInfo from './FamilyInfo';
import FamilyHead from './FamilyHead';
import FamilyMembers from './FamilyMembers';
import Address from './Address';
import Review from './Review';
import RegistrationSuccess from './RegistrationSuccess';

function RegisterFamily() {
  return(
    <div>
      <FamilyInfo />
      <FamilyHead />
      <FamilyMembers />
      <Address />
      <Review />
      <RegistrationSuccess />
    </div>
  );
}
export default RegisterFamily;