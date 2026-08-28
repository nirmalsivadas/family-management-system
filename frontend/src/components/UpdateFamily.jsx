import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import api from '../api/axios';
import './RegisterFamily.css';
import {
  BLOOD_GROUPS,
  COUNTRIES,
  EMPLOYMENT_TYPES,
  GENDERS,
  INDIAN_STATES,
  MARITAL_STATUSES,
  MEMBERSHIP_TYPES,
  OCCUPATIONS,
  REGISTRATION_CATEGORIES,
  RELATIONS,
  namesFromMaster,
  toIsoDate,
  toLong,
} from '../utils/registerFamily';

function UpdateFamily() {
  const { membershipId } = useParams();
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem('user') || 'null');
  const userId = user?.id;
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [occupations, setOccupations] = useState([]);
  const [designations, setDesignations] = useState([]);
  const [bloodGroups, setBloodGroups] = useState([]);
  const [form, setForm] = useState(null);

  useEffect(() => {
    Promise.allSettled([
      api.get('/master/professions'),
      api.get('/master/designations'),
      api.get('/master/blood-groups'),
    ]).then(([professionRes, designationRes, bloodRes]) => {
      if (professionRes.status === 'fulfilled') setOccupations(namesFromMaster(professionRes.value.data));
      if (designationRes.status === 'fulfilled') setDesignations(namesFromMaster(designationRes.value.data));
      if (bloodRes.status === 'fulfilled') setBloodGroups(namesFromMaster(bloodRes.value.data));
    });
  }, []);

  useEffect(() => {
    if (!userId || !membershipId) {
      setError('Unable to load this family.');
      setLoading(false);
      return;
    }
    api.get(`/family/${membershipId}/view-family`, { params: { userId } })
      .then((response) => {
        const family = response.data?.data ?? response.data;
        setForm({
          familyName: family.familyName || '',
          memberShipType: family.familyMemberShipType || 'Standard',
          registrationCategory: family.registrationCategory || 'General',
          firstName: family.firstName || '',
          middleName: family.middleName || '',
          lastName: family.lastName || '',
          dateOfBirth: toIsoDate(family.dateOfBirth),
          gender: family.familyHeadGender || '',
          maritalStatus: family.maritalStatus || '',
          bloodGroup: family.bloodGroup || '',
          mobileNumber: family.mobileNumber || '',
          alternateMobile: family.alternateMobile || '',
          email: family.email || '',
          occupation: family.occupation || '',
          employment: family.employment || '',
          organization: family.organization || '',
          designation: family.designation || '',
          qualification: family.qualification || '',
          annualIncome: family.annualIncome || '',
          addressLine1: family.addressLine1 || '',
          addressLine2: family.addressLine2 || '',
          city: family.city || '',
          state: family.state || '',
          country: family.country || 'India',
          pinCode: family.pinCode || '',
          members: (family.familyMembers || []).map((member) => ({
            familyMemberId: member.id,
            relationShipWithFamilyHead: member.relationShipWithFamilyHead || '',
            firstName: member.firstName || '',
            middleName: member.middleName || '',
            lastName: member.lastName || '',
            dateOfBirth: toIsoDate(member.dateOfBirth),
            gender: member.gender || '',
            maritalStatus: member.maritalStatus || '',
            bloodGroup: member.bloodGroup || '',
            mobileNumber: member.mobileNumber || '',
            occupation: member.occupation || '',
            employment: member.employment || '',
          })),
        });
      })
      .catch((err) => {
        setError(err.response?.data?.message || 'Unable to load this family.');
      })
      .finally(() => setLoading(false));
  }, [membershipId, userId]);

  function handleChange(event) {
    const { name, value } = event.target;
    setError('');
    setMessage('');
    setForm((current) => ({ ...current, [name]: value }));
  }

  function handleMemberChange(index, event) {
    const { name, value } = event.target;
    setError('');
    setForm((current) => {
      const members = current.members.map((member, memberIndex) => (
        memberIndex === index ? { ...member, [name]: value } : member
      ));
      return { ...current, members };
    });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (!form.familyName.trim() || !form.firstName.trim() || !form.lastName.trim()) {
      setError('Family name and family head name are required.');
      return;
    }
    setSaving(true);
    setError('');
    setMessage('');
    try {
      await api.patch('/family/update-family', {
        memberShipId: membershipId,
        updateFamilyHeadRequest: {
          familyName: form.familyName.trim(),
          memberShipType: form.memberShipType,
          registrationCategory: form.registrationCategory,
          firstName: form.firstName.trim(),
          middleName: form.middleName.trim(),
          lastName: form.lastName.trim(),
          dateOfBirth: form.dateOfBirth || null,
          gender: form.gender,
          maritalStatus: form.maritalStatus,
          bloodGroup: form.bloodGroup,
          mobileNumber: toLong(form.mobileNumber),
          alternateMobile: toLong(form.alternateMobile),
          email: form.email.trim(),
          occupation: form.occupation,
          employment: form.employment,
          profession: form.employment || form.occupation,
          qualification: form.qualification,
          designation: form.designation,
          organization: form.organization.trim(),
          annualIncome: toLong(form.annualIncome),
          addressLine1: form.addressLine1.trim(),
          addressLine2: form.addressLine2.trim(),
          city: form.city.trim(),
          country: form.country,
          state: form.state,
          pinCode: form.pinCode.trim(),
        },
        updateFamilyMemberRequests: form.members.map((member) => ({
          familyMemberId: member.familyMemberId,
          relationShipWithFamilyHead: member.relationShipWithFamilyHead,
          firstName: member.firstName.trim(),
          lastName: member.lastName.trim(),
          gender: member.gender,
          maritalStatus: member.maritalStatus,
          bloodGroup: member.bloodGroup,
          mobileNumber: toLong(member.mobileNumber),
          occupation: member.occupation,
          employment: member.employment,
        })),
      }, { params: { userId } });
      setMessage('Family updated successfully.');
      setTimeout(() => navigate(`/view-family/${membershipId}`), 700);
    } catch (err) {
      setError(err.response?.data?.message || 'Family update failed.');
    } finally {
      setSaving(false);
    }
  }

  if (loading) {
    return <div className="register-family">Loading family...</div>;
  }

  if (!form) {
    return (
      <div className="register-family">
        <p className="alert-error">{error || 'Family not found.'}</p>
        <Link to="/view-families">Back to families</Link>
      </div>
    );
  }

  const occupationOptions = occupations.length ? occupations : OCCUPATIONS;
  const bloodGroupOptions = bloodGroups.length ? bloodGroups : BLOOD_GROUPS;

  return (
    <form className="register-family" onSubmit={handleSubmit}>
      <div className="register-family-header">
        <h1>Edit Family</h1>
        <p>Update family head, address, and member details for {membershipId}.</p>
      </div>
      {error && <div className="alert-error">{error}</div>}
      {message && <div className="alert-success">{message}</div>}

      <section className="reg-card">
        <h2>Family Information</h2>
        <div className="reg-grid">
          <div className="form-group">
            <label htmlFor="familyName">Family Name <span>*</span></label>
            <input id="familyName" name="familyName" value={form.familyName} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label htmlFor="memberShipType">Membership Type</label>
            <select id="memberShipType" name="memberShipType" value={form.memberShipType} onChange={handleChange}>
              {MEMBERSHIP_TYPES.map((type) => <option key={type} value={type}>{type}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="registrationCategory">Registration Category</label>
            <select id="registrationCategory" name="registrationCategory" value={form.registrationCategory} onChange={handleChange}>
              {REGISTRATION_CATEGORIES.map((category) => <option key={category} value={category}>{category}</option>)}
            </select>
          </div>
        </div>
      </section>

      <section className="reg-card" style={{ marginTop: 16 }}>
        <h2>Family Head</h2>
        <div className="reg-grid three">
          <div className="form-group">
            <label>First Name <span>*</span></label>
            <input name="firstName" value={form.firstName} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Middle Name</label>
            <input name="middleName" value={form.middleName} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Last Name <span>*</span></label>
            <input name="lastName" value={form.lastName} onChange={handleChange} />
          </div>
        </div>
        <div className="reg-grid" style={{ marginTop: 16 }}>
          <div className="form-group">
            <label>Date of Birth</label>
            <input type="date" name="dateOfBirth" value={form.dateOfBirth} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Gender</label>
            <select name="gender" value={form.gender} onChange={handleChange}>
              <option value="">Select gender</option>
              {GENDERS.map((gender) => <option key={gender} value={gender}>{gender}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Marital Status</label>
            <select name="maritalStatus" value={form.maritalStatus} onChange={handleChange}>
              <option value="">Select status</option>
              {MARITAL_STATUSES.map((status) => <option key={status} value={status}>{status}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Blood Group</label>
            <select name="bloodGroup" value={form.bloodGroup} onChange={handleChange}>
              <option value="">Select blood group</option>
              {bloodGroupOptions.map((group) => <option key={group} value={group}>{group}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Mobile</label>
            <input name="mobileNumber" value={form.mobileNumber} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Email</label>
            <input name="email" value={form.email} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Occupation</label>
            <select name="occupation" value={form.occupation} onChange={handleChange}>
              <option value="">Select occupation</option>
              {occupationOptions.map((occupation) => <option key={occupation} value={occupation}>{occupation}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Employment</label>
            <select name="employment" value={form.employment} onChange={handleChange}>
              <option value="">Select type</option>
              {EMPLOYMENT_TYPES.map((type) => <option key={type} value={type}>{type}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Designation</label>
            <select name="designation" value={form.designation} onChange={handleChange}>
              <option value="">Select designation</option>
              {(designations.length ? designations : ['Senior Manager', 'Manager', 'Executive', 'Other']).map((item) => (
                <option key={item} value={item}>{item}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label>Organization</label>
            <input name="organization" value={form.organization} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Annual Income</label>
            <input type="number" name="annualIncome" value={form.annualIncome} onChange={handleChange} />
          </div>
        </div>
      </section>

      <section className="reg-card" style={{ marginTop: 16 }}>
        <h2>Address</h2>
        <div className="reg-grid">
          <div className="form-group full">
            <label>Address Line 1</label>
            <input name="addressLine1" value={form.addressLine1} onChange={handleChange} />
          </div>
          <div className="form-group full">
            <label>Address Line 2</label>
            <input name="addressLine2" value={form.addressLine2} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Country</label>
            <select name="country" value={form.country} onChange={handleChange}>
              {COUNTRIES.map((country) => <option key={country} value={country}>{country}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>State</label>
            <select name="state" value={form.state} onChange={handleChange}>
              <option value="">Select state</option>
              {INDIAN_STATES.map((state) => <option key={state} value={state}>{state}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>City</label>
            <input name="city" value={form.city} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Postal Code</label>
            <input name="pinCode" value={form.pinCode} onChange={handleChange} />
          </div>
        </div>
      </section>

      {form.members.map((member, index) => (
        <section className="reg-card" style={{ marginTop: 16 }} key={member.familyMemberId || index}>
          <h2>Member {index + 1}</h2>
          <div className="reg-grid">
            <div className="form-group">
              <label>Relationship</label>
              <select name="relationShipWithFamilyHead" value={member.relationShipWithFamilyHead} onChange={(event) => handleMemberChange(index, event)}>
                <option value="">Select relationship</option>
                {RELATIONS.map((relation) => <option key={relation.value} value={relation.value}>{relation.label}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label>First Name</label>
              <input name="firstName" value={member.firstName} onChange={(event) => handleMemberChange(index, event)} />
            </div>
            <div className="form-group">
              <label>Last Name</label>
              <input name="lastName" value={member.lastName} onChange={(event) => handleMemberChange(index, event)} />
            </div>
            <div className="form-group">
              <label>Gender</label>
              <select name="gender" value={member.gender} onChange={(event) => handleMemberChange(index, event)}>
                <option value="">Select gender</option>
                {GENDERS.map((gender) => <option key={gender} value={gender}>{gender}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label>Occupation</label>
              <select name="occupation" value={member.occupation} onChange={(event) => handleMemberChange(index, event)}>
                <option value="">Select occupation</option>
                {occupationOptions.map((occupation) => <option key={occupation} value={occupation}>{occupation}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label>Mobile</label>
              <input name="mobileNumber" value={member.mobileNumber} onChange={(event) => handleMemberChange(index, event)} />
            </div>
          </div>
        </section>
      ))}

      <div className="reg-actions">
        <Link className="btn btn-ghost" to={`/view-family/${membershipId}`}>Cancel</Link>
        <button className="btn btn-primary" type="submit" disabled={saving}>
          {saving ? 'Saving...' : 'Save changes'}
        </button>
      </div>
    </form>
  );
}

export default UpdateFamily;
