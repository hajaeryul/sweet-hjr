import Link from "next/link";
import { getProjects } from "@/features/project/api/getProjects";
import { getProjectPhaseLabel } from "@/features/project/utils/projectPhase";

export default async function ProjectsPage() {
  const projects = await getProjects();

  return (
    <main className="mx-auto max-w-7xl px-6 py-16">
      <div className="flex flex-col gap-3">
        <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
          projects
        </p>
        <h1 className="text-3xl font-bold tracking-tight md:text-4xl">
          프로젝트 목록
        </h1>
        <p className="max-w-2xl text-sm leading-6 text-slate-600 md:text-base">
          현재 진행 중인 포토북 프로젝트를 확인하고, 원하는 프로젝트 상세 페이지로 이동할 수 있습니다.
        </p>
      </div>

      {/* 데이터가 없을 때 보여줄 빈 상태 */}
      {projects.length === 0 ? (
        <div className="mt-12 rounded-3xl border border-dashed border-slate-300 p-10 text-center">
          <p className="text-lg font-semibold text-slate-700">
            아직 등록된 프로젝트가 없습니다.
          </p>
          <p className="mt-2 text-sm text-slate-500">
            관리자 페이지에서 프로젝트를 먼저 생성해 주세요.
          </p>
        </div>
      ) : (
        <div className="mt-10 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
          {projects.map((project) => (
            <Link
              key={project.id}
              href={`/projects/${project.id}`}
              className="block overflow-hidden rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm transition hover:-translate-y-1 hover:shadow-lg"
            >
              <div className="flex items-start justify-between gap-4">
                <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700">
                  {getProjectPhaseLabel(project.projectPhase)}
                </span>

                <span className="text-xs text-slate-400">
                  #{project.id}
                </span>
              </div>

              <div className="mt-5">
                <p className="text-sm font-semibold text-slate-500">
                  {project.influencerDisplayName}
                </p>

                <h2 className="mt-2 text-xl font-bold tracking-tight text-slate-900">
                  {project.title}
                </h2>

                <p className="mt-3 line-clamp-3 text-sm leading-6 text-slate-600">
                  {project.description || "프로젝트 설명이 아직 등록되지 않았습니다."}
                </p>
              </div>

              <div className="mt-6 border-t border-slate-100 pt-4">
                <p className="text-xs text-slate-500">업로드 기간</p>
                <p className="mt-1 text-sm font-medium text-slate-800">
                  {formatDate(project.uploadStartAt)} ~ {formatDate(project.uploadEndAt)}
                </p>
              </div>

              <div className="mt-5 inline-flex items-center text-sm font-semibold text-slate-900">
                상세 보기
                <span className="ml-2">→</span>
              </div>
            </Link>
          ))}
        </div>
      )}
    </main>
  );
}

/**
 * 나중에 공통 util로 분리 가능
 * 지금은 목록 페이지에서만 쓰니까 여기 두고 시작한다.
 */
function formatDate(dateString: string) {
  const date = new Date(dateString);

  // 날짜 데이터가 이상할 때 원문 그대로 보여준다.
  if (Number.isNaN(date.getTime())) {
    return dateString;
  }

  return date.toLocaleDateString("ko-KR");
}