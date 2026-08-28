import React from 'react';
import NavBar from './Navbar';  
import Overview from './Overview';
import RecentFamilies from './RecentFamilies';
import QuickActions from './QuickActions';
import RecentNotification from './RecentNotification';


function Dashboard(){
  return(
    <>
      <NavBar />
      <Overview />
      <RecentFamilies />
      <QuickActions />
      <RecentNotification />
    </>
  )
}

export default Dashboard;