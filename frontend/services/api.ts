import axios from 'axios';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  timeout: 15000, // 15s: Render free tier puede tardar en despertar
  headers: {
    'Content-Type': 'application/json'
  }
});
