import React,{useState,useEffect} from 'react';
import {Link,Navigate} from 'react-router-dom';
import './ViewMembers.css';
import api from '../api/axios';

function ViewMembers(){
  const[familyMembers,setFamilyMembers] = useState([]);
  const[loading,setLoading] = useState(true);

  useEffect(()=>{
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;

    if(!userId){
      setLoading(false);
    }
    api.get('/family/view-families?userId=' + userId).then((response)=>{
      setFamilyMembers(response.data.data)
      setLoading(false);
    }).catch((err)=>{
      console.error("Error fetching families:", err);
        setLoading(false);
    }).finally(()=>{
      setLoading(false);
    })
  },[])

  if(loading){
    <div className='view-members-container'>Loading members....</div>
  }
  return(
    <div className='view-members-container'>
      <h2>Family Members</h2>
      <div className='view-members-container'>
        {familyMembers.map((member) => (
          <div key={member.id} className='member-card'>
            <h3>{member.name}</h3>
            <h4>RelationShip: {member.relationSipWithFamilyHead}</h4>
            <p>Family Name: {member.familyName}</p>
            <p>Membership Id: {member.memberShipId}</p>
            <p>Occupation: {member.occupation}</p>
            <p>Mobile: {member.mobileNumber}</p>
            <p>Status: {member.status}</p>
            <p>Action: <Link to='view-family'>View</Link></p>
          </div>
        ))}
      </div>
    </div>
  )
}

export default ViewMembers;