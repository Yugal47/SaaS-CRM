import React, { useEffect, useState } from 'react';
import api from '../api/client.js';

const empty = { name: '', email: '', phone: '', source: '', status: 'NEW' };
const STATUSES = ['NEW', 'CONTACTED', 'QUALIFIED', 'CONVERTED', 'LOST'];

export default function Leads() {
  const [items, setItems] = useState([]);
  const [form, setForm] = useState(empty);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState('');

  const load = async () => {
    const res = await api.get('/leads');
    setItems(res.data);
  };

  useEffect(() => { load(); }, []);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      if (editingId) {
        await api.put(`/leads/${editingId}`, form);
      } else {
        await api.post('/leads', form);
      }
      setForm(empty);
      setEditingId(null);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Save failed');
    }
  };

  const edit = (l) => {
    setEditingId(l.id);
    setForm({ name: l.name, email: l.email || '', phone: l.phone || '', source: l.source || '', status: l.status });
  };

  const remove = async (id) => {
    if (!confirm('Delete this lead?')) return;
    await api.delete(`/leads/${id}`);
    load();
  };

  const quickStatus = async (l, status) => {
    await api.put(`/leads/${l.id}`, { ...l, status });
    load();
  };

  return (
    <div>
      <h1>Leads</h1>

      <form className="inline-form" onSubmit={submit}>
        {error && <div className="error">{error}</div>}
        <input placeholder="Name" value={form.name} onChange={update('name')} required />
        <input placeholder="Email" value={form.email} onChange={update('email')} />
        <input placeholder="Phone" value={form.phone} onChange={update('phone')} />
        <input placeholder="Source" value={form.source} onChange={update('source')} />
        <select value={form.status} onChange={update('status')}>
          {STATUSES.map((s) => <option key={s} value={s}>{s}</option>)}
        </select>
        <button type="submit">{editingId ? 'Update' : 'Add lead'}</button>
        {editingId && <button type="button" onClick={() => { setEditingId(null); setForm(empty); }}>Cancel</button>}
      </form>

      <table className="data-table">
        <thead>
          <tr><th>Name</th><th>Email</th><th>Source</th><th>Status</th><th></th></tr>
        </thead>
        <tbody>
          {items.map((l) => (
            <tr key={l.id}>
              <td>{l.name}</td>
              <td>{l.email}</td>
              <td>{l.source}</td>
              <td>
                <select value={l.status} onChange={(e) => quickStatus(l, e.target.value)}>
                  {STATUSES.map((s) => <option key={s} value={s}>{s}</option>)}
                </select>
              </td>
              <td>
                <button onClick={() => edit(l)}>Edit</button>
                <button onClick={() => remove(l.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
