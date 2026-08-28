import React from 'react';
import { Link } from 'react-router-dom';
import { formatDisplayDate, formatStatus } from '../utils/registerFamily';

function RegistrationSuccess({ result }) {
  const membershipNumber = result?.memberShipNumber || result?.membershipNumber;
  return (
    <section className="reg-card success-wrap">
      <div className="success-check">✓</div>
      <h1>Registration Successful!</h1>
      <p>Your family membership has been successfully submitted.</p>
      <div className="success-summary">
        <div className="success-row">
          <span>Membership Number</span>
          <strong>{membershipNumber || '—'}</strong>
        </div>
        <div className="success-row">
          <span>Family Name</span>
          <strong>{result?.familyName || '—'}</strong>
        </div>
        <div className="success-row">
          <span>Registration Date</span>
          <strong>{formatDisplayDate(result?.registrationDate)}</strong>
        </div>
        <div className="success-row">
          <span>Family Head</span>
          <strong>{result?.familyHeadName || '—'}</strong>
        </div>
        <div className="success-row">
          <span>Members</span>
          <strong>{result?.numberOfMembers ?? '—'}</strong>
        </div>
        <div className="success-row">
          <span>Status</span>
          <strong>{formatStatus(result?.status)}</strong>
        </div>
      </div>
      <div className="success-actions">
        <Link className="btn btn-ghost" to={membershipNumber ? `/view-family/${membershipNumber}` : '/view-families'}>
          View Family Profile
        </Link>
        <Link className="btn btn-primary" to="/dashboard">Go to Dashboard</Link>
      </div>
    </section>
  );
}

export default RegistrationSuccess;
