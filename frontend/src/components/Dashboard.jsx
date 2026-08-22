import React from 'react';
import NavBar from './Navbar';  
import Overview from './Overview';
import RecentFamilies from './RecentFamilies';
import RecentActivity from './RecentActivity';
import QuickActions from './QuickActions';


function Dashboard(){
  return(
    <>
      <NavBar />
      <Overview />
      <RecentFamilies />
      <QuickActions />
      <RecentActivity />
    </>
  )
}

export default Dashboard;