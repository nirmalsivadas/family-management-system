import {useEffect,useMemo,useState} from "react";
import {Link,useLocation,useNavigate} from "react-router-dom";
import api from '../api/axios';
import './Navbar.css';
import { getStoredUser, profileInitials, profileName, profilePhotoSrc } from '../utils/profile';
import { filterNotificationsByPreferences, getNotificationPreferences } from '../utils/notificationPreferences';
import { normalizePageResponse } from '../utils/pageResponse';
import { logoutSession } from '../utils/authSession';

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

  const diffMinutes = Math.max(1, Math.floor((Date.now() - date.getTime()) / 60000));
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
    return 'rejected';
  }
  if(text.includes('pending') || text.includes('review')){
    return 'pending';
  }
  if(text.includes('confirm') || text.includes('registered')){
    return 'confirmed';
  }
  return 'info';
}

function Navbar(){
  const navigate = useNavigate();
  const location = useLocation();
  const [profileOpen,setProfileOpen] = useState(false);
  const [notificationsOpen,setNotificationsOpen] = useState(false);
  const [topNotifications,setTopNotifications] = useState([]);
  const [notificationsLoading,setNotificationsLoading] = useState(false);
  const [loggingOut,setLoggingOut] = useState(false);
  const [preferences,setPreferences] = useState(getNotificationPreferences);
  const [search,setSearch] = useState('');
  const [user,setUser] = useState(getStoredUser);
  const userId = user?.id;
  const name = profileName(user);
  const email = user?.email || '';
  const initials = profileInitials(user);
  const photoSrc = profilePhotoSrc(user);
  const visibleTopNotifications = useMemo(
    ()=>filterNotificationsByPreferences(topNotifications, preferences),
    [topNotifications, preferences]
  );
  const unreadCount = useMemo(
    ()=>visibleTopNotifications.filter((notification)=>!notification.markAsRead).length,
    [visibleTopNotifications]
  );

  useEffect(()=>{
    if(!userId){
      return;
    }

    let active = true;

    function loadTopNotifications(){
    if(!userId){
      setTopNotifications([]);
      setNotificationsLoading(false);
      return;
    }

    setNotificationsLoading(true);
    api.get(`/notification?userId=${userId}&page=0&size=20`)
      .then((response)=>{
          if(active){
            setTopNotifications(normalizePageResponse(response.data?.data).content);
          }
      })
      .catch((err)=>{
        console.error('Error fetching top notifications:', err);
          if(active){
            setTopNotifications([]);
          }
      })
      .finally(()=>{
          if(active){
            setNotificationsLoading(false);
          }
      });
    }

    loadTopNotifications();
    const intervalId = window.setInterval(loadTopNotifications, 15000);

    return () => {
      active = false;
      window.clearInterval(intervalId);
    };
  },[userId]);

  useEffect(()=>{
    function clearSessionState(){
      setUser(null);
      setTopNotifications([]);
      setNotificationsOpen(false);
      setProfileOpen(false);
      setNotificationsLoading(false);
    }

    window.addEventListener('auth-session-cleared', clearSessionState);
    return () => {
      window.removeEventListener('auth-session-cleared', clearSessionState);
    };
  },[]);

  useEffect(()=>{
    function syncProfile(event){
      setUser(event.detail || getStoredUser());
    }

    window.addEventListener('user-profile-updated', syncProfile);
    window.addEventListener('storage', syncProfile);
    return () => {
      window.removeEventListener('user-profile-updated', syncProfile);
      window.removeEventListener('storage', syncProfile);
    };
  },[]);

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
    if(!notificationsOpen || !userId || unreadCount === 0){
      return;
    }

    const unreadVisibleNotifications = visibleTopNotifications.filter((notification)=>!notification.markAsRead);
    setTopNotifications((current)=>
      current.map((notification)=>
        unreadVisibleNotifications.some((visible)=>visible.notificationId === notification.notificationId)
          ? {...notification, markAsRead: true}
          : notification
      )
    );

    unreadVisibleNotifications.forEach((notification)=>{
      api.get(`/notification/mark-as-read/${notification.notificationId}?userId=${userId}`)
        .catch((err)=>{
          console.error('Error marking notification as read:', err);
        });
      });
  },[notificationsOpen,userId,unreadCount,visibleTopNotifications]);

  return(
    <header className="navbar">
      <form
        className="search-wrap"
        onSubmit={(event)=>{
          event.preventDefault();
          const value = search.trim();
          navigate(value ? `/view-families?query=${encodeURIComponent(value)}` : '/view-families');
        }}
      >
        <span className="search-icon" aria-hidden="true"></span>
        <input
          type="text"
          placeholder="Search families, members..."
          className="search-input"
          value={search}
          onChange={(event)=>setSearch(event.target.value)}
        />
        {search && (
          <button
            type="button"
            className="search-clear"
            aria-label="Clear search"
            onClick={()=>{
              setSearch('');
              if(location.pathname === '/view-families'){
                navigate('/view-families');
              }
            }}
          >
            Clear
          </button>
        )}
        <button type="submit" className="search-submit">Go</button>
      </form>
      <div className="navbar-actions">
        <button
          type="button"
          className="icon-button notification-trigger"
          aria-label="Recent notifications"
          onClick={()=>{
            setNotificationsOpen((open)=>!open);
            setProfileOpen(false);
          }}
        >
          <span>!</span>
          {unreadCount > 0 && <i></i>}
        </button>
        {notificationsOpen && (
          <div className="top-notifications-menu">
            <div className="top-notifications-header">
              <h2>Notifications</h2>
              <Link to="/notifications" onClick={()=>setNotificationsOpen(false)}>View all</Link>
            </div>
            <div className="top-notifications-list">
              {notificationsLoading && <p className="top-notifications-empty">Loading notifications...</p>}
              {!notificationsLoading && visibleTopNotifications.slice(0, 5).map((notification)=>(
                <Link
                  to="/notifications"
                  className="top-notification-item"
                  key={notification.notificationId}
                  onClick={()=>setNotificationsOpen(false)}
                >
                  <span className={`top-notification-dot ${getNotificationTone(notification)}`}></span>
                  <span>
                    <strong>{formatTitle(notification.title)}</strong>
                    <small>{notification.message}</small>
                    <time>{formatTime(notification.timeStamp)}</time>
                  </span>
                </Link>
              ))}
              {!notificationsLoading && visibleTopNotifications.length === 0 && (
                <p className="top-notifications-empty">No recent notifications.</p>
              )}
            </div>
          </div>
        )}
        <button
          type="button"
          className="profile-link"
          aria-label="Profile menu"
          onClick={()=>{
            setProfileOpen((open)=>!open);
            setNotificationsOpen(false);
          }}
        >
          {photoSrc ? (
            <img className="avatar avatar-image" src={photoSrc} alt="Profile" />
          ) : (
            <span className="avatar">{initials}</span>
          )}
          <span>
            <strong>{name}</strong>
            <small>Member</small>
          </span>
        </button>
        {profileOpen && (
          <div className="profile-menu">
            <strong>{name}</strong>
            <small>{email}</small>
            <Link to="/profile" onClick={()=>setProfileOpen(false)}>My Profile</Link>
            <Link to="/settings" onClick={()=>setProfileOpen(false)}>Settings</Link>
            <button
              type="button"
              disabled={loggingOut}
              onClick={async ()=>{
                setLoggingOut(true);
                try{
                  await logoutSession();
                }catch(err){
                  console.error('Logout failed:', err);
                }finally{
                  setLoggingOut(false);
                }
                navigate('/login');
              }}
            >
              {loggingOut ? 'Logging out...' : 'Logout'}
            </button>
          </div>
        )}
      </div>
    </header>
  )
}

export default Navbar;
