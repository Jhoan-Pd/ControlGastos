import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import Dashboard from '../pages/Dashboard';
import Expenses from '../pages/Expenses';
import Income from '../pages/Income';
import Categories from '../pages/Categories';
import Budgets from '../pages/Budgets';
import Login from '../pages/Login';

// Redirige al login si no hay sesión activa
function RutaProtegida({ children }: { children: JSX.Element }) {
  const { usuario } = useAuth();
  return usuario ? children : <Navigate to="/login" replace />;
}

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={
        <RutaProtegida>
          <PaginaConLayout><Dashboard /></PaginaConLayout>
        </RutaProtegida>
      } />
      <Route path="/expenses" element={
        <RutaProtegida>
          <PaginaConLayout><Expenses /></PaginaConLayout>
        </RutaProtegida>
      } />
      <Route path="/income" element={
        <RutaProtegida>
          <PaginaConLayout><Income /></PaginaConLayout>
        </RutaProtegida>
      } />
      <Route path="/categories" element={
        <RutaProtegida>
          <PaginaConLayout><Categories /></PaginaConLayout>
        </RutaProtegida>
      } />
      <Route path="/budgets" element={
        <RutaProtegida>
          <PaginaConLayout><Budgets /></PaginaConLayout>
        </RutaProtegida>
      } />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

function PaginaConLayout({ children }: { children: JSX.Element }) {
  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <Navbar />
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>
      <footer className="bg-white border-t border-gray-200 py-3 text-center text-xs text-gray-400">
        Control de Gastos Personales
      </footer>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <Router>
        <AppRoutes />
      </Router>
    </AuthProvider>
  );
}
