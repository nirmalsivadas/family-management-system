import React,{useState,useEffect} from 'react';
import {Link} from 'react-router-dom';
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
      return;
    }
    api.get('/family/view-members?userId=' + userId).then((response)=>{
      setFamilyMembers(response.data.data.content ?? response.data.data)
      setLoading(false);
    }).catch((err)=>{
      console.error("Error fetching families:", err);
        setLoading(false);
    }).finally(()=>{
      setLoading(false);
    })
  },[])

  if(loading){
    return <div className='view-members-container'>Loading members....</div>
  }
  return(
    <div className='view-members-container'>
      <div className="page-heading">
        <div>
          <h1>Members</h1>
          <p>{familyMembers.length} members</p>
        </div>
      </div>
      <div className='data-panel members-list'>
        <div className="members-row members-header">
          <span>Name</span>
          <span>Relationship</span>
          <span>Family</span>
          <span>Membership #</span>
          <span>Occupation</span>
          <span>Mobile</span>
          <span>Status</span>
          <span>Action</span>
        </div>
        {familyMembers.map((member) => (
          <div key={`${member.memberShipId}-${member.name}`} className='members-row'>
            <strong>{member.name}</strong>
            <span>{member.relationShip}</span>
            <span>{member.familyName}</span>
            <span>{member.memberShipId}</span>
            <span>{member.occupation}</span>
            <span>{member.mobileNumber}</span>
            <span className={`status-pill ${member.status?.toLowerCase()}`}>{member.status}</span>
            <Link to={`/view-family/${member.memberShipId}`}>View</Link>
          </div>
        ))}
        {familyMembers.length === 0 && <p className="empty-state">No members found.</p>}
      </div>
    </div>
  )
}

export default ViewMembers;
