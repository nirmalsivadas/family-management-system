import React from 'react';
import { formatDisplayDate, fullName, isMemberComplete, memberDisplayName } from '../utils/registerFamily';

function Review({
  form,
  error,
  confirmed,
  submitting,
  onConfirmChange,
  onEdit,
  onBack,
  onSaveDraft,
  onSubmit,
}) {
  const canSubmit = confirmed && form.members.every(isMemberComplete) && !submitting;

  return (
    <div>
      <div className="alert-info">Please review all information carefully before submitting.</div>
      {error && <div className="alert-error">{error}</div>}

      <section className="review-card">
        <div className="review-card-head">
          <h3>Family Information</h3>
          <button type="button" className="edit-link" onClick={() => onEdit(0)}>Edit</button>
        </div>
        <div className="review-grid">
          <div className="review-item"><span>Family Name</span><strong>{form.familyName || '—'}</strong></div>
          <div className="review-item"><span>Members</span><strong>{form.members.length}</strong></div>
          <div className="review-item"><span>Membership Type</span><strong>{form.memberShipType || '—'}</strong></div>
          <div className="review-item"><span>Category</span><strong>{form.registrationCategory || '—'}</strong></div>
        </div>
      </section>

      <section className="review-card">
        <div className="review-card-head">
          <h3>Family Head</h3>
          <button type="button" className="edit-link" onClick={() => onEdit(1)}>Edit</button>
        </div>
        <div className="review-grid">
          <div className="review-item"><span>Name</span><strong>{fullName(form) || '—'}</strong></div>
          <div className="review-item"><span>Date of Birth</span><strong>{formatDisplayDate(form.dateOfBirth)}</strong></div>
          <div className="review-item"><span>Gender</span><strong>{form.gender || '—'}</strong></div>
          <div className="review-item"><span>Mobile</span><strong>{form.mobileNumber || '—'}</strong></div>
          <div className="review-item"><span>Occupation</span><strong>{form.occupation || '—'}</strong></div>
          <div className="review-item"><span>Blood Group</span><strong>{form.bloodGroup || '—'}</strong></div>
        </div>
      </section>

      <section className="review-card">
        <div className="review-card-head">
          <h3>Family Members ({form.members.length})</h3>
          <button type="button" className="edit-link" onClick={() => onEdit(2)}>Edit</button>
        </div>
        {form.members.length === 0 && <p className="helper-text">No additional members.</p>}
        {form.members.map((member, index) => (
          <div className="member-row" key={index}>
            <span className="member-index">{index + 1}</span>
            <strong>{isMemberComplete(member) ? memberDisplayName(member, index) : 'Relationship not set'}</strong>
            {!isMemberComplete(member) && <span className="incomplete-pill">Incomplete</span>}
          </div>
        ))}
      </section>

      <section className="review-card">
        <div className="review-card-head">
          <h3>Address</h3>
          <button type="button" className="edit-link" onClick={() => onEdit(3)}>Edit</button>
        </div>
        <div className="review-item">
          <span>Permanent Address</span>
          <strong>{form.addressLine1}</strong>
          <strong>{[form.addressLine2, form.city, form.state].filter(Boolean).join(', ')}{form.pinCode ? ` — ${form.pinCode}` : ''}{form.country ? `, ${form.country}` : ''}</strong>
        </div>
        {form.currentSameAsPermanent && (
          <p className="helper-text">Current address same as permanent</p>
        )}
      </section>

      <section className="review-card">
        <label className="reg-check">
          <input type="checkbox" checked={confirmed} onChange={onConfirmChange} />
          I confirm that all the information provided above is correct and complete. I understand that false information may result in cancellation of the membership.
        </label>
      </section>

      <div className="reg-actions">
        <button type="button" className="btn btn-ghost" onClick={onBack}>← Back</button>
        <div className="reg-actions-right">
          <button type="button" className="btn btn-draft" onClick={onSaveDraft}>Save Draft</button>
          <button type="button" className="btn btn-primary" onClick={onSubmit} disabled={!canSubmit}>
            {submitting ? 'Submitting...' : 'Confirm & Submit'}
          </button>
        </div>
      </div>
    </div>
  );
}

export default Review;
