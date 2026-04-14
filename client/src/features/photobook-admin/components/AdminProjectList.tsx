"use client";

import Link from "next/link";
import { useAuth } from "@/features/auth/context/AuthContext";
import { ProjectSummary } from "@/features/project/types/project";

type Props = {
  projects: ProjectSummary[];
};

export default function AdminProjectList({ projects }: Props) {
  const { user, isLoggedIn } = useAuth();

  if (!isLoggedIn || user?.role !== "ADMIN") {
    return (
      <div className="rounded-2xl border border-rose-200 bg-rose-50 p-8 text-sm text-rose-700 shadow-sm">
        관리자만 접근할 수 있습니다.
      </div>
    );
  }

  if (!projects || projects.length === 0) {
    return (
      <div className="rounded-2xl border border-slate-200 bg-white p-8 text-sm text-slate-600 shadow-sm">
        등록된 프로젝트가 없습니다.
      </div>
    );
  }

  return (
    <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
      {projects.map((project) => (
        <article
          key={project.id}
          className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm"
        >
          {project.coverImageUrl ? (
            <img
              src={project.coverImageUrl}
              alt={project.title}
              className="h-52 w-full object-cover"
            />
          ) : (
            <div className="flex h-52 items-center justify-center bg-slate-100 text-sm text-slate-400">
              썸네일 없음
            </div>
          )}

          <div className="space-y-4 p-5">
            <div>
              <h2 className="line-clamp-2 text-lg font-semibold text-slate-900">
                {project.title}
              </h2>

              <p className="mt-2 line-clamp-3 text-sm leading-6 text-slate-600">
                {project.description}
              </p>
            </div>

            <div className="flex flex-wrap gap-2 text-xs">
              <span className="rounded-full bg-slate-100 px-2 py-1 text-slate-700">
                {project.status}
              </span>
              <span className="rounded-full bg-slate-100 px-2 py-1 text-slate-700">
                {project.projectPhase}
              </span>
              <span className="rounded-full bg-slate-100 px-2 py-1 text-slate-700">
                {project.influencerDisplayName || project.influencerName}
              </span>
            </div>

            <div className="text-xs text-slate-500">
              업로드 기간: {formatDate(project.uploadStartAt)} ~{" "}
              {formatDate(project.uploadEndAt)}
            </div>

            <div className="flex gap-3">
              <Link
                href={`/projects/${project.id}`}
                className="inline-flex h-10 flex-1 items-center justify-center rounded-xl border border-slate-300 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
              >
                프로젝트 보기
              </Link>

              <Link
                href={`/admin/projects/${project.id}/photobook`}
                className="inline-flex h-10 flex-1 items-center justify-center rounded-xl bg-slate-900 text-sm font-medium text-white transition hover:bg-slate-700"
              >
                포토북 관리
              </Link>
            </div>
          </div>
        </article>
      ))}
    </div>
  );
}

function formatDate(value: string) {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleDateString("ko-KR");
}