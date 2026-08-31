import React,{useEffect} from 'react';
import { Outlet } from 'react-router-dom';
import api from '../api/axios';
import Navbar from './Navbar';
import Sidebar from './Sidebar';
import './Dashboard.css';
import { getStoredUser, saveStoredUser } from '../utils/profile';

function AppLayout(){
  useEffect(()=>{
    const storedUser = getStoredUser();
    if(!storedUser.id){
      return;
    }

    api.get(`/users/${storedUser.id}`)
      .then((response)=>{
        const freshProfile = response.data?.data ?? response.data;
        saveStoredUser({
          ...storedUser,
          ...freshProfile,
          password: undefined,
        });
      })
      .catch((err)=>{
        console.error('Error refreshing profile:', err);
      });
  },[]);

  return(
    <div className="app-shell">
      <Sidebar />
      <main className="main-area">
        <Navbar />
        <div className="dashboard-content">
          <Outlet />
        </div>
      </main>
    </div>
  );
}

export default AppLayout;
