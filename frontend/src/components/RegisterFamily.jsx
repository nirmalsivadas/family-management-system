import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import FamilyInfo from './FamilyInfo';
import FamilyHead from './FamilyHead';
import FamilyMembers from './FamilyMembers';
import Address from './Address';
import Review from './Review';
import RegistrationSuccess from './RegistrationSuccess';
import './RegisterFamily.css';
import {
  createInitialForm,
  emptyMember,
  isMemberComplete,
  namesFromMaster,
  STEPS,
  toLong,
} from '../utils/registerFamily';

function draftKey(userId) {
  return `registerFamilyDraft:${userId || 'guest'}`;
}

function RegisterFamily() {
  const navigate = useNavigate();
  const user = useMemo(() => {
    try {
      return JSON.parse(localStorage.getItem('user'));
    } catch {
      return null;
    }
  }, []);
  const userId = user?.id;

  const [step, setStep] = useState(0);
  const [form, setForm] = useState(createInitialForm);
  const [photoFile, setPhotoFile] = useState(null);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [confirmed, setConfirmed] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [result, setResult] = useState(null);
  const [masterOptions, setMasterOptions] = useState({
    bloodGroups: [],
    cities: [],
    countries: [],
    designations: [],
    genders: [],
    maritalStatuses: [],
    membershipTypes: [],
    occupations: [],
    professions: [],
    qualifications: [],
    registrationCategories: [],
    states: [],
  });

  useEffect(() => {
    const saved = localStorage.getItem(draftKey(userId));
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        setForm({ ...createInitialForm(), ...parsed, members: parsed.members?.length ? parsed.members : createInitialForm().members });
        setMessage('Draft restored.');
      } catch {
        localStorage.removeItem(draftKey(userId));
      }
    }
  }, [userId]);

  useEffect(() => {
    const endpoints = {
      bloodGroups: '/master/blood-groups',
      cities: '/master/cities',
      countries: '/master/countries',
      designations: '/master/designations',
      genders: '/master/genders',
      maritalStatuses: '/master/marital-status',
      membershipTypes: '/master/membership-types',
      occupations: '/master/occupations',
      professions: '/master/professions',
      qualifications: '/master/qualifications',
      registrationCategories: '/master/registration-categories',
      states: '/master/states',
    };

    Promise.allSettled(
      Object.entries(endpoints).map(([key, url]) => (
        api.get(url).then((response) => [key, namesFromMaster(response.data)])
      ))
    ).then((results) => {
      const nextOptions = {};
      results.forEach((result) => {
        if (result.status === 'fulfilled') {
          const [key, values] = result.value;
          nextOptions[key] = values;
        }
      });
      setMasterOptions((current) => ({ ...current, ...nextOptions }));
    });
  }, []);

  function syncMemberCount(nextForm, rawCount) {
    const count = Math.max(0, Math.min(20, Number(rawCount) || 0));
    const members = [...(nextForm.members || [])];
    while (members.length < count) {
      members.push(emptyMember());
    }
    members.length = count;
    return { ...nextForm, numberOfFamilyMembers: count, members };
  }

  function handleChange(event) {
    const { name, value, type, checked } = event.target;
    setError('');
    setMessage('');
    setForm((current) => {
      if (name === 'numberOfFamilyMembers') {
        return syncMemberCount(current, value);
      }
      if (type === 'checkbox') {
        return { ...current, [name]: checked };
      }
      return { ...current, [name]: value };
    });
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

  function saveDraft() {
    localStorage.setItem(draftKey(userId), JSON.stringify(form));
    setMessage('Draft saved on this device.');
  }

  function validateStep(currentStep) {
    if (currentStep === 0) {
      if (!form.familyName.trim()) return 'Family name is required.';
      if (form.numberOfFamilyMembers === '' || form.numberOfFamilyMembers < 0) return 'Enter a valid number of family members.';
      if (!form.memberShipType) return 'Membership type is required.';
      if (!form.registrationCategory) return 'Registration category is required.';
    }
    if (currentStep === 1) {
      if (!form.firstName.trim() || !form.lastName.trim()) return 'Family head first and last name are required.';
      if (!form.dateOfBirth) return 'Date of birth is required.';
      if (!form.gender) return 'Gender is required.';
      if (!form.maritalStatus) return 'Marital status is required.';
      if (!form.mobileNumber) return 'Mobile number is required.';
      if (!form.occupation) return 'Occupation is required.';
      if (!form.employment) return 'Employment type is required.';
    }
    if (currentStep === 2 && form.members.some((member) => !isMemberComplete(member))) {
      return 'Please complete required details for every family member.';
    }
    if (currentStep === 3) {
      if (!form.addressLine1.trim()) return 'Address line 1 is required.';
      if (!form.country) return 'Country is required.';
      if (!form.state) return 'State is required.';
      if (!form.city.trim()) return 'City is required.';
      if (!form.pinCode.trim()) return 'Postal code is required.';
    }
    return '';
  }

  function goNext() {
    const validationError = validateStep(step);
    if (validationError) {
      setError(validationError);
      return;
    }
    setError('');
    setStep((current) => current + 1);
  }

  async function handleSubmit() {
    const validationError = validateStep(4) || validateStep(1) || validateStep(3) || validateStep(2);
    if (validationError) {
      setError(validationError);
      return;
    }
    if (!userId) {
      setError('Please sign in again before submitting.');
      return;
    }
    if (!confirmed) {
      setError('Please confirm that the information is correct.');
      return;
    }

    setSubmitting(true);
    setError('');
    try {
      const payload = {
        userId,
        registerFamilyHeadRequest: {
          familyName: form.familyName.trim(),
          numberOfFamilyMembers: Number(form.numberOfFamilyMembers) || 0,
          memberShipType: form.memberShipType,
          registrationCategory: form.registrationCategory,
          firstName: form.firstName.trim(),
          middleName: form.middleName.trim(),
          lastName: form.lastName.trim(),
          dateOfBirth: form.dateOfBirth,
          gender: form.gender,
          maritalStatus: form.maritalStatus,
          bloodGroup: form.bloodGroup,
          mobileNumber: toLong(form.mobileNumber),
          alternateMobile: toLong(form.alternateMobile),
          email: form.email.trim(),
          occupation: form.occupation,
          employment: form.employment,
          profession: form.employment,
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
        registerFamilyMemberRequests: form.members.map((member) => ({
          relationShipWithFamilyHead: member.relationShipWithFamilyHead,
          firstName: member.firstName.trim(),
          middleName: member.middleName.trim(),
          lastName: member.lastName.trim(),
          dateOfBirth: member.dateOfBirth,
          gender: member.gender,
          maritalStatus: member.maritalStatus,
          bloodGroup: member.bloodGroup,
          mobileNumber: toLong(member.mobileNumber),
          email: member.email.trim(),
          occupation: member.occupation,
          employment: member.employment,
          profession: member.profession || member.occupation,
          qualification: member.qualification,
          organization: member.organization?.trim() || '',
        })),
      };

      const body = new FormData();
      body.append('request', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
      if (photoFile) {
        body.append('photo', photoFile);
      }

      const response = await api.post('/family/register-family', body);
      const data = response.data?.data ?? response.data;
      localStorage.removeItem(draftKey(userId));
      setResult(data);
      setStep(5);
    } catch (err) {
      setError(err.response?.data?.message || 'Family registration failed.');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="register-family">
      <div className="register-family-header">
        <h1>Register New Family</h1>
        <p>Complete all steps to submit your family membership registration.</p>
      </div>

      <div className="reg-stepper">
        {STEPS.map((label, index) => (
          <React.Fragment key={label}>
            <div className={`reg-step ${index < step ? 'done' : index === step ? 'active' : ''}`}>
              <span className="reg-step-num">{index < step ? '✓' : index + 1}</span>
              <span className="reg-step-label">{label}</span>
            </div>
            {index < STEPS.length - 1 && <div className={`reg-step-line ${index < step ? 'done' : ''}`} />}
          </React.Fragment>
        ))}
      </div>

      {message && step < 5 && <div className="alert-success">{message}</div>}

      {step === 0 && (
        <FamilyInfo
          form={form}
          error={error}
          membershipTypes={masterOptions.membershipTypes}
          registrationCategories={masterOptions.registrationCategories}
          onChange={handleChange}
          onCancel={() => navigate('/dashboard')}
          onSaveDraft={saveDraft}
          onContinue={goNext}
        />
      )}
      {step === 1 && (
        <FamilyHead
          form={form}
          error={error}
          bloodGroups={masterOptions.bloodGroups}
          designations={masterOptions.designations}
          genders={masterOptions.genders}
          maritalStatuses={masterOptions.maritalStatuses}
          occupations={masterOptions.occupations}
          professions={masterOptions.professions}
          qualifications={masterOptions.qualifications}
          onChange={handleChange}
          onPhotoChange={(event) => setPhotoFile(event.target.files?.[0] || null)}
          photoName={photoFile?.name}
          onBack={() => { setError(''); setStep(0); }}
          onSaveDraft={saveDraft}
          onContinue={goNext}
        />
      )}
      {step === 2 && (
        <FamilyMembers
          form={form}
          error={error}
          bloodGroups={masterOptions.bloodGroups}
          genders={masterOptions.genders}
          maritalStatuses={masterOptions.maritalStatuses}
          occupations={masterOptions.occupations}
          professions={masterOptions.professions}
          onMemberChange={handleMemberChange}
          onBack={() => { setError(''); setStep(1); }}
          onSaveDraft={saveDraft}
          onContinue={goNext}
        />
      )}
      {step === 3 && (
        <Address
          form={form}
          error={error}
          cities={masterOptions.cities}
          countries={masterOptions.countries}
          states={masterOptions.states}
          onChange={handleChange}
          onBack={() => { setError(''); setStep(2); }}
          onSaveDraft={saveDraft}
          onContinue={goNext}
        />
      )}
      {step === 4 && (
        <Review
          form={form}
          error={error}
          confirmed={confirmed}
          submitting={submitting}
          onConfirmChange={(event) => setConfirmed(event.target.checked)}
          onEdit={(nextStep) => { setError(''); setStep(nextStep); }}
          onBack={() => { setError(''); setStep(3); }}
          onSaveDraft={saveDraft}
          onSubmit={handleSubmit}
        />
      )}
      {step === 5 && <RegistrationSuccess result={result} />}
    </div>
  );
}

export default RegisterFamily;
