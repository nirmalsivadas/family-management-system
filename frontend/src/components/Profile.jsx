import React,{useEffect,useMemo,useState} from "react";
import {Link} from "react-router-dom";
import api from "../api/axios";
import "./Profile.css";

function getStoredUser(){
  return JSON.parse(localStorage.getItem("user")) || {};
}

function initialsFor(firstName,lastName,email){
  const fullName = `${firstName || ""} ${lastName || ""}`.trim();
  const source = fullName || email || "User";
  return source
    .split(" ")
    .filter(Boolean)
    .map((part)=>part[0])
    .join("")
    .slice(0,2)
    .toUpperCase();
}

function Profile(){
  const [profile,setProfile] = useState(getStoredUser);
  const fullName = useMemo(
    ()=>`${profile.firstName || ""} ${profile.lastName || ""}`.trim() || profile.userName || "User",
    [profile]
  );
  const initials = initialsFor(profile.firstName,profile.lastName,profile.email);

  useEffect(()=>{
    const storedUser = getStoredUser();
    if(!storedUser.id){
      return;
    }

    api.get(`/users/${storedUser.id}`)
      .then((response)=>{
        const freshProfile = response.data?.data ?? response.data;
        setProfile(freshProfile);
        localStorage.setItem("user", JSON.stringify(freshProfile));
      })
      .catch((err)=>{
        console.error("Error fetching profile:", err);
      });
  },[]);

  return(
    <div className="profile-page">
      <div className="page-heading">
        <div>
          <h1>My Profile</h1>
          <p>View and manage your personal account information.</p>
        </div>
      </div>

      <section className="profile-hero-card">
        <div className="profile-avatar">{initials}</div>
        <div className="profile-identity">
          <h2>{fullName}</h2>
          <p>{profile.email || "No email available"}</p>
          <span>Member since January 2026</span>
          <div className="profile-actions">
            <Link to="/settings">Edit Profile</Link>
            <Link to="/settings?tab=security">Change Password</Link>
          </div>
        </div>
      </section>

      <section className="profile-info-card">
        <h2>Account Information</h2>
        <dl>
          <div>
            <dt>Full Name</dt>
            <dd>{fullName}</dd>
          </div>
          <div>
            <dt>Email Address</dt>
            <dd>{profile.email || "-"}</dd>
          </div>
          <div>
            <dt>Mobile Number</dt>
            <dd>{profile.mobileNumber || "-"}</dd>
          </div>
          <div>
            <dt>Account Created</dt>
            <dd>15 January 2026</dd>
          </div>
          <div>
            <dt>Role</dt>
            <dd>Administrator</dd>
          </div>
          <div>
            <dt>Last Login</dt>
            <dd>Today</dd>
          </div>
        </dl>
      </section>
    </div>
  );
}

export default Profile;
