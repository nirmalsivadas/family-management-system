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
      <div className="section-heading">
        <h2>Recent Families</h2>
        <Link to="/view-families">View all →</Link>
      </div>
      <div className="families-table">
        <div className="families-row families-header">
          <span>Membership #</span>
          <span>Family Head</span>
          <span>Members</span>
          <span>Status</span>
          <span>Action</span>
        </div>
        {families.map((family)=>(
          <div key={family.membershipId} className='families-row'>
            <strong>{family.membershipId}</strong>
            <span>{family.familyHead}</span>
            <span>{family.numberOfFamilyMembers}</span>
            <span className={`status-pill ${family.status?.toLowerCase()}`}>{family.status}</span>
            <Link to={`/view-family/${family.membershipId}`}>View</Link>
          </div>
        ))}
        {families.length === 0 && <p className="empty-state">No recent families found.</p>}
      </div>
    </div>
  );
}

export default RecentFamilies;
