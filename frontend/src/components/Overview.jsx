import React,{useState,useEffect} from "react";
import {Link} from "react-router-dom";
import './Overview.css';
import api from '../api/axios';

function Overview(){
  const [totalFamilies,setTotalFamilies] = useState(null);
  const [totalMembers,setTotalMembers] = useState(null);
  const [pending,setPending] = useState(null);
  const [confirmed,setConfirmed] = useState(null);
  const [loading,setLoading] = useState(true);

  useEffect(()=>{
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;
    const userName = user?.userName;

    if(!userId){
      setLoading(false);
    }
    Promise.all([
      api.get('/users/'+userId+'/total-families'),
      api.get('/users/'+userId+'/total-members'),
      api.get('/users/'+userId+'/PENDING'),
      api.get('/users/'+userId+'/CONFIRMED')
    ])
    .then(([familyResponse,memberResponse,pendingResponse,confirmedResponse])=>{
      setTotalFamilies(response.data.data);
      setTotalMembers(response.data.data);
      setPending(response.data.data);
      setConfirmed(response.data.data);
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
      <h1>Hello, {userName}!</h1>
      <p>Manage your family memberships and member information.</p>
      <div className="overview-card">Total Families
        <p>{familyResponse ?? 0}</p>
      </div>
      <div className="overview-card">Total Members
        <p>{memberResponse ?? 0}</p>
      </div>
      <div className="overview-card">Total Pending Status
        <p>{pendingResponse ?? 0}</p>
      </div>
      <div className="overview-card">Total Confirmed Status
        <p>{confirmedResponse ?? 0}</p>
      </div>
    </div>
  )
}

export default Overview;