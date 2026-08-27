import React,{useState,useEffect} from "react";
import {Link,Navigate} from 'react-router-dom';
import api from '../api/axios';
import './Notification.css';
function Notification() {
  const [notifications,setNotifications] = useState([]);
  const [loading,setLoading] = useState(true);

  useEffect(()=>{
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;
    if(userid){
      setLoading(false);
    }
    api.get('/notifications?userId=' + userId).then((response)=>{
      setNotifications(response.data.data);
      setLoading(false)
    }).catch((error)=>{
      console.error("Error fetching recent families:", err);
      setLoading(false);
    }).finally(()=>{
      setLoading(false);
    });
  },[])

  if(loading){
    <div className="notifications-container">Notifications loading...</div>
  }
  return (
  <div className="notifications-container">
    <div>
      <h1>Notifications</h1>
      <button>Mark all as read</button>
    </div>
    <div>
      {notifications.map((notification) => (
        <div key={notification.id} className="notification-card">
          <p>{notification.message}</p>
          <p>{notification.timeStamp}</p>
          <button>Mark as read</button>
        </div>
      ))}
    </div>
  </div>
);
}
export default Notification;