import React, { useEffect, useState } from 'react';
import api from '../api/client.js';

export default function Dashboard() {
  const [summary, setSummary] = useState(null);

  useEffect(() => {
    async function load() {
      const res = await api.get('/reports/summary');
      setSummary(res.data);
    }
    load();
  }, []);

  if (!summary) return <div><h1>Dashboard</h1><p>Loading…</p></div>;

  return (
    <div>
      <h1>Dashboard</h1>
      <div className="stat-grid">
        <div className="stat-card">
          <div className="stat-value">{summary.customerCount}</div>
          <div className="stat-label">Customers</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{summary.leadCount}</div>
          <div className="stat-label">Leads</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{summary.dealCount}</div>
          <div className="stat-label">Deals</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">${Number(summary.openPipelineValue).toLocaleString()}</div>
          <div className="stat-label">Open pipeline value</div>
        </div>
      </div>

      <h2 style={{ marginTop: 32 }}>Leads by status</h2>
      <table className="data-table">
        <thead><tr><th>Status</th><th>Count</th></tr></thead>
        <tbody>
          {Object.entries(summary.leadsByStatus || {}).map(([status, count]) => (
            <tr key={status}><td>{status}</td><td>{count}</td></tr>
          ))}
        </tbody>
      </table>

      <h2 style={{ marginTop: 32 }}>Pipeline value by stage</h2>
      <table className="data-table">
        <thead><tr><th>Stage</th><th>Value</th></tr></thead>
        <tbody>
          {Object.entries(summary.pipelineValueByStage || {}).map(([stage, value]) => (
            <tr key={stage}><td>{stage}</td><td>${Number(value).toLocaleString()}</td></tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
