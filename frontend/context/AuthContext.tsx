import { createContext, useContext, useState, ReactNode } from 'react';

interface Usuario {
  id: number;
  nombre: string;
  email: string;
}

interface AuthContextType {
  usuario: Usuario | null;
  login: (u: Usuario) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<Usuario | null>(() => {
    const guardado = localStorage.getItem('usuario_cg');
    return guardado ? JSON.parse(guardado) : null;
  });

  const login = (u: Usuario) => {
    setUsuario(u);
    localStorage.setItem('usuario_cg', JSON.stringify(u));
  };

  const logout = () => {
    setUsuario(null);
    localStorage.removeItem('usuario_cg');
  };

  return (
    <AuthContext.Provider value={{ usuario, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth debe usarse dentro de AuthProvider');
  return ctx;
}
