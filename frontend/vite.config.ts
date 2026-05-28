import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  root: 'src',
  // envDir relativo a root ('src'), '..' apunta a frontend/ donde viven los .env
  envDir: '..',
  server: {
    port: 3001
  },
  build: {
    outDir: '../dist',
  },
});
