import React,{useState,useEffect} from "react";
import {Link,Navigate} from 'react-router-dom';
import api from '../api/axios';

function ViewFamily(){
  const [family, setFamily] = useState([]);
  const [loading,setLoading] = useState(true);

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;
    if(!userId){
      setLoading(false)
    }
    api.get('/family/view-family?userId=' + userId)
    .then((response)=>{
      setFamily(response.data.data);
      setLoading(false);
    }).catch((err)=>{
      console.error("Error fetching family:", err);
      setLoading(false);
    }).finally(()=>{
      setLoading(false);
    });
  },[]);

  if(loading){
    return <div className='view-family-container'>Loading family...</div>;
  }
  return(
    <div className="view-family-container">
      <h2>Family Overview</h2>
      {family.map((family) => (
        <div key={family.id} className="family-card">
          <h3>{family.familyName}</h3>
          <p>Membership Id: {family.memberShipId}</p>
          <p>Status: {family.status}</p>
          <p>Standard: {family.standard}</p>
          <div>
            <Link to={`/edit-family/${family.id}`}>
              <button>Edit</button>
            </Link>
            <button>Delete</button>
          </div>
          <div>
            <h3>Family Overview</h3>
            <h4>Membership: {family.memberShipId}</h4>
            <p>Members: {family.members.length}</p>
            <p>Registration Date: {family.registrationDate}</p>
            <p>MemberShip Type: {family.memberShipType}</p>
            <p>Status: {family.status}</p>
          </div>
          <div>
            <h3>Family Members</h3>
          </div>
          <div>
            <h3>Address</h3>
            <p>{family.address}</p>
          </div>
          <div>
            <h3>Family Head</h3>
            <img src={family.photo} alt="Family Head" />
            <p>Name: {family.familyHead.name}</p>
            <p>Gender: {family.familyHead.gender}</p>
            <p>Occupation: {family.familyHead.occupation}</p>
            <p>Mobile: {family.familyHead.mobile}</p>
            <p>Email: {family.email}</p>
            <p>Blood Group: {family.familyHead.bloodGroup}</p>
            <p>Employer: {family.familyHead.employer}</p>
            <p>Designation: {family.designation}</p>
            <p>Annual Income: {family.annualIncome}</p>
            </div> 
        </div> 
      ))} 
    </div> 
  ); 
}

export default ViewFamily;