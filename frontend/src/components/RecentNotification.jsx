import React,{useState,useEffect} from "react";
import {Link,Navigate} from 'react-router-dom';
import api from '../api/axios';
function RecentNotification(){
  const [recentNotifications,setRecentNotifications] = useState([]);
  const [loading,setLoading] = useState(true);

  useEffect(()=>{
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;

    if(!userId){
      setLoading(false);
    }
    api.get('/notifications/top-5?userId='+userId)
    .then((response)=>{
      setRecentNotifications(response.data.data);
      setLoading(false);
    }).catch((err)=>{
      console.error('Error fetching recent notifications:', err);
      setLoading(false);
    }).finally(()=>{
      setLoading(false);
    })
  },[])

  if(loading){
    return <div className="recent-notifications-container">Loading notifications...</div>
  }
  return(
    <div className="recent-notifications-container">
      <div>
        <h3>Notifications</h3>
        <p><Link to='/notifications'>View All</Link></p>
      </div>
    </div>
  );
}

export default RecentNotification;

