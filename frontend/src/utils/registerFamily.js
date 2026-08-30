export const STEPS = [
  'Family Info',
  'Family Head',
  'Members',
  'Address',
  'Review',
  'Confirmation',
];

export const RELATIONS = [
  { value: 'FATHER', label: 'Father' },
  { value: 'MOTHER', label: 'Mother' },
  { value: 'HUSBAND', label: 'Husband' },
  { value: 'WIFE', label: 'Wife' },
  { value: 'SON', label: 'Son' },
  { value: 'DAUGHTER', label: 'Daughter' },
  { value: 'BROTHER', label: 'Brother' },
  { value: 'SISTER', label: 'Sister' },
  { value: 'OTHER', label: 'Other' },
];

export function emptyMember() {
  return {
    relationShipWithFamilyHead: '',
    firstName: '',
    middleName: '',
    lastName: '',
    dateOfBirth: '',
    gender: '',
    maritalStatus: '',
    bloodGroup: '',
    mobileNumber: '',
    email: '',
    occupation: '',
    employment: '',
    qualification: '',
    profession: '',
    organization: '',
  };
}

export function createInitialForm() {
  return {
    familyName: '',
    numberOfFamilyMembers: 3,
    memberShipType: 'Standard',
    registrationCategory: 'General',
    firstName: '',
    middleName: '',
    lastName: '',
    dateOfBirth: '',
    gender: '',
    maritalStatus: '',
    bloodGroup: '',
    mobileNumber: '',
    alternateMobile: '',
    email: '',
    occupation: '',
    employment: '',
    qualification: '',
    profession: '',
    organization: '',
    designation: '',
    annualIncome: '',
    addressLine1: '',
    addressLine2: '',
    country: 'India',
    state: '',
    city: '',
    pinCode: '',
    members: [emptyMember(), emptyMember(), emptyMember()],
  };
}

export function namesFromMaster(payload) {
  const list = payload?.data ?? payload ?? [];
  if (!Array.isArray(list)) {
    return [];
  }
  return list
    .map((item) => (typeof item === 'string' ? item : item?.name))
    .filter(Boolean);
}

export function fullName(person) {
  return [person.firstName, person.middleName, person.lastName].filter(Boolean).join(' ');
}

export function formatDisplayDate(value) {
  if (!value) {
    return '—';
  }
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return value;
  }
  return date.toLocaleDateString('en-IN');
}

export function formatStatus(status) {
  if (!status) {
    return 'Pending Review';
  }
  const normalized = String(status).toUpperCase();
  if (normalized === 'PENDING') {
    return 'Pending Review';
  }
  if (normalized === 'CONFIRMED') {
    return 'Confirmed';
  }
  if (normalized === 'REJECTED') {
    return 'Rejected';
  }
  return status;
}

export function isMemberComplete(member) {
  if (!member) {
    return false;
  }
  return Boolean(
    member.relationShipWithFamilyHead &&
    member.firstName?.trim() &&
    member.lastName?.trim() &&
    member.dateOfBirth &&
    member.gender
  );
}

export function memberDisplayName(member, index) {
  const name = fullName(member);
  return name || `Member ${index + 1}`;
}

export function toIsoDate(value) {
  if (!value) {
    return '';
  }
  if (typeof value === 'string') {
    return value.slice(0, 10);
  }
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return '';
  }
  return date.toISOString().slice(0, 10);
}

export function toLong(value) {
  if (value === '' || value === null || value === undefined) {
    return null;
  }
  const parsed = Number(value);
  return Number.isNaN(parsed) ? null : parsed;
}

export function toInputDate(value) {
  if (!value) {
    return '';
  }
  if (typeof value === 'string' && /^\d{4}-\d{2}-\d{2}/.test(value)) {
    return value.slice(0, 10);
  }
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return '';
  }
  const offset = date.getTimezoneOffset() * 60000;
  return new Date(date.getTime() - offset).toISOString().slice(0, 10);
}

export function getStoredUser() {
  try {
    return JSON.parse(localStorage.getItem('user')) || {};
  } catch {
    return {};
  }
}
