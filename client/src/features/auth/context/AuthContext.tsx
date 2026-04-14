"use client";

import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";

export type UserRole = "ADMIN" | "USER" | "CURATOR";

export type MockUser = {
  id: number;
  nickname: string;
  email: string;
  role: UserRole;
};

type AuthContextType = {
  isLoggedIn: boolean;
  user: MockUser | null;
  loginAs: (role: UserRole) => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

const MOCK_USERS: Record<UserRole, MockUser> = {
  ADMIN: {
    id: 1,
    nickname: "admin",
    email: "admin@sweet-hjr.com",
    role: "ADMIN",
  },
  USER: {
    id: 2,
    nickname: "fan_user",
    email: "user@sweet-hjr.com",
    role: "USER",
  },
  CURATOR: {
    id: 3,
    nickname: "curator",
    email: "curator@sweet-hjr.com",
    role: "CURATOR",
  },
};

const STORAGE_KEY = "fanbook_mock_auth";

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<MockUser | null>(null);
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    const savedRole = window.localStorage.getItem(STORAGE_KEY) as UserRole | null;

    if (savedRole && MOCK_USERS[savedRole]) {
      setUser(MOCK_USERS[savedRole]);
    }

    setIsReady(true);
  }, []);

  const loginAs = (role: UserRole) => {
    window.localStorage.setItem(STORAGE_KEY, role);
    setUser(MOCK_USERS[role]);
  };

  const logout = () => {
    window.localStorage.removeItem(STORAGE_KEY);
    setUser(null);
  };

  const value = useMemo(
    () => ({
      isLoggedIn: !!user,
      user,
      loginAs,
      logout,
    }),
    [user]
  );

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