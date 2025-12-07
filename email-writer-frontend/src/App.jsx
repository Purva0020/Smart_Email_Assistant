import React, { useState } from 'react';
import { Copy, Mail, Sparkles, Loader2 } from 'lucide-react';
import axios from 'axios';
import './App.css';

export default function App() {
  const [emailContent, setEmailContent] = useState('');
  const [tone, setTone] = useState('');
  const [generatedReply, setGeneratedReply] = useState('');
  const [loading, setLoading] = useState(false);
  const [copied, setCopied] = useState(false);

  const toneOptions = [
    { value: '', label: 'None' },
    { value: 'Professional', label: 'Professional' },
    { value: 'Casual', label: 'Casual' },
    { value: 'Friendly', label: 'Friendly' }
  ];

  const handleSubmit = async () => {
    setLoading(true);
    try {
      const response = await axios.post("http://localhost:8080/api/email/generate", {
        emailContent,
        tone
      });
      setGeneratedReply(typeof response.data === 'string' 
        ? response.data 
        : JSON.stringify(response.data)
      );
    } catch (error) {
      setGeneratedReply('Error generating reply. Please try again.');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const copyToClipboard = () => {
    navigator.clipboard.writeText(generatedReply);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="app-container">
      <div className="app-wrapper">
        <div className="header">
          <div className="icon-badge">
            <Mail className="icon-mail" />
          </div>
          <h1 className="title">Email Reply Generator</h1>
          <p className="subtitle">Generate professional email responses instantly</p>
        </div>

        <div className="main-card">
          <div className="card-content">
            <div className="form-group">
              <label className="form-label">Original Email Content</label>
              <textarea
                value={emailContent}
                onChange={(e) => setEmailContent(e.target.value)}
                placeholder="Paste the email you want to reply to..."
                className="textarea-input"
              />
            </div>

            <div className="form-group">
              <label className="form-label">Tone (Optional)</label>
              <select
                value={tone}
                onChange={(e) => setTone(e.target.value)}
                className="select-input"
              >
                {toneOptions.map(option => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>

            <button
              onClick={handleSubmit}
              disabled={!emailContent || loading}
              className={`btn-generate ${(!emailContent || loading) ? 'btn-disabled' : ''}`}
            >
              {loading ? (
                <>
                  <Loader2 className="icon-spinner" />
                  Generating...
                </>
              ) : (
                <>
                  <Sparkles className="icon-sparkles" />
                  Generate Reply
                </>
              )}
            </button>

            {generatedReply && (
              <div className="form-group reply-section">
                <label className="form-label">Generated Reply</label>
                <textarea
                  value={generatedReply}
                  readOnly
                  className="textarea-output"
                />
              </div>
            )}

            {generatedReply && (
              <button onClick={copyToClipboard} className="btn-copy">
                <Copy className="icon-copy" />
                {copied ? 'Copied!' : 'Copy to Clipboard'}
              </button>
            )}
          </div>
        </div>

        <div className="footer">
          <p>Powered by AI • Generate professional email responses effortlessly</p>
        </div>
      </div>
    </div>
  );
}