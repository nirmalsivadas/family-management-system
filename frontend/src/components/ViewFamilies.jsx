import React, {useState,useEffect} from 'react';
import {Link,Navigate} from 'react-router-dom';
import './ViewFamilies.css';
import api from '../api/axios';

function ViewFamilies(){
  const [families, setFamilies] = useState([]);
  const [loading,setLoading] = useState(true);
  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;
    if(!userId){
      setLoading(false)
    }
    api.get('/family/view-families?userId=' + userId)
    .then((response)=>{
      setFamilies(response.data.data);
      setLoading(false);
    }).catch((err)=>{
      console.error("Error fetching families:", err);
      setLoading(false);
    }).finally(()=>{
      setLoading(false);
    })
  },[])

  if(loading){
    <div className='view-families-container'>Loading families...</div>
  }

  return(
    <div className='view-families-container'>
      {families.map((family)=>(
        <div key={family.memberShipId} className='family-card'>
          <h4>Family Head: {family.familyHead}</h4>
          <h4>Family Name: {family.familyName}</h4>
          <p>Members: {family.numberOfFamilyMembers}</p>
          <p>Registration Date: {family.registrationDate}</p>
          <span>Status: {family.status}</span>
          <div>Actions: 
            <Link to='/view-family'>View</Link>
            <Link to='/update-family'>Edit</Link>
            <Link to='/delete-family'>Delete</Link>
          </div>
        </div>
      )
      )};
    </div>
  );
}

export default ViewFamilies;