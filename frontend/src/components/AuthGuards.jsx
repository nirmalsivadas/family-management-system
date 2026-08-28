import React, { useEffect, useMemo, useState } from 'react';
import { Link, Navigate, Outlet } from 'react-router-dom';

function getUser() {
  try {
    return JSON.parse(localStorage.getItem('user'));
  } catch {
    return null;
  }
}

function RequireAuth() {
  const user = getUser();
  if (!user?.id) {
    return <Navigate to="/login" replace />;
  }
  return <Outlet />;
}

function PublicOnly() {
  const user = getUser();
  if (user?.id) {
    return <Navigate to="/dashboard" replace />;
  }
  return <Outlet />;
}

export { RequireAuth, PublicOnly };
