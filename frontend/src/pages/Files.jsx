import React, { useEffect, useState } from 'react';
import api from '../api/client.js';

export default function Files() {
  const [items, setItems] = useState([]);
  const [file, setFile] = useState(null);
  const [error, setError] = useState('');

  const load = async () => {
    const res = await api.get('/files');
    setItems(res.data);
  };

  useEffect(() => { load(); }, []);

  const upload = async (e) => {
    e.preventDefault();
    setError('');
    if (!file) return;
    try {
      const formData = new FormData();
      formData.append('file', file);
      await api.post('/files', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
      setFile(null);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Upload failed');
    }
  };

  const download = (id) => {
    window.open(`/api/files/${id}/download`, '_blank');
  };

  return (
    <div>
      <h1>Files</h1>

      <form className="inline-form" onSubmit={upload}>
        {error && <div className="error">{error}</div>}
        <input type="file" onChange={(e) => setFile(e.target.files[0])} />
        <button type="submit">Upload</button>
      </form>

      <table className="data-table">
        <thead>
          <tr><th>Filename</th><th>Size</th><th>Type</th><th>Uploaded</th><th></th></tr>
        </thead>
        <tbody>
          {items.map((f) => (
            <tr key={f.id}>
              <td>{f.originalFilename}</td>
              <td>{Math.round(f.sizeBytes / 1024)} KB</td>
              <td>{f.contentType}</td>
              <td>{new Date(f.uploadedAt).toLocaleString()}</td>
              <td><button onClick={() => download(f.id)}>Download</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
