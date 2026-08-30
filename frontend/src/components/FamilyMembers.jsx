import React, { useState } from 'react';
import {
  isMemberComplete,
  RELATIONS,
} from '../utils/registerFamily';

function FamilyMembers({
  form,
  error,
  bloodGroups,
  genders,
  maritalStatuses,
  occupations,
  professions,
  onMemberChange,
  onBack,
  onSaveDraft,
  onContinue,
}) {
  const [openIndex, setOpenIndex] = useState(0);

  return (
    <section className="reg-card">
      <h2>Family Members</h2>
      <p className="section-copy">
        {form.members.length
          ? `Add information for all ${form.members.length} family members.`
          : 'No additional members to add. You can continue.'}
      </p>
      {error && <div className="alert-error" style={{ marginTop: 16 }}>{error}</div>}

      {form.members.map((member, index) => {
        const complete = isMemberComplete(member);
        const open = openIndex === index;
        return (
          <div className="member-accordion" key={index}>
            <button type="button" className="member-accordion-header" onClick={() => setOpenIndex(open ? -1 : index)}>
              <span className={`member-status-icon ${complete ? 'ok' : 'warn'}`}>{complete ? '✓' : '!'}</span>
              <span>
                <strong>Member {index + 1}</strong>
                <small>{complete ? `${member.firstName} ${member.lastName}` : 'Information incomplete.'}</small>
              </span>
              <span className="chevron">{open ? '▴' : '▾'}</span>
            </button>
            {open && (
              <div className="member-accordion-body">
                <div className="reg-grid" style={{ marginTop: 16 }}>
                  <div className="form-group full">
                    <label htmlFor={`relation-${index}`}>Relationship to Family Head <span>*</span></label>
                    <select
                      id={`relation-${index}`}
                      name="relationShipWithFamilyHead"
                      value={member.relationShipWithFamilyHead}
                      onChange={(event) => onMemberChange(index, event)}
                    >
                      <option value="">Select relationship</option>
                      {RELATIONS.map((relation) => (
                        <option key={relation.value} value={relation.value}>{relation.label}</option>
                      ))}
                    </select>
                  </div>
                </div>
                <div className="reg-grid three" style={{ marginTop: 16 }}>
                  <div className="form-group">
                    <label htmlFor={`firstName-${index}`}>First Name <span>*</span></label>
                    <input id={`firstName-${index}`} name="firstName" value={member.firstName} onChange={(event) => onMemberChange(index, event)} />
                  </div>
                  <div className="form-group">
                    <label htmlFor={`middleName-${index}`}>Middle Name</label>
                    <input id={`middleName-${index}`} name="middleName" value={member.middleName} onChange={(event) => onMemberChange(index, event)} />
                  </div>
                  <div className="form-group">
                    <label htmlFor={`lastName-${index}`}>Last Name <span>*</span></label>
                    <input id={`lastName-${index}`} name="lastName" value={member.lastName} onChange={(event) => onMemberChange(index, event)} />
                  </div>
                </div>
                <div className="reg-grid" style={{ marginTop: 16 }}>
                  <div className="form-group">
                    <label htmlFor={`dob-${index}`}>Date of Birth <span>*</span></label>
                    <input id={`dob-${index}`} name="dateOfBirth" type="date" value={member.dateOfBirth} onChange={(event) => onMemberChange(index, event)} />
                  </div>
                  <div className="form-group">
                    <label htmlFor={`gender-${index}`}>Gender <span>*</span></label>
                    <select id={`gender-${index}`} name="gender" value={member.gender} onChange={(event) => onMemberChange(index, event)}>
                      <option value="">Select gender</option>
                      {genders.map((gender) => <option key={gender} value={gender}>{gender}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label htmlFor={`marital-${index}`}>Marital Status</label>
                    <select id={`marital-${index}`} name="maritalStatus" value={member.maritalStatus} onChange={(event) => onMemberChange(index, event)}>
                      <option value="">Select status</option>
                      {maritalStatuses.map((status) => <option key={status} value={status}>{status}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label htmlFor={`blood-${index}`}>Blood Group</label>
                    <select id={`blood-${index}`} name="bloodGroup" value={member.bloodGroup} onChange={(event) => onMemberChange(index, event)}>
                      <option value="">Select blood group</option>
                      {bloodGroups.map((group) => <option key={group} value={group}>{group}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label htmlFor={`mobile-${index}`}>Mobile</label>
                    <input id={`mobile-${index}`} name="mobileNumber" value={member.mobileNumber} onChange={(event) => onMemberChange(index, event)} />
                  </div>
                  <div className="form-group">
                    <label htmlFor={`email-${index}`}>Email</label>
                    <input id={`email-${index}`} name="email" type="email" value={member.email} onChange={(event) => onMemberChange(index, event)} />
                  </div>
                  <div className="form-group">
                    <label htmlFor={`occupation-${index}`}>Occupation</label>
                    <select id={`occupation-${index}`} name="occupation" value={member.occupation} onChange={(event) => onMemberChange(index, event)}>
                      <option value="">Select occupation</option>
                      {occupations.map((occupation) => <option key={occupation} value={occupation}>{occupation}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label htmlFor={`employment-${index}`}>Employment Type</label>
                    <select id={`employment-${index}`} name="employment" value={member.employment} onChange={(event) => onMemberChange(index, event)}>
                      <option value="">Select type</option>
                      {professions.map((type) => <option key={type} value={type}>{type}</option>)}
                    </select>
                  </div>
                </div>
                <div className="member-actions">
                  <button type="button" className="btn btn-ghost" onClick={() => setOpenIndex(-1)}>Cancel</button>
                  <button type="button" className="btn btn-primary" onClick={() => setOpenIndex(index < form.members.length - 1 ? index + 1 : -1)}>Save Member</button>
                </div>
              </div>
            )}
          </div>
        );
      })}

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

export default FamilyMembers;
