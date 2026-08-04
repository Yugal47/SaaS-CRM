import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../api/client.js';
import { useAuth } from '../AuthContext.jsx';

export default function Register() {
  const [form, setForm] = useState({
    organizationName: '',
    slug: '',
    adminName: '',
    adminEmail: '',
    password: ''
  });
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await api.post('/auth/register', form);
      login(res.data);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h1>Create your organization</h1>
        {error && <div className="error">{error}</div>}
        <label>Organization name</label>
        <input value={form.organizationName} onChange={update('organizationName')} required />
        <label>Slug (used to log in)</label>
        <input value={form.slug} onChange={update('slug')} placeholder="acme" required />
        <label>Your name</label>
        <input value={form.adminName} onChange={update('adminName')} required />
        <label>Your email</label>
        <input type="email" value={form.adminEmail} onChange={update('adminEmail')} required />
        <label>Password</label>
        <input type="password" value={form.password} onChange={update('password')} required minLength={8} />
        <button type="submit">Create organization</button>
        <p>Already have one? <Link to="/login">Log in</Link></p>
      </form>
    </div>
  );
}
