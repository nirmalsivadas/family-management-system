export function getStoredUser() {
  try {
    return JSON.parse(localStorage.getItem('user')) || {};
  } catch {
    return {};
  }
}

export function profileName(user) {
  return `${user?.firstName || ''} ${user?.lastName || ''}`.trim() || user?.userName || 'User';
}

export function profileInitials(user) {
  return profileName(user)
    .split(' ')
    .filter(Boolean)
    .map((part) => part[0])
    .join('')
    .slice(0, 2)
    .toUpperCase();
}

export function profilePhotoSrc(user) {
  return user?.photo ? `data:image/jpeg;base64,${user.photo}` : '';
}

export function saveStoredUser(user) {
  localStorage.setItem('user', JSON.stringify(user));
  window.dispatchEvent(new CustomEvent('user-profile-updated', {detail: user}));
}
