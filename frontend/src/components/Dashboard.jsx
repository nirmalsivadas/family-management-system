import React from 'react';
import Overview from './Overview';
import RecentFamilies from './RecentFamilies';
import RecentActivity from './RecentActivity';
import QuickActions from './QuickActions';


function Dashboard(){
  return(
    <>
      <Overview />
      <RecentFamilies />
      <QuickActions />
      <RecentActivity />
    </>
  )
}

export default Dashboard;