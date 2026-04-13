"use client";

import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";

type MockUser = {
  id: number;
  nickname: string;
  email: string;
};

type AuthContextType = {
  isLoggedIn: boolean;
  user: MockUser | null;
  login: () => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

const MOCK_USER: MockUser = {
  id: 1,
  nickname: "admin",
  email: "admin@sweet-hjr.com",
};

const STORAGE_KEY = "fanbook_mock_auth";

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<MockUser | null>(null);
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    const saved = window.localStorage.getItem(STORAGE_KEY);

    if (saved === "logged_in") {
      setUser(MOCK_USER);
    }

    setIsReady(true);
  }, []);

  const login = () => {
    window.localStorage.setItem(STORAGE_KEY, "logged_in");
    setUser(MOCK_USER);
  };

  const logout = () => {
    window.localStorage.removeItem(STORAGE_KEY);
    setUser(null);
  };

  const value = useMemo(
    () => ({
      isLoggedIn: !!user,
      user,
      login,
      logout,
    }),
    [user]
  );

  // hydration mismatch 방지
  if (!isReady) {
    return null;
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }

  return context;
}