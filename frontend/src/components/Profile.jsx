import React,{useEffect,useMemo,useState} from "react";
import {Link} from "react-router-dom";
import api from "../api/axios";
import "./Profile.css";
import { getStoredUser, profileInitials, profileName, profilePhotoSrc, saveStoredUser } from "../utils/profile";

function Profile(){
  const [profile,setProfile] = useState(getStoredUser);
  const fullName = useMemo(
    ()=>profileName(profile),
    [profile]
  );
  const initials = profileInitials(profile);
  const photoSrc = profilePhotoSrc(profile);

  useEffect(()=>{
    const storedUser = getStoredUser();
    if(!storedUser.id){
      return;
    }

    api.get(`/users/${storedUser.id}`)
      .then((response)=>{
        const freshProfile = response.data?.data ?? response.data;
        const nextProfile = {
          ...storedUser,
          ...freshProfile,
          password: undefined,
        };
        setProfile(nextProfile);
        saveStoredUser(nextProfile);
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
        {photoSrc ? (
          <img className="profile-avatar image" src={photoSrc} alt="Profile" />
        ) : (
          <div className="profile-avatar">{initials}</div>
        )}
        <div className="profile-identity">
          <h2>{fullName}</h2>
          <p>{profile.email || "No email available"}</p>
          <span>Signed-in family administrator</span>
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
            <dt>User ID</dt>
            <dd>{profile.id || "-"}</dd>
          </div>
          <div>
            <dt>Role</dt>
            <dd>Family Administrator</dd>
          </div>
        </dl>
      </section>
    </div>
  );
}

export default Profile;
