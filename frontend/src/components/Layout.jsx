import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../AuthContext.jsx';

export default function Layout() {
  const { user, logout } = useAuth();

  return (
    <div className="layout">
      <aside className="sidebar">
        <h2>CRM</h2>
        <nav>
          <NavLink to="/" end>Dashboard</NavLink>
          <NavLink to="/customers">Customers</NavLink>
          <NavLink to="/leads">Leads</NavLink>
          <NavLink to="/deals">Deals</NavLink>
          <NavLink to="/notifications">Notifications</NavLink>
          <NavLink to="/files">Files</NavLink>
        </nav>
        <div className="sidebar-footer">
          <div>{user?.email}</div>
          <div className="role-badge">{user?.role}</div>
          <button onClick={logout}>Log out</button>
        </div>
      </aside>
      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}
