import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import api from '../api/axios';
import './ViewFamilies.css';
import './ViewFamily.css';
import { formatDisplayDate, formatStatus } from '../utils/registerFamily';

function ViewFamily() {
  const { membershipId } = useParams();
  const [family, setFamily] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [statusMessage, setStatusMessage] = useState('');
  const [updatingStatus, setUpdatingStatus] = useState(false);
  const user = JSON.parse(localStorage.getItem('user') || 'null');
  const userId = user?.id;

  function loadFamily() {
    if (!userId || !membershipId) {
      setError('Unable to load this family profile.');
      setLoading(false);
      return;
    }
    setLoading(true);
    api.get(`/family/${membershipId}/view-family`, { params: { userId } })
      .then((response) => {
        setFamily(response.data?.data ?? response.data);
        setError('');
      })
      .catch((err) => {
        setError(err.response?.data?.message || 'Unable to load this family profile.');
      })
      .finally(() => setLoading(false));
  }

  useEffect(() => {
    loadFamily();
  }, [membershipId]);

  async function changeStatus(status) {
    if (!userId || !membershipId) {
      return;
    }
    setUpdatingStatus(true);
    setStatusMessage('');
    try {
      await api.patch(`/family/${membershipId}/status`, null, { params: { userId, status } });
      setStatusMessage(`Status updated to ${formatStatus(status)}.`);
      loadFamily();
    } catch (err) {
      setError(err.response?.data?.message || 'Unable to update status.');
    } finally {
      setUpdatingStatus(false);
    }
  }

  if (loading) {
    return <div className="view-family-container">Loading family profile...</div>;
  }

  if (error || !family) {
    return (
      <div className="view-family-container">
        <p className="empty-state">{error || 'Family not found.'}</p>
        <Link to="/view-families">Back to families</Link>
      </div>
    );
  }

  const currentStatus = String(family.status || '').toUpperCase();

  return (
    <div className="view-family-container">
      <div className="page-heading">
        <div>
          <h1>{family.familyName}</h1>
          <p>{family.membershipId} · {formatStatus(family.status)}</p>
        </div>
        <div className="heading-actions">
          <Link className="ghost-action" to="/view-families">Back</Link>
          <Link className="primary-action" to={`/update-family/${family.membershipId}`}>Edit Family</Link>
        </div>
      </div>
      {statusMessage && <div className="alert-success">{statusMessage}</div>}
      <div className="status-actions">
        <span>Review decision</span>
        <button type="button" disabled={updatingStatus || currentStatus === 'CONFIRMED'} onClick={()=>changeStatus('CONFIRMED')}>Confirm</button>
        <button type="button" disabled={updatingStatus || currentStatus === 'PENDING'} onClick={()=>changeStatus('PENDING')}>Mark pending</button>
        <button type="button" className="danger" disabled={updatingStatus || currentStatus === 'REJECTED'} onClick={()=>changeStatus('REJECTED')}>Reject</button>
      </div>
      <div className="data-panel view-family-card">
        {family.photo && (
          <img className="family-photo" src={`data:image/jpeg;base64,${family.photo}`} alt={family.familHeadName} />
        )}
        <div className="review-grid">
          <div className="review-item"><span>Family Head</span><strong>{family.familHeadName}</strong></div>
          <div className="review-item"><span>Date of Birth</span><strong>{formatDisplayDate(family.dateOfBirth)}</strong></div>
          <div className="review-item"><span>Gender</span><strong>{family.familyHeadGender || '—'}</strong></div>
          <div className="review-item"><span>Marital Status</span><strong>{family.maritalStatus || '—'}</strong></div>
          <div className="review-item"><span>Registration Date</span><strong>{formatDisplayDate(family.registrationDate)}</strong></div>
          <div className="review-item"><span>Members</span><strong>{family.numberOfFamilyMembers}</strong></div>
          <div className="review-item"><span>Membership Type</span><strong>{family.familyMemberShipType}</strong></div>
          <div className="review-item"><span>Category</span><strong>{family.registrationCategory || '—'}</strong></div>
          <div className="review-item"><span>Mobile</span><strong>{family.mobileNumber}</strong></div>
          <div className="review-item"><span>Email</span><strong>{family.email || '—'}</strong></div>
          <div className="review-item"><span>Occupation</span><strong>{family.occupation || '—'}</strong></div>
          <div className="review-item"><span>Organization</span><strong>{family.organization || '—'}</strong></div>
          <div className="review-item"><span>Blood Group</span><strong>{family.bloodGroup || '—'}</strong></div>
          <div className="review-item"><span>Address</span><strong>{[family.address, family.city, family.state, family.pinCode].filter(Boolean).join(', ') || '—'}</strong></div>
        </div>
      </div>
      <div className="data-panel view-family-card" style={{ marginTop: 16 }}>
        <h2>Family Members</h2>
        {(family.familyMembers || []).map((member) => (
          <div className="member-row" key={member.id}>
            <strong>{member.firstName} {member.lastName}</strong>
            <span>{member.relationShipWithFamilyHead} · {member.occupation || 'No occupation'} · {member.mobileNumber || 'No mobile'}</span>
          </div>
        ))}
        {(!family.familyMembers || family.familyMembers.length === 0) && (
          <p className="empty-state">No additional members.</p>
        )}
      </div>
    </div>
  );
}

export default ViewFamily;
