import Link from "next/link";
import { getProjectDetail } from "@/features/project/api/getProjectDetail";
import { getProjectPhaseLabel } from "@/features/project/utils/projectPhase";
import ProjectCuratedPreviewSection from "@/features/project/components/ProjectCuratedPreviewSection";

type PageProps = {
  params: {
    projectId: string;
  };
};

export default async function ProjectDetailPage({ params }: PageProps) {
  const project = await getProjectDetail(params.projectId);

  return (
    <main className="bg-white text-slate-900">
      {/* 상단 히어로 섹션 */}
      <section className="border-b border-slate-200 bg-slate-50">
        <div className="mx-auto grid max-w-7xl gap-10 px-6 py-14 md:grid-cols-2 md:items-center">
          <div>
            <span className="inline-flex rounded-full border border-slate-300 bg-white px-3 py-1 text-xs font-semibold text-slate-700">
              {getProjectPhaseLabel(project.projectPhase)}
            </span>

            <p className="mt-5 text-sm font-semibold text-slate-500">
              {project.influencerDisplayName}
            </p>

            <h1 className="mt-3 text-4xl font-bold tracking-tight md:text-5xl">
              {project.title}
            </h1>

            <p className="mt-5 text-base leading-7 text-slate-600 md:text-lg">
              {project.description || "프로젝트 설명이 아직 등록되지 않았습니다."}
            </p>

            <div className="mt-8 flex flex-wrap gap-3">
              {project.projectPhase === "UPLOAD_OPEN" && (
                <Link
                    href={`/upload/${project.id}`}
                    className="rounded-full bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-700"
                >
                    사진 업로드하기
                </Link>
)}

              {project.projectPhase === "PREVIEW_OPEN" && (
                <button className="rounded-full bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-700">
                  미리보기 보러가기
                </button>
              )}

              {project.projectPhase === "ORDER_OPEN" && (
                <button className="rounded-full bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-700">
                  포토북 주문하기
                </button>
              )}

              {project.projectPhase === "BEFORE_UPLOAD" && (
                <button className="rounded-full bg-slate-300 px-6 py-3 text-sm font-semibold text-white">
                  오픈 예정
                </button>
              )}

              {project.projectPhase === "CLOSED" && (
                <button className="rounded-full bg-slate-300 px-6 py-3 text-sm font-semibold text-white">
                  종료된 프로젝트
                </button>
              )}

              <Link
                href="/projects"
                className="rounded-full border border-slate-300 px-6 py-3 text-sm font-semibold transition hover:bg-slate-100"
              >
                목록으로
              </Link>
            </div>
          </div>

          <div className="overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-lg">
            <img
              src={project.coverImageUrl || "https://picsum.photos/seed/default/1200/800"}
              alt={project.title}
              className="h-[420px] w-full object-cover"
            />
          </div>
        </div>
      </section>

      {/* 본문 */}
      <section className="mx-auto max-w-7xl px-6 py-14">
        <div className="grid gap-8 lg:grid-cols-[1.3fr_0.7fr]">
          <article className="rounded-[2rem] border border-slate-200 p-8">
            <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
              project overview
            </p>
            <h2 className="mt-4 text-2xl font-bold">프로젝트 소개</h2>

            <p className="mt-5 text-base leading-7 text-slate-600">
              {project.description || "프로젝트 소개가 아직 등록되지 않았습니다."}
            </p>

            <div className="mt-8 rounded-2xl bg-slate-50 p-6">
              <h3 className="text-lg font-bold">참여 전 확인해보세요</h3>
              <ul className="mt-4 space-y-3 text-sm leading-6 text-slate-600">
                <li>• 프로젝트 단계에 따라 참여 가능한 기능이 달라집니다.</li>
                <li>• 업로드 단계에서는 팬 사진 제출이 가능합니다.</li>
                <li>• 미리보기 단계에서는 일부 페이지를 확인할 수 있습니다.</li>
                <li>• 주문 단계에서는 실제 포토북 주문이 가능합니다.</li>
              </ul>
            </div>
            <ProjectCuratedPreviewSection projectId={project.id} />
          </article>

          <aside className="rounded-[2rem] border border-slate-200 p-8">
            <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
              schedule
            </p>
            <h2 className="mt-4 text-2xl font-bold">프로젝트 일정</h2>

            <div className="mt-6 space-y-5">
              <div className="rounded-2xl bg-slate-50 p-4">
                <p className="text-xs font-semibold text-slate-500">업로드 기간</p>
                <p className="mt-2 text-sm font-bold text-slate-900">
                  {formatDate(project.uploadStartAt)} ~ {formatDate(project.uploadEndAt)}
                </p>
              </div>

              <div className="rounded-2xl bg-slate-50 p-4">
                <p className="text-xs font-semibold text-slate-500">미리보기 기간</p>
                <p className="mt-2 text-sm font-bold text-slate-900">
                  {formatDate(project.previewStartAt)} ~ {formatDate(project.previewEndAt)}
                </p>
              </div>

              <div className="rounded-2xl bg-slate-50 p-4">
                <p className="text-xs font-semibold text-slate-500">주문 기간</p>
                <p className="mt-2 text-sm font-bold text-slate-900">
                  {formatDate(project.orderStartAt)} ~ {formatDate(project.orderEndAt)}
                </p>
              </div>
            </div>

            <div className="mt-8 rounded-2xl border border-slate-200 p-5">
              <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">
                influencer
              </p>
              <p className="mt-3 text-xl font-bold text-slate-900">
                {project.influencerDisplayName}
              </p>
              <p className="mt-2 text-sm text-slate-600">
                @{project.influencerName}
              </p>
            </div>

            <div className="mt-5 rounded-2xl border border-slate-200 p-5">
              <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">
                created by
              </p>
              <p className="mt-3 text-sm font-semibold text-slate-900">
                {project.createdByNickname}
              </p>
            </div>
          </aside>
        </div>
      </section>
    </main>
  );
}

function formatDate(dateString: string) {
  const date = new Date(dateString);

  if (Number.isNaN(date.getTime())) {
    return dateString;
  }

  return date.toLocaleDateString("ko-KR");
}