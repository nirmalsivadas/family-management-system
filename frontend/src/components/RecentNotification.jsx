import React,{useEffect,useState} from "react";
import {Link} from 'react-router-dom';
import api from '../api/axios';
import './QuickActions.css';
import { filterNotificationsByPreferences, getNotificationPreferences } from '../utils/notificationPreferences';
import { normalizePageResponse } from '../utils/pageResponse';

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
  const [preferences,setPreferences] = useState(getNotificationPreferences);
  const [loading,setLoading] = useState(true);
  const visibleRecentNotifications = filterNotificationsByPreferences(recentNotifications, preferences);

  useEffect(()=>{
    function syncPreferences(event){
      setPreferences(event.detail || getNotificationPreferences());
    }

    window.addEventListener('notification-preferences-updated', syncPreferences);
    window.addEventListener('storage', syncPreferences);
    return () => {
      window.removeEventListener('notification-preferences-updated', syncPreferences);
      window.removeEventListener('storage', syncPreferences);
    };
  },[]);

  useEffect(()=>{
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;

    if(!userId){
      setRecentNotifications([]);
      setLoading(false);
      return;
    }

    let active = true;

    function loadRecentNotifications(){
    api.get(`/notification?userId=${userId}&page=0&size=20`)
    .then((response)=>{
        if(active){
          setRecentNotifications(normalizePageResponse(response.data.data).content);
          setLoading(false);
        }
    }).catch((err)=>{
      console.error('Error fetching recent notifications:', err);
        if(active){
          setLoading(false);
        }
    });
    }

    loadRecentNotifications();
    const intervalId = window.setInterval(loadRecentNotifications, 15000);

    return () => {
      active = false;
      window.clearInterval(intervalId);
    };
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
      {visibleRecentNotifications.slice(0, 5).map((notification)=>{
        const tone = getActivityTone(notification);
        return (
          <div className="activity-item" key={notification.notificationId}>
            <span className={`activity-dot ${tone.className}`}>{tone.icon}</span>
            <p>{notification.message}</p>
          </div>
        );
      })}
      {visibleRecentNotifications.length === 0 && <p className="empty-state">No recent notifications.</p>}
    </div>
  );
}

export default RecentNotification;
