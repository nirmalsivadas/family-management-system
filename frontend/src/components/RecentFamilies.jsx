import React, {useState,useEffect} from "react";
import './RecentFamilies.css';
import api from '../api/axios';
import {Link} from 'react-router-dom';

function RecentFamilies(){
  const [families, setFamilies] = useState([]);
  const [loading,setLoading] = useState(true);
  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;
    if(!userId){
      setLoading(false)
      return;
    }
    api.get('/family/recent-families?userId=' + userId)
    .then((response)=>{
      setFamilies(response.data.data);
      setLoading(false);
    }).catch((err)=>{
      console.error("Error fetching recent families:", err);
      setLoading(false);
    }).finally(()=>{
      setLoading(false);
    });
  }, []);

  if(loading){
    return <div className='recent-families'>Loading recent families...</div>
  }

  return (
    <div className='recent-families'>
      {families.map((family)=>(
        <div key={family.membershipId} className='family-card'>
          <h4>{family.familyHead}</h4>
          <p>Members: {family.numberOfFamilyMembers}</p>
          <span>Status: {family.status}</span>
          <span>Action: <Link to='/view-families'>View</Link></span>
        </div>
      ))}
    </div>
  );
}

export default RecentFamilies;