import React,{useEffect,useState} from "react";
import {Link} from 'react-router-dom';
import api from '../api/axios';
import './QuickActions.css';

function getActivityTone(notification){
  const text = `${notification.title || ''} ${notification.message || ''}`.toLowerCase();
  if(text.includes('reject')){
    return {className: 'rejected', icon: 'x'};
  }
  if(text.includes('pending') || text.includes('review')){
    return {className: 'pending', icon: '!'};
  }
  if(text.includes('confirm') || text.includes('registered')){
    return {className: 'confirmed', icon: 'check'};
  }
  return {className: 'info', icon: 'i'};
}

function RecentNotification(){
  const [recentNotifications,setRecentNotifications] = useState([]);
  const [loading,setLoading] = useState(true);

  useEffect(()=>{
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;

    if(!userId){
      setLoading(false);
      return;
    }
    api.get('/notification/top-5?userId='+userId)
    .then((response)=>{
      setRecentNotifications(response.data.data ?? []);
      setLoading(false);
    }).catch((err)=>{
      console.error('Error fetching recent notifications:', err);
      setLoading(false);
    })
  },[])

  if(loading){
    return <div className="recent-notifications-container">Loading notifications...</div>
  }
  return(
    <div className="recent-notifications-container">
      <div className="section-heading">
        <h2>Recent Activity</h2>
        <Link to='/notifications'>View all -&gt;</Link>
      </div>
      {recentNotifications.map((notification)=>{
        const tone = getActivityTone(notification);
        return (
          <div className="activity-item" key={notification.notificationId}>
            <span className={`activity-dot ${tone.className}`}>{tone.icon}</span>
            <p>{notification.message}</p>
          </div>
        );
      })}
      {recentNotifications.length === 0 && <p className="empty-state">No recent notifications.</p>}
    </div>
  );
}

export default RecentNotification;
