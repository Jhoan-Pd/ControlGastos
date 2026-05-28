import { useState, useEffect, FormEvent } from 'react';
import { PlusIcon, XMarkIcon, ChartPieIcon } from '@heroicons/react/24/outline';
import { format } from 'date-fns';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

interface Presupuesto {
  id: number;
  descripcion: string;
  saldoDisponible: number;
  fechaInicio: string;
  fechaFin: string;
}

const hoy = new Date().toISOString().split('T')[0];

export default function Budgets() {
  const { usuario } = useAuth();
  const [presupuestos, setPresupuestos] = useState<Presupuesto[]>([]);
  const [loading, setLoading] = useState(true);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [guardando, setGuardando] = useState(false);
  const [form, setForm] = useState({ descripcion: '', saldoDisponible: '', fechaInicio: hoy, fechaFin: '' });

  const cargar = async () => {
    if (!usuario) return;
    try {
      const res = await api.get(`/presupuestos/usuario/${usuario.id}`);
      setPresupuestos(res.data);
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
      await api.post('/presupuestos', {
        usuarioId: usuario.id,
        descripcion: form.descripcion,
        saldoDisponible: parseFloat(form.saldoDisponible),
        fechaInicio: form.fechaInicio,
        fechaFin: form.fechaFin,
      });
      setForm({ descripcion: '', saldoDisponible: '', fechaInicio: hoy, fechaFin: '' });
      setMostrarForm(false);
      cargar();
    } finally {
      setGuardando(false);
    }
  };

  const fmt = (n: number) =>
    Number(n).toLocaleString('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 });

  const fmtFecha = (f: string) => format(new Date(f + 'T00:00:00'), 'dd/MM/yyyy');

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Presupuestos</h1>
        <button
          onClick={() => setMostrarForm(!mostrarForm)}
          className="flex items-center gap-1.5 bg-teal-700 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-teal-800 transition-colors"
        >
          {mostrarForm ? <XMarkIcon className="h-4 w-4" /> : <PlusIcon className="h-4 w-4" />}
          {mostrarForm ? 'Cancelar' : 'Agregar presupuesto'}
        </button>
      </div>

      {/* Formulario */}
      {mostrarForm && (
        <form onSubmit={handleSubmit} className="bg-white rounded-xl border border-gray-200 p-5 mb-6 grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div className="sm:col-span-2">
            <label className="block text-xs font-medium text-gray-600 mb-1">Descripción *</label>
            <input type="text" required
              value={form.descripcion} onChange={(e) => setForm({ ...form, descripcion: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
              placeholder="Ej: Presupuesto mensual mayo" />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">Saldo disponible *</label>
            <input type="number" step="0.01" min="0" required
              value={form.saldoDisponible} onChange={(e) => setForm({ ...form, saldoDisponible: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
              placeholder="0.00" />
          </div>
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-xs font-medium text-gray-600 mb-1">Fecha inicio *</label>
              <input type="date" required
                value={form.fechaInicio} onChange={(e) => setForm({ ...form, fechaInicio: e.target.value })}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500" />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-600 mb-1">Fecha fin *</label>
              <input type="date" required
                value={form.fechaFin} onChange={(e) => setForm({ ...form, fechaFin: e.target.value })}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500" />
            </div>
          </div>
          <div className="sm:col-span-2 flex justify-end">
            <button type="submit" disabled={guardando}
              className="bg-teal-700 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-teal-800 transition-colors disabled:opacity-50">
              {guardando ? 'Guardando...' : 'Guardar presupuesto'}
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
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {presupuestos.length === 0 ? (
            <p className="col-span-full text-center text-gray-400 py-10">No hay presupuestos registrados</p>
          ) : presupuestos.map((p) => (
            <div key={p.id} className="bg-white rounded-xl border border-gray-200 p-5">
              <div className="flex items-start justify-between gap-4">
                <div className="flex items-center gap-2">
                  <div className="bg-teal-100 p-2 rounded-lg">
                    <ChartPieIcon className="h-5 w-5 text-teal-700" />
                  </div>
                  <div>
                    <h2 className="font-semibold text-gray-800 text-sm">{p.descripcion || 'Presupuesto'}</h2>
                    <p className="text-xs text-gray-500 mt-0.5">
                      {fmtFecha(p.fechaInicio)} — {fmtFecha(p.fechaFin)}
                    </p>
                  </div>
                </div>
                <span className="text-lg font-bold text-teal-700 whitespace-nowrap">{fmt(p.saldoDisponible)}</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
