import React from 'react';
import { COUNTRIES, INDIAN_STATES } from '../utils/registerFamily';

function Address({ form, error, onChange, onBack, onSaveDraft, onContinue }) {
  return (
    <section className="reg-card">
      <h2>Permanent Address</h2>
      <p className="section-copy">Enter the family's permanent address details.</p>
      {error && <div className="alert-error" style={{ marginTop: 16 }}>{error}</div>}
      <div className="reg-grid">
        <div className="form-group full">
          <label htmlFor="addressLine1">Address Line 1 <span>*</span></label>
          <input id="addressLine1" name="addressLine1" value={form.addressLine1} onChange={onChange} placeholder="House no., Street name" />
        </div>
        <div className="form-group full">
          <label htmlFor="addressLine2">Address Line 2</label>
          <input id="addressLine2" name="addressLine2" value={form.addressLine2} onChange={onChange} placeholder="Area, Landmark (optional)" />
        </div>
        <div className="form-group">
          <label htmlFor="country">Country <span>*</span></label>
          <select id="country" name="country" value={form.country} onChange={onChange}>
            <option value="">Select country</option>
            {COUNTRIES.map((country) => <option key={country} value={country}>{country}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="state">State <span>*</span></label>
          <select id="state" name="state" value={form.state} onChange={onChange}>
            <option value="">Select state</option>
            {INDIAN_STATES.map((state) => <option key={state} value={state}>{state}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="city">City <span>*</span></label>
          <input id="city" name="city" value={form.city} onChange={onChange} placeholder="City name" />
        </div>
        <div className="form-group">
          <label htmlFor="pinCode">Postal Code <span>*</span></label>
          <input id="pinCode" name="pinCode" value={form.pinCode} onChange={onChange} placeholder="110001" />
        </div>
      </div>

      <h3 className="reg-section-title">Current Address</h3>
      <label className="reg-check">
        <input
          type="checkbox"
          name="currentSameAsPermanent"
          checked={form.currentSameAsPermanent}
          onChange={onChange}
        />
        Same as permanent address
      </label>

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

export default Address;
