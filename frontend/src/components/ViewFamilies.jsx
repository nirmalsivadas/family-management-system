import React, {useState,useEffect} from 'react';
import {Link} from 'react-router-dom';
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
      return;
    }
    api.get('/family/view-families?userId=' + userId)
    .then((response)=>{
      setFamilies(response.data.data.content ?? response.data.data);
      setLoading(false);
    }).catch((err)=>{
      console.error("Error fetching families:", err);
      setLoading(false);
    }).finally(()=>{
      setLoading(false);
    })
  },[])

  if(loading){
    return <div className='view-families-container'>Loading families...</div>
  }

  return(
    <div className='view-families-container'>
      <div className="page-heading">
        <div>
          <h1>Families</h1>
          <p>{families.length} total registrations</p>
        </div>
        <Link className="primary-action" to="/register-family">+ Register New Family</Link>
      </div>
      <div className="data-panel families-list">
        <div className="families-row families-header">
          <span>Membership #</span>
          <span>Family Head</span>
          <span>Family Name</span>
          <span>Members</span>
          <span>Status</span>
          <span>Actions</span>
        </div>
        {families.map((family)=>(
          <div key={family.membershipId} className='families-row'>
            <strong>{family.membershipId}</strong>
            <span>{family.familyHead}</span>
            <span>{family.familyName}</span>
            <span>{family.numberOfFamilyMembers}</span>
            <span className={`status-pill ${family.status?.toLowerCase()}`}>{family.status}</span>
            <span className="row-actions">
              <Link to={`/view-family/${family.membershipId}`}>View</Link>
              <Link to='/update-family'>Edit</Link>
            </span>
          </div>
        ))}
        {families.length === 0 && <p className="empty-state">No families found.</p>}
      </div>
    </div>
  );
}

export default ViewFamilies;
