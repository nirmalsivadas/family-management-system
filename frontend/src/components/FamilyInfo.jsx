import React from 'react';

function FamilyInfo({
  form,
  error,
  membershipTypes,
  registrationCategories,
  onChange,
  onCancel,
  onSaveDraft,
  onContinue,
}) {
  return (
    <section className="reg-card">
      <h2>Family Information</h2>
      <p className="section-copy">Enter the basic details about the family.</p>
      {error && <div className="alert-error" style={{ marginTop: 16 }}>{error}</div>}
      <div className="reg-grid">
        <div className="form-group full">
          <label htmlFor="familyName">Family Name <span>*</span></label>
          <input
            id="familyName"
            name="familyName"
            value={form.familyName}
            onChange={onChange}
            placeholder="e.g. Sharma Family"
          />
        </div>
        <div className="form-group full">
          <label htmlFor="numberOfFamilyMembers">Number of Family Members <span>*</span></label>
          <input
            id="numberOfFamilyMembers"
            name="numberOfFamilyMembers"
            type="number"
            min="0"
            max="20"
            value={form.numberOfFamilyMembers}
            onChange={onChange}
          />
          <p className="helper-text">Enter the total number of family members you want to register. (Excluding the family head)</p>
        </div>
        <div className="form-group">
          <label htmlFor="memberShipType">Membership Type <span>*</span></label>
          <select id="memberShipType" name="memberShipType" value={form.memberShipType} onChange={onChange}>
            <option value="">Select type</option>
            {membershipTypes.map((type) => (
              <option key={type} value={type}>{type}</option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="registrationCategory">Registration Category <span>*</span></label>
          <select id="registrationCategory" name="registrationCategory" value={form.registrationCategory} onChange={onChange}>
            <option value="">Select category</option>
            {registrationCategories.map((category) => (
              <option key={category} value={category}>{category}</option>
            ))}
          </select>
        </div>
      </div>
      <div className="reg-actions">
        <button type="button" className="btn btn-ghost" onClick={onCancel}>Cancel</button>
        <div className="reg-actions-right">
          <button type="button" className="btn btn-draft" onClick={onSaveDraft}>Save Draft</button>
          <button type="button" className="btn btn-primary" onClick={onContinue}>Continue →</button>
        </div>
      </div>
    </section>
  );
}

export default FamilyInfo;
