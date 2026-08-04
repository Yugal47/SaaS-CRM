import React, { useEffect, useState } from 'react';
import api from '../api/client.js';

const empty = { name: '', email: '', phone: '', company: '', notes: '' };

export default function Customers() {
  const [items, setItems] = useState([]);
  const [form, setForm] = useState(empty);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState('');

  const load = async () => {
    const res = await api.get('/customers');
    setItems(res.data);
  };

  useEffect(() => { load(); }, []);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      if (editingId) {
        await api.put(`/customers/${editingId}`, form);
      } else {
        await api.post('/customers', form);
      }
      setForm(empty);
      setEditingId(null);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Save failed');
    }
  };

  const edit = (c) => {
    setEditingId(c.id);
    setForm({ name: c.name, email: c.email || '', phone: c.phone || '', company: c.company || '', notes: c.notes || '' });
  };

  const remove = async (id) => {
    if (!confirm('Delete this customer?')) return;
    await api.delete(`/customers/${id}`);
    load();
  };

  return (
    <div>
      <h1>Customers</h1>

      <form className="inline-form" onSubmit={submit}>
        {error && <div className="error">{error}</div>}
        <input placeholder="Name" value={form.name} onChange={update('name')} required />
        <input placeholder="Email" value={form.email} onChange={update('email')} />
        <input placeholder="Phone" value={form.phone} onChange={update('phone')} />
        <input placeholder="Company" value={form.company} onChange={update('company')} />
        <input placeholder="Notes" value={form.notes} onChange={update('notes')} />
        <button type="submit">{editingId ? 'Update' : 'Add customer'}</button>
        {editingId && <button type="button" onClick={() => { setEditingId(null); setForm(empty); }}>Cancel</button>}
      </form>

      <table className="data-table">
        <thead>
          <tr><th>Name</th><th>Email</th><th>Phone</th><th>Company</th><th></th></tr>
        </thead>
        <tbody>
          {items.map((c) => (
            <tr key={c.id}>
              <td>{c.name}</td>
              <td>{c.email}</td>
              <td>{c.phone}</td>
              <td>{c.company}</td>
              <td>
                <button onClick={() => edit(c)}>Edit</button>
                <button onClick={() => remove(c.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
