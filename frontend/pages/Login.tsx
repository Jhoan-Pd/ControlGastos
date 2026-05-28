import { useState, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { CurrencyDollarIcon } from '@heroicons/react/24/outline';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

type Modo = 'login' | 'registro';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [modo, setModo] = useState<Modo>('login');
  const [form, setForm] = useState({ nombre: '', email: '', contrasena: '' });
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setCargando(true);
    try {
      if (modo === 'login') {
        const res = await api.post('/usuarios/login', {
          email: form.email,
          contrasena: form.contrasena,
        });
        login(res.data);
        navigate('/');
      } else {
        const res = await api.post('/usuarios', {
          nombre: form.nombre,
          email: form.email,
          contrasena: form.contrasena,
        });
        login(res.data);
        navigate('/');
      }
    } catch {
      setError(
        modo === 'login'
          ? 'Correo o contraseña incorrectos.'
          : 'No se pudo crear la cuenta. El correo puede estar en uso.'
      );
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
      <div className="bg-white rounded-2xl shadow-lg w-full max-w-md p-8">
        {/* Logo */}
        <div className="flex flex-col items-center mb-8">
          <div className="bg-teal-700 rounded-full p-3 mb-3">
            <CurrencyDollarIcon className="h-8 w-8 text-white" />
          </div>
          <h1 className="text-2xl font-bold text-gray-800">Control de Gastos</h1>
          <p className="text-sm text-gray-500 mt-1">Gestiona tus finanzas personales</p>
        </div>

        {/* Tabs */}
        <div className="flex rounded-lg bg-gray-100 p-1 mb-6">
          {(['login', 'registro'] as Modo[]).map((m) => (
            <button
              key={m}
              type="button"
              onClick={() => { setModo(m); setError(''); }}
              className={`flex-1 py-2 text-sm font-medium rounded-md transition-colors ${
                modo === m
                  ? 'bg-white text-teal-700 shadow'
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              {m === 'login' ? 'Iniciar sesión' : 'Registrarse'}
            </button>
          ))}
        </div>

        {/* Formulario */}
        <form onSubmit={handleSubmit} className="space-y-4">
          {modo === 'registro' && (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
              <input
                type="text"
                required
                value={form.nombre}
                onChange={(e) => setForm({ ...form, nombre: e.target.value })}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
                placeholder="Tu nombre completo"
              />
            </div>
          )}

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Correo electrónico</label>
            <input
              type="email"
              required
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
              placeholder="correo@ejemplo.com"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Contraseña</label>
            <input
              type="password"
              required
              value={form.contrasena}
              onChange={(e) => setForm({ ...form, contrasena: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
              placeholder="••••••••"
            />
          </div>

          {error && (
            <p className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
              {error}
            </p>
          )}

          <button
            type="submit"
            disabled={cargando}
            className="w-full bg-teal-700 text-white py-2.5 rounded-lg font-medium text-sm hover:bg-teal-800 transition-colors disabled:opacity-50"
          >
            {cargando
              ? 'Cargando...'
              : modo === 'login'
              ? 'Iniciar sesión'
              : 'Crear cuenta'}
          </button>
        </form>
      </div>
    </div>
  );
}
