"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { useAuth } from "@/features/auth/context/AuthContext";
import { createFanUpload } from "@/features/upload/api/createFanUpload";
import { uploadFiles } from "@/features/upload/api/uploadFiles";
import { completeFanUpload } from "@/features/upload/api/completeFanUpload";

type PageProps = {
  params: {
    projectId: string;
  };
};

export default function UploadPage({ params }: PageProps) {
  const projectId = params.projectId;
  const { isLoggedIn, user } = useAuth();

  const [memo, setMemo] = useState("");
  const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
  const [fanUploadId, setFanUploadId] = useState<number | null>(null);
  const [isCreatingSession, setIsCreatingSession] = useState(false);
  const [isUploading, setIsUploading] = useState(false);
  const [isCompleting, setIsCompleting] = useState(false);
  const [message, setMessage] = useState("");

  const previews = useMemo(() => {
    return selectedFiles.map((file) => ({
      file,
      previewUrl: URL.createObjectURL(file),
    }));
  }, [selectedFiles]);

  useEffect(() => {
    return () => {
      previews.forEach(({ previewUrl }) => URL.revokeObjectURL(previewUrl));
    };
  }, [previews]);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(event.target.files ?? []);
    setSelectedFiles(files);
  };

  const handleCreateSession = async () => {
    if (!isLoggedIn || !user) {
      setMessage("로그인 후 업로드할 수 있습니다.");
      return;
    }

    try {
      setIsCreatingSession(true);
      setMessage("");

      const result = await createFanUpload(projectId, {
        userId: user.id,
        memo,
      });

      setFanUploadId(result.fanUploadId);
      setMessage("업로드 세션이 생성되었습니다.");
    } catch (error) {
      console.error(error);
      setMessage("업로드 세션 생성 중 문제가 발생했습니다.");
    } finally {
      setIsCreatingSession(false);
    }
  };

  const handleUploadFiles = async () => {
    if (!isLoggedIn || !user) {
      setMessage("로그인 후 업로드할 수 있습니다.");
      return;
    }

    if (!fanUploadId) {
      setMessage("먼저 업로드 세션을 생성해 주세요.");
      return;
    }

    if (selectedFiles.length === 0) {
      setMessage("업로드할 파일을 선택해 주세요.");
      return;
    }

    try {
      setIsUploading(true);
      setMessage("");

      const result = await uploadFiles(
        projectId,
        fanUploadId,
        user.id,
        selectedFiles
      );

      setMessage(`${result.uploadedCount}개 파일 업로드가 완료되었습니다.`);
    } catch (error) {
      console.error(error);
      setMessage("파일 업로드 중 문제가 발생했습니다.");
    } finally {
      setIsUploading(false);
    }
  };

  const handleCompleteUpload = async () => {
    if (!isLoggedIn || !user) {
      setMessage("로그인 후 업로드할 수 있습니다.");
      return;
    }

    if (!fanUploadId) {
      setMessage("먼저 업로드 세션을 생성해 주세요.");
      return;
    }

    try {
      setIsCompleting(true);
      setMessage("");

      const result = await completeFanUpload(projectId, fanUploadId, user.id);

      setMessage(
        `업로드 완료 처리되었습니다. 현재 상태: ${result.status}, 총 파일 수: ${result.totalFileCount}`
      );
    } catch (error) {
      console.error(error);
      setMessage("업로드 완료 처리 중 문제가 발생했습니다.");
    } finally {
      setIsCompleting(false);
    }
  };

  if (!isLoggedIn || !user) {
    return (
      <main className="mx-auto max-w-4xl px-6 py-16">
        <div className="rounded-[2rem] border border-slate-200 bg-white p-10 text-center shadow-sm">
          <h1 className="text-2xl font-bold">로그인이 필요합니다.</h1>
          <p className="mt-3 text-sm text-slate-500">
            사진 업로드는 로그인 후 진행할 수 있습니다.
          </p>

          <Link
            href={`/projects/${projectId}`}
            className="mt-6 inline-flex rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-slate-50"
          >
            프로젝트로 돌아가기
          </Link>
        </div>
      </main>
    );
  }

  return (
    <main className="mx-auto max-w-7xl px-6 py-16">
      <div className="flex items-center justify-between gap-4">
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
            upload
          </p>
          <h1 className="mt-3 text-3xl font-bold tracking-tight md:text-4xl">
            프로젝트 사진 업로드
          </h1>
          <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-600 md:text-base">
            프로젝트에 참여할 사진을 업로드하세요. 지금은 세션 생성 → 파일 업로드 → 완료 처리
            흐름으로 진행됩니다.
          </p>
          <p className="mt-2 text-sm text-slate-500">
            현재 사용자: {user.nickname} ({user.role})
          </p>
        </div>

        <Link
          href={`/projects/${projectId}`}
          className="rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-slate-50"
        >
          프로젝트로 돌아가기
        </Link>
      </div>

      <section className="mt-10 grid gap-8 lg:grid-cols-[0.9fr_1.1fr]">
        <div className="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="text-xl font-bold">업로드 정보</h2>

          <div className="mt-6">
            <label className="block text-sm font-semibold text-slate-700">
              메모
            </label>
            <textarea
              value={memo}
              onChange={(e) => setMemo(e.target.value)}
              placeholder="예: 잠실 직관 사진 / 방송 캡처 정리본"
              className="mt-2 h-32 w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-slate-500"
            />
          </div>

          <div className="mt-6">
            <label className="block text-sm font-semibold text-slate-700">
              파일 선택
            </label>
            <input
              type="file"
              accept="image/*"
              multiple
              onChange={handleFileChange}
              className="mt-2 block w-full rounded-xl border border-slate-300 px-3 py-2 text-sm"
            />
            <p className="mt-2 text-xs text-slate-500">
              여러 장 선택할 수 있습니다.
            </p>
          </div>

          <div className="mt-8 flex flex-col gap-3">
            <button
              onClick={handleCreateSession}
              disabled={isCreatingSession}
              className="rounded-full bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:bg-slate-400"
            >
              {isCreatingSession ? "세션 생성 중..." : "1. 업로드 세션 생성"}
            </button>

            <button
              onClick={handleUploadFiles}
              disabled={isUploading}
              className="rounded-full bg-slate-700 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-600 disabled:cursor-not-allowed disabled:bg-slate-400"
            >
              {isUploading ? "업로드 중..." : "2. 파일 업로드"}
            </button>

            <button
              onClick={handleCompleteUpload}
              disabled={isCompleting}
              className="rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:bg-slate-100"
            >
              {isCompleting ? "완료 처리 중..." : "3. 업로드 완료"}
            </button>
          </div>

          <div className="mt-6 rounded-2xl bg-slate-50 p-4 text-sm text-slate-600">
            <p>
              현재 업로드 세션 ID:{" "}
              <span className="font-semibold text-slate-900">
                {fanUploadId ?? "아직 없음"}
              </span>
            </p>

            {message && (
              <p className="mt-2 font-medium text-slate-900">{message}</p>
            )}
          </div>
        </div>

        <div className="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="text-xl font-bold">선택한 이미지 미리보기</h2>

          {previews.length === 0 ? (
            <div className="mt-6 rounded-3xl border border-dashed border-slate-300 p-10 text-center">
              <p className="text-lg font-semibold text-slate-700">
                아직 선택한 파일이 없습니다.
              </p>
              <p className="mt-2 text-sm text-slate-500">
                왼쪽 영역에서 이미지를 선택하면 여기에서 미리볼 수 있습니다.
              </p>
            </div>
          ) : (
            <div className="mt-6 grid gap-5 sm:grid-cols-2 xl:grid-cols-3">
              {previews.map(({ file, previewUrl }) => (
                <article
                  key={`${file.name}-${file.size}`}
                  className="overflow-hidden rounded-[1.5rem] border border-slate-200 bg-white shadow-sm"
                >
                  <div className="aspect-[4/5] overflow-hidden bg-slate-100">
                    <img
                      src={previewUrl}
                      alt={file.name}
                      className="h-full w-full object-cover"
                    />
                  </div>

                  <div className="p-4">
                    <p className="truncate text-sm font-semibold text-slate-900">
                      {file.name}
                    </p>

                    <div className="mt-3 space-y-1 text-xs text-slate-500">
                      <p>크기: {formatFileSize(file.size)}</p>
                      <p>타입: {file.type || "알 수 없음"}</p>
                    </div>
                  </div>
                </article>
              ))}
            </div>
          )}
        </div>
      </section>
    </main>
  );
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