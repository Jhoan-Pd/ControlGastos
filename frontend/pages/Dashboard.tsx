import { useState, useEffect } from 'react';
import { ArrowTrendingUpIcon, ArrowTrendingDownIcon, ScaleIcon } from '@heroicons/react/24/outline';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

export default function Dashboard() {
  const { usuario } = useAuth();
  const [resumen, setResumen] = useState({ ingresos: 0, gastos: 0, balance: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!usuario) return;
    const cargar = async () => {
      try {
        setError(null);
        const [rIngresos, rGastos] = await Promise.all([
          api.get(`/ingresos/usuario/${usuario.id}`),
          api.get(`/gastos/usuario/${usuario.id}`),
        ]);
        const ingresos = rIngresos.data.reduce((acc: number, c: any) => acc + Number(c.monto), 0);
        const gastos = rGastos.data.reduce((acc: number, c: any) => acc + Number(c.monto), 0);
        setResumen({ ingresos, gastos, balance: ingresos - gastos });
      } catch {
        setError('No se pudo cargar el resumen. Verifica que el servidor esté activo.');
      } finally {
        setLoading(false);
      }
    };
    cargar();
  }, [usuario]);

  const fmt = (n: number) =>
    n.toLocaleString('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 });

  if (loading) return <Spinner />;

  return (
    <div>
      <h1 className="text-2xl font-bold text-gray-800 mb-2">
        Bienvenido, {usuario?.nombre}
      </h1>
      <p className="text-sm text-gray-500 mb-8">Aquí está el resumen de tus finanzas</p>

      {error && <Alerta mensaje={error} />}

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
        <Tarjeta
          titulo="Ingresos totales"
          valor={fmt(resumen.ingresos)}
          color="green"
          icono={<ArrowTrendingUpIcon className="h-6 w-6" />}
        />
        <Tarjeta
          titulo="Gastos totales"
          valor={fmt(resumen.gastos)}
          color="red"
          icono={<ArrowTrendingDownIcon className="h-6 w-6" />}
        />
        <Tarjeta
          titulo="Balance"
          valor={fmt(resumen.balance)}
          color={resumen.balance >= 0 ? 'blue' : 'red'}
          icono={<ScaleIcon className="h-6 w-6" />}
        />
      </div>
    </div>
  );
}

function Tarjeta({ titulo, valor, color, icono }: {
  titulo: string; valor: string; color: string; icono: JSX.Element;
}) {
  const colores: Record<string, string> = {
    green: 'bg-green-50 text-green-700 border-green-200',
    red: 'bg-red-50 text-red-700 border-red-200',
    blue: 'bg-blue-50 text-blue-700 border-blue-200',
  };
  const iconoColores: Record<string, string> = {
    green: 'bg-green-100 text-green-600',
    red: 'bg-red-100 text-red-600',
    blue: 'bg-blue-100 text-blue-600',
  };
  return (
    <div className={`rounded-xl border p-6 ${colores[color]}`}>
      <div className="flex items-center justify-between mb-3">
        <span className="text-sm font-medium opacity-80">{titulo}</span>
        <div className={`p-2 rounded-lg ${iconoColores[color]}`}>{icono}</div>
      </div>
      <p className="text-2xl font-bold">{valor}</p>
    </div>
  );
}

function Spinner() {
  return (
    <div className="flex justify-center items-center py-20">
      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-teal-700" />
    </div>
  );
}

function Alerta({ mensaje }: { mensaje: string }) {
  return (
    <div className="mb-6 bg-red-50 border border-red-200 rounded-lg p-4 text-sm text-red-700">
      {mensaje}
    </div>
  );
}
