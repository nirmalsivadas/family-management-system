export function getNotificationPreferences() {
  try {
    const saved = JSON.parse(localStorage.getItem('notificationPreferences'));
    return {
      normal: saved?.normal ?? saved?.email ?? true,
      status: saved?.status ?? true,
    };
  } catch {
    return {
      normal: true,
      status: true,
    };
  }
}

export function saveNotificationPreferences(preferences) {
  localStorage.setItem('notificationPreferences', JSON.stringify(preferences));
  window.dispatchEvent(new CustomEvent('notification-preferences-updated', {detail: preferences}));
}

export function isStatusNotification(notification) {
  const title = String(notification?.title || '').toLowerCase();
  const message = String(notification?.message || '').toLowerCase();
  return title === 'status-changed' || title.includes('status changed') || message.includes('status changed');
}

export function filterNotificationsByPreferences(notifications, preferences = getNotificationPreferences()) {
  return notifications.filter((notification) => (
    isStatusNotification(notification) ? preferences.status : preferences.normal
  ));
}
