import React, { useEffect, useState } from 'react';
import api from '../api/client.js';

const empty = { title: '', amount: '', customerId: '', stage: 'PROSPECTING', ownerUserId: '' };
const STAGES = ['PROSPECTING', 'QUALIFICATION', 'PROPOSAL', 'NEGOTIATION', 'WON', 'LOST'];

export default function Deals() {
  const [items, setItems] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [form, setForm] = useState(empty);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState('');

  const load = async () => {
    const [dealsRes, customersRes] = await Promise.all([api.get('/deals'), api.get('/customers')]);
    setItems(dealsRes.data);
    setCustomers(customersRes.data);
  };

  useEffect(() => { load(); }, []);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const toPayload = (f) => ({
    title: f.title,
    amount: f.amount === '' ? 0 : Number(f.amount),
    customerId: f.customerId === '' ? null : Number(f.customerId),
    stage: f.stage,
    ownerUserId: f.ownerUserId === '' ? null : Number(f.ownerUserId)
  });

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const payload = toPayload(form);
      if (editingId) {
        await api.put(`/deals/${editingId}`, payload);
      } else {
        await api.post('/deals', payload);
      }
      setForm(empty);
      setEditingId(null);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Save failed');
    }
  };

  const edit = (d) => {
    setEditingId(d.id);
    setForm({
      title: d.title,
      amount: String(d.amount),
      customerId: d.customerId ? String(d.customerId) : '',
      stage: d.stage,
      ownerUserId: d.ownerUserId ? String(d.ownerUserId) : ''
    });
  };

  const remove = async (id) => {
    if (!confirm('Delete this deal?')) return;
    await api.delete(`/deals/${id}`);
    load();
  };

  const quickStage = async (d, stage) => {
    await api.put(`/deals/${d.id}`, { ...d, stage });
    load();
  };

  const customerName = (id) => customers.find((c) => c.id === id)?.name || '—';

  return (
    <div>
      <h1>Deals</h1>

      <form className="inline-form" onSubmit={submit}>
        {error && <div className="error">{error}</div>}
        <input placeholder="Title" value={form.title} onChange={update('title')} required />
        <input placeholder="Amount" type="number" step="0.01" value={form.amount} onChange={update('amount')} required />
        <select value={form.customerId} onChange={update('customerId')}>
          <option value="">No customer linked</option>
          {customers.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
        </select>
        <select value={form.stage} onChange={update('stage')}>
          {STAGES.map((s) => <option key={s} value={s}>{s}</option>)}
        </select>
        <button type="submit">{editingId ? 'Update' : 'Add deal'}</button>
        {editingId && <button type="button" onClick={() => { setEditingId(null); setForm(empty); }}>Cancel</button>}
      </form>

      <table className="data-table">
        <thead>
          <tr><th>Title</th><th>Amount</th><th>Customer</th><th>Stage</th><th></th></tr>
        </thead>
        <tbody>
          {items.map((d) => (
            <tr key={d.id}>
              <td>{d.title}</td>
              <td>${Number(d.amount).toLocaleString()}</td>
              <td>{customerName(d.customerId)}</td>
              <td>
                <select value={d.stage} onChange={(e) => quickStage(d, e.target.value)}>
                  {STAGES.map((s) => <option key={s} value={s}>{s}</option>)}
                </select>
              </td>
              <td>
                <button onClick={() => edit(d)}>Edit</button>
                <button onClick={() => remove(d.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
