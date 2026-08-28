import React from 'react';
import Overview from './Overview';
import RecentFamilies from './RecentFamilies';
import QuickActions from './QuickActions';
import RecentNotification from './RecentNotification';


function Dashboard(){
  return(
    <>
      <Overview />
      <div className="dashboard-grid">
        <RecentFamilies />
        <aside className="dashboard-side">
          <QuickActions />
          <RecentNotification />
        </aside>
      </div>
    </>
  );
}

export default Dashboard;
