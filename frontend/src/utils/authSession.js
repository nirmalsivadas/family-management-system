import api from '../api/axios';

const AUTH_HEADER = 'Authorization';

export function clearAxiosAuthHeaders() {
  delete api.defaults.headers.common[AUTH_HEADER];

  ['delete', 'get', 'head', 'patch', 'post', 'put'].forEach((method) => {
    if (api.defaults.headers[method]) {
      delete api.defaults.headers[method][AUTH_HEADER];
    }
  });
}

export function clearClientSession() {
  clearAxiosAuthHeaders();
  localStorage.clear();
  sessionStorage.clear();
  window.dispatchEvent(new CustomEvent('user-profile-updated', {detail: null}));
  window.dispatchEvent(new CustomEvent('auth-session-cleared'));
}

export async function logoutSession() {
  try {
    await api.post('/auth/logout');
  } finally {
    clearClientSession();
  }
}
