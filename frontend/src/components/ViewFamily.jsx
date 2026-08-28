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

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;
    if (!userId || !membershipId) {
      setError('Unable to load this family profile.');
      setLoading(false);
      return;
    }
    api.get(`/family/${membershipId}/view-family`, { params: { userId } })
      .then((response) => {
        setFamily(response.data?.data ?? response.data);
      })
      .catch((err) => {
        setError(err.response?.data?.message || 'Unable to load this family profile.');
      })
      .finally(() => setLoading(false));
  }, [membershipId]);

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

  return (
    <div className="view-family-container">
      <div className="page-heading">
        <div>
          <h1>{family.familyName}</h1>
          <p>{family.membershipId} · {formatStatus(family.status)}</p>
        </div>
        <Link className="primary-action" to="/view-families">Back to Families</Link>
      </div>
      <div className="data-panel view-family-card">
        {family.photo && (
          <img className="family-photo" src={`data:image/jpeg;base64,${family.photo}`} alt={family.familHeadName} />
        )}
        <div className="review-grid">
          <div className="review-item"><span>Family Head</span><strong>{family.familHeadName}</strong></div>
          <div className="review-item"><span>Registration Date</span><strong>{formatDisplayDate(family.registrationDate)}</strong></div>
          <div className="review-item"><span>Members</span><strong>{family.numberOfFamilyMembers}</strong></div>
          <div className="review-item"><span>Membership Type</span><strong>{family.familyMemberShipType}</strong></div>
          <div className="review-item"><span>Mobile</span><strong>{family.mobileNumber}</strong></div>
          <div className="review-item"><span>Email</span><strong>{family.email || '—'}</strong></div>
          <div className="review-item"><span>Occupation</span><strong>{family.occupation || '—'}</strong></div>
          <div className="review-item"><span>Address</span><strong>{family.address || '—'}</strong></div>
        </div>
      </div>
      <div className="data-panel view-family-card" style={{ marginTop: 16 }}>
        <h2>Family Members</h2>
        {(family.familyMembers || []).map((member) => (
          <div className="member-row" key={member.id}>
            <strong>{member.firstName} {member.lastName}</strong>
            <span>{member.relationShipWithFamilyHead}</span>
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
