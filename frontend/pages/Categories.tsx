import { useState, useEffect, FormEvent } from 'react';
import { PlusIcon, XMarkIcon, FolderIcon, FolderOpenIcon } from '@heroicons/react/24/outline';
import { api } from '../services/api';

interface Categoria {
  id: number;
  nombre: string;
  esGlobal: boolean;
  subcategorias: Categoria[];
}

export default function Categories() {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [loading, setLoading] = useState(true);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [guardando, setGuardando] = useState(false);
  const [form, setForm] = useState({ nombre: '', esGlobal: false, padreId: '' });

  const cargar = async () => {
    try {
      const res = await api.get('/categorias');
      setCategorias(res.data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { cargar(); }, []);

  // Lista plana de categorías raíz para el selector de padre
  const raices = categorias.map((c) => ({ id: c.id, nombre: c.nombre }));

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setGuardando(true);
    try {
      await api.post('/categorias', {
        nombre: form.nombre,
        esGlobal: form.esGlobal,
        padreId: form.padreId ? parseInt(form.padreId) : null,
      });
      setForm({ nombre: '', esGlobal: false, padreId: '' });
      setMostrarForm(false);
      cargar();
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Categorías</h1>
        <button
          onClick={() => setMostrarForm(!mostrarForm)}
          className="flex items-center gap-1.5 bg-teal-700 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-teal-800 transition-colors"
        >
          {mostrarForm ? <XMarkIcon className="h-4 w-4" /> : <PlusIcon className="h-4 w-4" />}
          {mostrarForm ? 'Cancelar' : 'Agregar categoría'}
        </button>
      </div>

      {/* Formulario */}
      {mostrarForm && (
        <form onSubmit={handleSubmit} className="bg-white rounded-xl border border-gray-200 p-5 mb-6 grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">Nombre *</label>
            <input type="text" required
              value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
              placeholder="Ej: Alimentación" />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">Categoría padre (opcional)</label>
            <select value={form.padreId} onChange={(e) => setForm({ ...form, padreId: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500">
              <option value="">Sin padre (categoría raíz)</option>
              {raices.map((r) => <option key={r.id} value={r.id}>{r.nombre}</option>)}
            </select>
          </div>
          <div className="flex items-center gap-2">
            <input type="checkbox" id="esGlobal" checked={form.esGlobal}
              onChange={(e) => setForm({ ...form, esGlobal: e.target.checked })}
              className="h-4 w-4 text-teal-600 border-gray-300 rounded" />
            <label htmlFor="esGlobal" className="text-sm text-gray-700">Categoría global (visible para todos)</label>
          </div>
          <div className="flex justify-end items-end">
            <button type="submit" disabled={guardando}
              className="bg-teal-700 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-teal-800 transition-colors disabled:opacity-50">
              {guardando ? 'Guardando...' : 'Guardar categoría'}
            </button>
          </div>
        </form>
      )}

      {/* Grid */}
      {loading ? (
        <div className="py-12 flex justify-center">
          <div className="animate-spin rounded-full h-7 w-7 border-b-2 border-teal-700" />
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {categorias.length === 0 ? (
            <p className="col-span-full text-center text-gray-400 py-10">No hay categorías registradas</p>
          ) : categorias.map((cat) => (
            <div key={cat.id} className="bg-white rounded-xl border border-gray-200 p-5">
              <div className="flex items-center gap-2 mb-2">
                {cat.subcategorias.length > 0
                  ? <FolderOpenIcon className="h-5 w-5 text-teal-600 flex-shrink-0" />
                  : <FolderIcon className="h-5 w-5 text-gray-400 flex-shrink-0" />}
                <span className="font-semibold text-gray-800 text-sm">{cat.nombre}</span>
              </div>
              <span className={`inline-block text-xs px-2 py-0.5 rounded-full font-medium ${cat.esGlobal ? 'bg-teal-100 text-teal-700' : 'bg-gray-100 text-gray-600'}`}>
                {cat.esGlobal ? 'Global' : 'Personal'}
              </span>
              {cat.subcategorias.length > 0 && (
                <ul className="mt-3 space-y-1 border-t border-gray-100 pt-3">
                  {cat.subcategorias.map((sub) => (
                    <li key={sub.id} className="flex items-center gap-1.5 text-sm text-gray-600">
                      <span className="w-1.5 h-1.5 rounded-full bg-teal-400 flex-shrink-0" />
                      {sub.nombre}
                    </li>
                  ))}
                </ul>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
