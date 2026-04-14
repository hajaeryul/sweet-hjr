"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { useAuth } from "@/features/auth/context/AuthContext";
import { getMyUploads } from "@/features/upload/api/getMyUploads";
import { MyFanUpload } from "@/features/upload/types/upload";

const BASE_URL = "http://localhost:8080";

export default function MyUploadsPage() {
  const { isLoggedIn, user } = useAuth();
  const [myUploads, setMyUploads] = useState<MyFanUpload[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    if (!isLoggedIn || !user) {
      setMyUploads([]);
      setIsLoading(false);
      return;
    }

    const fetchMyUploads = async () => {
      try {
        setIsLoading(true);
        setErrorMessage("");

        const result = await getMyUploads(user.id);
        setMyUploads(result);
      } catch (error) {
        console.error(error);
        setErrorMessage("내 업로드 목록을 불러오는 중 문제가 발생했습니다.");
      } finally {
        setIsLoading(false);
      }
    };

    fetchMyUploads();
  }, [isLoggedIn, user]);

  if (!isLoggedIn || !user) {
    return (
      <main className="mx-auto max-w-4xl px-6 py-16">
        <section className="rounded-[2rem] border border-slate-200 bg-white p-10 text-center shadow-sm">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
            my uploads
          </p>
          <h1 className="mt-3 text-3xl font-bold tracking-tight">
            로그인이 필요합니다
          </h1>
          <p className="mt-3 text-sm leading-6 text-slate-600">
            마이페이지는 로그인한 사용자만 접근할 수 있습니다.
          </p>

          <Link
            href="/projects"
            className="mt-6 inline-flex rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-slate-50"
          >
            프로젝트 보러 가기
          </Link>
        </section>
      </main>
    );
  }

  if (isLoading) {
    return (
      <main className="mx-auto max-w-7xl px-6 py-16">
        <section className="rounded-[2rem] border border-slate-200 bg-white p-10 text-center shadow-sm">
          <p className="text-lg font-semibold text-slate-800">
            내 업로드 목록을 불러오는 중입니다.
          </p>
        </section>
      </main>
    );
  }

  if (errorMessage) {
    return (
      <main className="mx-auto max-w-7xl px-6 py-16">
        <section className="rounded-[2rem] border border-slate-200 bg-white p-10 text-center shadow-sm">
          <p className="text-lg font-semibold text-slate-800">{errorMessage}</p>
          <Link
            href="/projects"
            className="mt-6 inline-flex rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-slate-50"
          >
            프로젝트 보러 가기
          </Link>
        </section>
      </main>
    );
  }

  return (
    <main className="mx-auto max-w-7xl px-6 py-16">
      <section className="flex flex-col gap-3">
        <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
          my uploads
        </p>
        <h1 className="text-3xl font-bold tracking-tight md:text-4xl">
          내가 올린 사진
        </h1>
        <p className="max-w-2xl text-sm leading-6 text-slate-600 md:text-base">
          내가 업로드한 프로젝트별 사진 목록과 현재 상태를 확인할 수 있습니다.
        </p>
        <p className="text-sm text-slate-500">
          현재 사용자: {user.nickname} ({user.role})
        </p>
      </section>

      {myUploads.length === 0 ? (
        <section className="mt-10 rounded-[2rem] border border-dashed border-slate-300 p-10 text-center">
          <p className="text-lg font-semibold text-slate-800">
            아직 업로드한 사진이 없습니다.
          </p>
          <p className="mt-2 text-sm text-slate-500">
            프로젝트에 참여해서 사진을 업로드하면 이곳에 표시됩니다.
          </p>

          <Link
            href="/projects"
            className="mt-6 inline-flex rounded-full bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700"
          >
            프로젝트 보러 가기
          </Link>
        </section>
      ) : (
        <section className="mt-10 space-y-10">
          {myUploads.map((upload) => (
            <article
              key={upload.fanUploadId}
              className="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm"
            >
              <div className="flex flex-col gap-4 border-b border-slate-100 pb-5 md:flex-row md:items-start md:justify-between">
                <div>
                  <p className="text-sm font-semibold text-slate-500">
                    프로젝트
                  </p>
                  <Link
                    href={`/projects/${upload.projectId}`}
                    className="mt-2 inline-block text-2xl font-bold tracking-tight text-slate-900 transition hover:text-slate-600"
                  >
                    {upload.projectTitle}
                  </Link>

                  <div className="mt-4 flex flex-wrap items-center gap-3">
                    <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700">
                      {getUploadStatusLabel(upload.status)}
                    </span>

                    <span className="text-sm text-slate-500">
                      업로드일: {formatDateTime(upload.createdAt)}
                    </span>

                    <span className="text-sm text-slate-500">
                      총 {upload.files.length}장
                    </span>
                  </div>

                  {upload.memo && (
                    <p className="mt-4 text-sm leading-6 text-slate-600">
                      메모: {upload.memo}
                    </p>
                  )}
                </div>

                <div>
                  <Link
                    href={`/projects/${upload.projectId}`}
                    className="inline-flex rounded-full border border-slate-300 px-4 py-2 text-sm font-semibold transition hover:bg-slate-50"
                  >
                    프로젝트 보기
                  </Link>
                </div>
              </div>

              {upload.files.length === 0 ? (
                <div className="mt-6 rounded-2xl bg-slate-50 p-6 text-sm text-slate-500">
                  업로드된 파일이 없습니다.
                </div>
              ) : (
                <div className="mt-6 grid gap-5 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                  {upload.files.map((file) => (
                    <article
                      key={file.fileId}
                      className="overflow-hidden rounded-[1.5rem] border border-slate-200 bg-white shadow-sm"
                    >
                      <div className="aspect-[4/5] overflow-hidden bg-slate-100">
                        <img
                          src={`${BASE_URL}${file.fileUrl}`}
                          alt={file.originalFileName}
                          className="h-full w-full object-cover"
                        />
                      </div>

                      <div className="p-4">
                        <p className="truncate text-sm font-semibold text-slate-900">
                          {file.originalFileName}
                        </p>

                        <div className="mt-3 space-y-1 text-xs text-slate-500">
                          <p>크기: {formatFileSize(file.fileSize)}</p>
                          <p>
                            해상도: {file.width} × {file.height}
                          </p>
                          <p>{file.mimeType}</p>
                        </div>
                      </div>
                    </article>
                  ))}
                </div>
              )}
            </article>
          ))}
        </section>
      )}
    </main>
  );
}

function getUploadStatusLabel(
  status: "PENDING" | "FILTERED" | "APPROVED" | "REJECTED"
) {
  switch (status) {
    case "PENDING":
      return "검토 대기 중";
    case "FILTERED":
      return "필터링 완료";
    case "APPROVED":
      return "선정됨";
    case "REJECTED":
      return "반려됨";
    default:
      return "상태 확인 필요";
  }
}

function formatDateTime(dateString: string) {
  const date = new Date(dateString);

  if (Number.isNaN(date.getTime())) {
    return dateString;
  }

  return date.toLocaleString("ko-KR");
}

function formatFileSize(bytes: number) {
  if (!bytes || Number.isNaN(bytes)) {
    return "0 B";
  }

  const units = ["B", "KB", "MB", "GB"];
  let size = bytes;
  let unitIndex = 0;

  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024;
    unitIndex += 1;
  }

  return `${size.toFixed(unitIndex === 0 ? 0 : 1)} ${units[unitIndex]}`;
}