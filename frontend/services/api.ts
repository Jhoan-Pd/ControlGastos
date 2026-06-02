import axios from 'axios';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  timeout: 90000, // 90s: Render free tier puede tardar hasta 60s en despertar tras inactividad
  headers: {
    'Content-Type': 'application/json'
  }
});
