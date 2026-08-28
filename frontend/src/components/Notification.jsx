import React,{useEffect,useMemo,useState} from "react";
import api from '../api/axios';
import './Notification.css';

function formatTitle(title){
  if(!title){
    return 'Notification';
  }

  return title
    .replaceAll('-', ' ')
    .replace(/\b\w/g, (char)=>char.toUpperCase());
}

function formatTime(timeStamp){
  if(!timeStamp){
    return '';
  }

  const date = new Date(timeStamp);
  if(Number.isNaN(date.getTime())){
    return timeStamp;
  }

  const diffMs = Date.now() - date.getTime();
  const diffMinutes = Math.max(1, Math.floor(diffMs / 60000));

  if(diffMinutes < 60){
    return `${diffMinutes} min ago`;
  }

  const diffHours = Math.floor(diffMinutes / 60);
  if(diffHours < 24){
    return `${diffHours} hour${diffHours === 1 ? '' : 's'} ago`;
  }

  const diffDays = Math.floor(diffHours / 24);
  return `${diffDays} day${diffDays === 1 ? '' : 's'} ago`;
}

function getNotificationTone(notification){
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

function Notification() {
  const [notifications,setNotifications] = useState([]);
  const [loading,setLoading] = useState(true);
  const [error,setError] = useState('');
  const [marking,setMarking] = useState(false);
  const user = JSON.parse(localStorage.getItem('user'));
  const userId = user?.id;

  const unreadCount = useMemo(
    ()=>notifications.filter((notification)=>!notification.markAsRead).length,
    [notifications]
  );

  useEffect(()=>{
    if(!userId){
      setLoading(false);
      return;
    }

    api.get('/notification?userId=' + userId).then((response)=>{
      setNotifications(response.data.data.content ?? response.data.data);
      setLoading(false);
    }).catch((err)=>{
      console.error("Error fetching notifications:", err);
      setError('Unable to load notifications.');
      setLoading(false);
    });
  },[userId]);

  async function markAsRead(notificationId){
    if(!userId || !notificationId){
      return;
    }

    setNotifications((current)=>
      current.map((notification)=>
        notification.notificationId === notificationId
          ? {...notification, markAsRead: true}
          : notification
      )
    );

    try{
      await api.get(`/notification/mark-as-read/${notificationId}?userId=${userId}`);
    }catch(err){
      console.error('Error marking notification as read:', err);
      setError('Unable to mark notification as read.');
    }
  }

  async function markAllAsRead(){
    const unreadNotifications = notifications.filter((notification)=>!notification.markAsRead);
    if(unreadNotifications.length === 0){
      return;
    }

    setMarking(true);
    setNotifications((current)=>
      current.map((notification)=>({...notification, markAsRead: true}))
    );

    try{
      await api.post(`/notification/mark-all-as-read?userId=${userId}`);
    }catch(err){
      console.error('Error marking all notifications as read:', err);
      setError('Unable to mark all notifications as read.');
    }finally{
      setMarking(false);
    }
  }

  if(loading){
    return <div className="notifications-container">Notifications loading...</div>
  }

  return (
    <div className="notifications-container">
      <div className="notifications-heading">
        <div>
          <h1>Notifications</h1>
          <p>{unreadCount} unread notification{unreadCount === 1 ? '' : 's'}</p>
        </div>
        <button type="button" onClick={markAllAsRead} disabled={marking || unreadCount === 0}>
          {marking ? 'Marking...' : 'Mark all as read'}
        </button>
      </div>

      {error && <div className="notifications-error">{error}</div>}

      <div className="notifications-list">
        {notifications.map((notification) => {
          const tone = getNotificationTone(notification);
          return (
            <article
              key={notification.notificationId}
              className={`notification-card ${notification.markAsRead ? 'read' : 'unread'}`}
            >
              <div className={`notification-icon ${tone.className}`}>{tone.icon}</div>
              <div className="notification-body">
                <h2>{formatTitle(notification.title)}</h2>
                <p>{notification.message}</p>
                <time>{formatTime(notification.timeStamp)}</time>
              </div>
              {!notification.markAsRead && (
                <button
                  type="button"
                  className="mark-read-button"
                  onClick={()=>markAsRead(notification.notificationId)}
                >
                  <span></span>
                  Mark read
                </button>
              )}
            </article>
          );
        })}
        {notifications.length === 0 && (
          <div className="notifications-empty">
            <h2>No notifications yet</h2>
            <p>Membership activity and account updates will appear here.</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default Notification;
