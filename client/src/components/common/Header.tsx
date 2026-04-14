"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useAuth } from "@/features/auth/context/AuthContext";

export default function Header() {
  const router = useRouter();
  const { isLoggedIn, user, loginAs, logout } = useAuth();

  const handleMockLogin = (role: "ADMIN" | "USER" | "CURATOR") => {
    loginAs(role);
    router.refresh();
  };

  const handleLogout = () => {
    logout();
    router.push("/");
    router.refresh();
  };

  return (
    <header className="sticky top-0 z-50 border-b border-slate-200 bg-white/90 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
        <div>
          <Link href="/" className="text-lg font-bold tracking-tight">
            FanBook
          </Link>
          <p className="text-xs text-slate-500">
            Influencer PhotoBook Goods Platform
          </p>
        </div>

        <nav className="hidden gap-6 text-sm font-medium md:flex">
          <Link href="/projects" className="transition hover:text-slate-600">
            Projects
          </Link>

          {isLoggedIn && (
            <Link
              href="/my/uploads"
              className="transition hover:text-slate-600"
            >
              My Uploads
            </Link>
          )}
        </nav>

        <div className="flex items-center gap-3">
          {isLoggedIn ? (
            <>
              <div className="hidden text-sm text-slate-500 md:block">
                {user?.nickname} ({user?.role})
              </div>

              <Link
                href="/my/uploads"
                className="rounded-full border border-slate-300 px-4 py-2 text-sm font-medium transition hover:bg-slate-50"
              >
                마이페이지
              </Link>

              <button
                onClick={handleLogout}
                className="rounded-full bg-slate-900 px-4 py-2 text-sm font-medium text-white transition hover:bg-slate-700"
              >
                로그아웃
              </button>
            </>
          ) : (
            <div className="flex items-center gap-2">
              <button
                onClick={() => handleMockLogin("ADMIN")}
                className="rounded-full bg-slate-900 px-4 py-2 text-sm font-medium text-white transition hover:bg-slate-700"
              >
                관리자 로그인
              </button>

              <button
                onClick={() => handleMockLogin("USER")}
                className="rounded-full border border-slate-300 px-4 py-2 text-sm font-medium transition hover:bg-slate-50"
              >
                유저 로그인
              </button>

              <button
                onClick={() => handleMockLogin("CURATOR")}
                className="rounded-full border border-slate-300 px-4 py-2 text-sm font-medium transition hover:bg-slate-50"
              >
                큐레이터 로그인
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
}