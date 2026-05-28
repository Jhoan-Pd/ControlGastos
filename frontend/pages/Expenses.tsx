import { useState, useEffect, FormEvent } from 'react';
import { PlusIcon, XMarkIcon } from '@heroicons/react/24/outline';
import { format } from 'date-fns';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

interface Gasto {
  id: number;
  monto: number;
  fecha: string;
  esFijo: boolean;
  categoria?: { id: number; nombre: string };
}

interface CategoriaPlana {
  id: number;
  nombre: string;
}

const hoy = new Date().toISOString().split('T')[0];

export default function Expenses() {
  const { usuario } = useAuth();
  const [gastos, setGastos] = useState<Gasto[]>([]);
  const [categorias, setCategorias] = useState<CategoriaPlana[]>([]);
  const [loading, setLoading] = useState(true);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [guardando, setGuardando] = useState(false);
  const [form, setForm] = useState({ monto: '', fecha: hoy, categoriaId: '', esFijo: false });

  const cargar = async () => {
    if (!usuario) return;
    try {
      const [rGastos, rCats] = await Promise.all([
        api.get(`/gastos/usuario/${usuario.id}`),
        api.get('/categorias'),
      ]);
      setGastos(rGastos.data);
      // Aplanar árbol de categorías para el select
      const planas: CategoriaPlana[] = [];
      for (const c of rCats.data) {
        planas.push({ id: c.id, nombre: c.nombre });
        for (const s of c.subcategorias ?? []) {
          planas.push({ id: s.id, nombre: `${c.nombre} › ${s.nombre}` });
        }
      }
      setCategorias(planas);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { cargar(); }, [usuario]);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!usuario) return;
    setGuardando(true);
    try {
      await api.post('/gastos', {
        usuarioId: usuario.id,
        monto: parseFloat(form.monto),
        fecha: form.fecha,
        esActiva: true,
        esFijo: form.esFijo,
        categoriaId: form.categoriaId ? parseInt(form.categoriaId) : null,
      });
      setForm({ monto: '', fecha: hoy, categoriaId: '', esFijo: false });
      setMostrarForm(false);
      cargar();
    } finally {
      setGuardando(false);
    }
  };

  const fmt = (n: number) =>
    Number(n).toLocaleString('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 });

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Gastos</h1>
        <button
          onClick={() => setMostrarForm(!mostrarForm)}
          className="flex items-center gap-1.5 bg-teal-700 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-teal-800 transition-colors"
        >
          {mostrarForm ? <XMarkIcon className="h-4 w-4" /> : <PlusIcon className="h-4 w-4" />}
          {mostrarForm ? 'Cancelar' : 'Agregar gasto'}
        </button>
      </div>

      {/* Formulario */}
      {mostrarForm && (
        <form onSubmit={handleSubmit} className="bg-white rounded-xl border border-gray-200 p-5 mb-6 grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">Monto *</label>
            <input type="number" step="0.01" min="0" required
              value={form.monto} onChange={(e) => setForm({ ...form, monto: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
              placeholder="0.00" />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">Fecha *</label>
            <input type="date" required
              value={form.fecha} onChange={(e) => setForm({ ...form, fecha: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500" />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">Categoría</label>
            <select value={form.categoriaId} onChange={(e) => setForm({ ...form, categoriaId: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500">
              <option value="">Sin categoría</option>
              {categorias.map((c) => <option key={c.id} value={c.id}>{c.nombre}</option>)}
            </select>
          </div>
          <div className="flex items-center gap-2 pt-5">
            <input type="checkbox" id="esFijo" checked={form.esFijo}
              onChange={(e) => setForm({ ...form, esFijo: e.target.checked })}
              className="h-4 w-4 text-teal-600 border-gray-300 rounded" />
            <label htmlFor="esFijo" className="text-sm text-gray-700">Gasto fijo (recurrente)</label>
          </div>
          <div className="sm:col-span-2 flex justify-end">
            <button type="submit" disabled={guardando}
              className="bg-teal-700 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-teal-800 transition-colors disabled:opacity-50">
              {guardando ? 'Guardando...' : 'Guardar gasto'}
            </button>
          </div>
        </form>
      )}

      {/* Tabla */}
      <div className="bg-white shadow-sm rounded-xl overflow-hidden border border-gray-200">
        {loading ? (
          <div className="py-12 flex justify-center">
            <div className="animate-spin rounded-full h-7 w-7 border-b-2 border-teal-700" />
          </div>
        ) : (
          <table className="min-w-full divide-y divide-gray-100">
            <thead className="bg-gray-50">
              <tr>
                {['Fecha', 'Monto', 'Categoría', 'Tipo'].map((h) => (
                  <th key={h} className="px-5 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {gastos.length === 0 ? (
                <tr><td colSpan={4} className="px-5 py-10 text-center text-gray-400 text-sm">No hay gastos registrados</td></tr>
              ) : gastos.map((g) => (
                <tr key={g.id} className="hover:bg-gray-50 transition-colors">
                  <td className="px-5 py-3 text-sm text-gray-700">{format(new Date(g.fecha + 'T00:00:00'), 'dd/MM/yyyy')}</td>
                  <td className="px-5 py-3 text-sm font-semibold text-red-600">{fmt(g.monto)}</td>
                  <td className="px-5 py-3 text-sm text-gray-600">{g.categoria?.nombre ?? <span className="text-gray-400">Sin categoría</span>}</td>
                  <td className="px-5 py-3">
                    <span className={`inline-flex px-2 py-0.5 rounded-full text-xs font-medium ${g.esFijo ? 'bg-blue-100 text-blue-700' : 'bg-gray-100 text-gray-600'}`}>
                      {g.esFijo ? 'Fijo' : 'Variable'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
