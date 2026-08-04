import React, { useEffect, useState } from 'react';
import api from '../api/client.js';

export default function Notifications() {
  const [items, setItems] = useState([]);
  const [form, setForm] = useState({ channel: 'EMAIL', recipient: '', message: '' });
  const [error, setError] = useState('');

  const load = async () => {
    const res = await api.get('/notifications');
    setItems(res.data);
  };

  useEffect(() => { load(); }, []);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await api.post('/notifications', form);
      setForm({ channel: 'EMAIL', recipient: '', message: '' });
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Send failed');
    }
  };

  return (
    <div>
      <h1>Notifications</h1>

      <form className="inline-form" onSubmit={submit}>
        {error && <div className="error">{error}</div>}
        <select value={form.channel} onChange={update('channel')}>
          <option value="EMAIL">Email</option>
          <option value="SMS">SMS</option>
        </select>
        <input placeholder="Recipient" value={form.recipient} onChange={update('recipient')} required />
        <input placeholder="Message" value={form.message} onChange={update('message')} required />
        <button type="submit">Send</button>
      </form>

      <table className="data-table">
        <thead>
          <tr><th>Channel</th><th>Recipient</th><th>Message</th><th>Status</th><th>Sent</th></tr>
        </thead>
        <tbody>
          {items.map((n) => (
            <tr key={n.id}>
              <td>{n.channel}</td>
              <td>{n.recipient}</td>
              <td>{n.message}</td>
              <td>{n.status}</td>
              <td>{new Date(n.sentAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
