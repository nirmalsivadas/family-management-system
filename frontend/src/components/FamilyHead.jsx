import React from 'react';
import {
  BLOOD_GROUPS,
  EMPLOYMENT_TYPES,
  GENDERS,
  MARITAL_STATUSES,
  OCCUPATIONS,
} from '../utils/registerFamily';

function FamilyHead({
  form,
  error,
  occupations,
  designations,
  bloodGroups,
  onChange,
  onPhotoChange,
  photoName,
  onBack,
  onSaveDraft,
  onContinue,
}) {
  const occupationOptions = occupations.length ? occupations : OCCUPATIONS;
  const bloodGroupOptions = bloodGroups.length ? bloodGroups : BLOOD_GROUPS;

  return (
    <section className="reg-card">
      <h2>Family Head Information</h2>
      <p className="section-copy">Enter personal, contact, and professional details of the family head.</p>
      {error && <div className="alert-error" style={{ marginTop: 16 }}>{error}</div>}

      <h3 className="reg-section-title">Personal Information</h3>
      <div className="reg-grid three">
        <div className="form-group">
          <label htmlFor="firstName">First Name <span>*</span></label>
          <input id="firstName" name="firstName" value={form.firstName} onChange={onChange} placeholder="Rajesh" />
        </div>
        <div className="form-group">
          <label htmlFor="middleName">Middle Name</label>
          <input id="middleName" name="middleName" value={form.middleName} onChange={onChange} placeholder="Kumar" />
        </div>
        <div className="form-group">
          <label htmlFor="lastName">Last Name <span>*</span></label>
          <input id="lastName" name="lastName" value={form.lastName} onChange={onChange} placeholder="Sharma" />
        </div>
      </div>
      <div className="reg-grid" style={{ marginTop: 16 }}>
        <div className="form-group">
          <label htmlFor="dateOfBirth">Date of Birth <span>*</span></label>
          <input id="dateOfBirth" name="dateOfBirth" type="date" value={form.dateOfBirth} onChange={onChange} />
        </div>
        <div className="form-group">
          <label htmlFor="gender">Gender <span>*</span></label>
          <select id="gender" name="gender" value={form.gender} onChange={onChange}>
            <option value="">Select gender</option>
            {GENDERS.map((gender) => <option key={gender} value={gender}>{gender}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="maritalStatus">Marital Status <span>*</span></label>
          <select id="maritalStatus" name="maritalStatus" value={form.maritalStatus} onChange={onChange}>
            <option value="">Select status</option>
            {MARITAL_STATUSES.map((status) => <option key={status} value={status}>{status}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="bloodGroup">Blood Group</label>
          <select id="bloodGroup" name="bloodGroup" value={form.bloodGroup} onChange={onChange}>
            <option value="">Select blood group</option>
            {bloodGroupOptions.map((group) => <option key={group} value={group}>{group}</option>)}
          </select>
        </div>
      </div>

      <h3 className="reg-section-title">Contact Information</h3>
      <div className="reg-grid">
        <div className="form-group">
          <label htmlFor="mobileNumber">Mobile Number <span>*</span></label>
          <input id="mobileNumber" name="mobileNumber" value={form.mobileNumber} onChange={onChange} placeholder="9876543210" />
        </div>
        <div className="form-group">
          <label htmlFor="alternateMobile">Alternate Mobile</label>
          <input id="alternateMobile" name="alternateMobile" value={form.alternateMobile} onChange={onChange} placeholder="9123456780" />
        </div>
        <div className="form-group full">
          <label htmlFor="email">Email Address</label>
          <input id="email" name="email" type="email" value={form.email} onChange={onChange} placeholder="rajesh@email.com" />
        </div>
      </div>

      <h3 className="reg-section-title">Professional Information</h3>
      <div className="reg-grid">
        <div className="form-group">
          <label htmlFor="occupation">Occupation <span>*</span></label>
          <select id="occupation" name="occupation" value={form.occupation} onChange={onChange}>
            <option value="">Select occupation</option>
            {occupationOptions.map((occupation) => <option key={occupation} value={occupation}>{occupation}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="employment">Employment Type <span>*</span></label>
          <select id="employment" name="employment" value={form.employment} onChange={onChange}>
            <option value="">Select type</option>
            {EMPLOYMENT_TYPES.map((type) => <option key={type} value={type}>{type}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="organization">Employer / Organization</label>
          <input id="organization" name="organization" value={form.organization} onChange={onChange} placeholder="TechCorp Solutions" />
        </div>
        <div className="form-group">
          <label htmlFor="designation">Designation</label>
          <select id="designation" name="designation" value={form.designation} onChange={onChange}>
            <option value="">Select designation</option>
            {(designations.length ? designations : ['Senior Manager', 'Manager', 'Executive', 'Other']).map((item) => (
              <option key={item} value={item}>{item}</option>
            ))}
          </select>
        </div>
        <div className="form-group full">
          <label htmlFor="annualIncome">Annual Income (₹)</label>
          <input id="annualIncome" name="annualIncome" type="number" min="0" value={form.annualIncome} onChange={onChange} placeholder="1800000" />
        </div>
        <div className="form-group full">
          <label htmlFor="photo">Family Head Photo</label>
          <input id="photo" name="photo" type="file" accept="image/*" onChange={onPhotoChange} />
          {photoName && <p className="helper-text">Selected: {photoName}</p>}
        </div>
      </div>

      <div className="reg-actions">
        <button type="button" className="btn btn-ghost" onClick={onBack}>← Back</button>
        <div className="reg-actions-right">
          <button type="button" className="btn btn-draft" onClick={onSaveDraft}>Save Draft</button>
          <button type="button" className="btn btn-primary" onClick={onContinue}>Continue →</button>
        </div>
      </div>
    </section>
  );
}

export default FamilyHead;
