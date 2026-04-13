import "./globals.css";
import Header from "@/components/common/Header";
import { AuthProvider } from "@/features/auth/context/AuthContext";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="ko">
      <body className="bg-white text-slate-900">
        <AuthProvider>
        <Header />
        {children}
        </AuthProvider>
      </body>
    </html>
  );
}