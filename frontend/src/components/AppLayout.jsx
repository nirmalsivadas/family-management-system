import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';
import Sidebar from './Sidebar';
import './Dashboard.css';

function AppLayout(){
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
