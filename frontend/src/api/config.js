// src/api/config.js
// Centralized API base URL.
// Locally it falls back to http://localhost:5000 (your backend port).
// In production (Vercel/Netlify), set VITE_API_URL as an environment variable
// to your deployed backend URL, e.g. https://your-backend.onrender.com

export const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8081";
