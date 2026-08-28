import React,{useState,useEffect} from "react";
import './Overview.css';
import api from '../api/axios';

function Overview(){
  const [totalFamilies,setTotalFamilies] = useState(null);
  const [totalMembers,setTotalMembers] = useState(null);
  const [pending,setPending] = useState(null);
  const [confirmed,setConfirmed] = useState(null);
  const [loading,setLoading] = useState(true);
  const [userName,setUserName] = useState('User');

  useEffect(()=>{
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;
    setUserName(user?.firstName || user?.userName || 'User');

    if(!userId){
      setLoading(false);
      return;
    }

    Promise.all([
      api.get('/users/'+userId+'/total-families'),
      api.get('/users/'+userId+'/total-members'),
      api.get('/users/'+userId+'/PENDING'),
      api.get('/users/'+userId+'/CONFIRMED')
    ])
    .then(([familyResponse,memberResponse,pendingResponse,confirmedResponse])=>{
      setTotalFamilies(familyResponse.data.data);
      setTotalMembers(memberResponse.data.data);
      setPending(pendingResponse.data.data);
      setConfirmed(confirmedResponse.data.data);
      setLoading(false);
    }).catch((err)=>{
      console.error('Error fetching overview stats:', err)
      setLoading(false);
    }).finally(()=>{
      setLoading(false);
    })

  },[])

  if(loading){
    return <div className="overview-container">Loading overview...</div>
  }

  return(
    <div className="overview-container">
      <h1>Good afternoon, {userName} 👋</h1>
      <p>Manage your family memberships and member information.</p>
      <div className="overview-stats">
      <div className="overview-card">
        <span>Total Families</span>
        <strong>{totalFamilies ?? 0}</strong>
        <i>⌂</i>
      </div>
      <div className="overview-card">
        <span>Total Members</span>
        <strong>{totalMembers ?? 0}</strong>
        <i>◎</i>
      </div>
      <div className="overview-card">
        <span>Pending</span>
        <strong>{pending ?? 0}</strong>
        <i>⌛</i>
      </div>
      <div className="overview-card">
        <span>Confirmed</span>
        <strong>{confirmed ?? 0}</strong>
        <i>✓</i>
      </div>
      </div>
    </div>
  )
}

export default Overview;
