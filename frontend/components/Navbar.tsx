import { Link, useLocation, useNavigate } from 'react-router-dom';
import {
  HomeIcon,
  CurrencyDollarIcon,
  ArrowTrendingUpIcon,
  FolderIcon,
  ChartPieIcon,
  ArrowRightOnRectangleIcon,
  UserCircleIcon,
} from '@heroicons/react/24/outline';
import { useAuth } from '../context/AuthContext';

const navigation = [
  { name: 'Dashboard', href: '/', icon: HomeIcon },
  { name: 'Gastos', href: '/expenses', icon: CurrencyDollarIcon },
  { name: 'Ingresos', href: '/income', icon: ArrowTrendingUpIcon },
  { name: 'Categorías', href: '/categories', icon: FolderIcon },
  { name: 'Presupuestos', href: '/budgets', icon: ChartPieIcon },
];

export default function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();
  const { usuario, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="bg-teal-700 shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <div className="flex items-center gap-2 flex-shrink-0">
            <CurrencyDollarIcon className="h-6 w-6 text-white" />
            <span className="text-white font-bold text-base tracking-wide hidden sm:block">
              Control de Gastos
            </span>
          </div>

          {/* Navegación central */}
          <div className="flex items-center gap-0.5">
            {navigation.map((item) => {
              const activo = location.pathname === item.href;
              return (
                <Link
                  key={item.name}
                  to={item.href}
                  className={`flex items-center gap-1.5 px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                    activo
                      ? 'bg-teal-900 text-white'
                      : 'text-teal-100 hover:bg-teal-600 hover:text-white'
                  }`}
                >
                  <item.icon className="h-4 w-4 flex-shrink-0" />
                  <span className="hidden md:block">{item.name}</span>
                </Link>
              );
            })}
          </div>

          {/* Usuario + logout */}
          {usuario && (
            <div className="flex items-center gap-2">
              <div className="flex items-center gap-1.5 text-teal-100 text-sm">
                <UserCircleIcon className="h-5 w-5" />
                <span className="hidden sm:block font-medium">{usuario.nombre}</span>
              </div>
              <button
                onClick={handleLogout}
                title="Cerrar sesión"
                className="flex items-center gap-1 px-2 py-2 rounded-md text-teal-100 hover:bg-teal-600 hover:text-white transition-colors"
              >
                <ArrowRightOnRectangleIcon className="h-5 w-5" />
              </button>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
