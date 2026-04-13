import Link from "next/link";
import { getProjects } from "@/features/project/api/getProjects";
import { getProjectPhaseLabel } from "@/features/project/utils/projectPhase";

export default async function HomePage() {
  const projects = await getProjects();

  // 메인에서는 너무 많으면 복잡하니까 우선 3개만 노출
  const featuredProjects = projects.slice(0, 3);

  const phases = [
    {
      step: "01",
      title: "프로젝트 발견",
      description: "팬은 인플루언서 또는 스포츠 스타의 포토북 프로젝트를 발견합니다.",
    },
    {
      step: "02",
      title: "사진 업로드",
      description: "일정 기간 동안 의미 있는 사진과 콘텐츠를 업로드합니다.",
    },
    {
      step: "03",
      title: "AI + 큐레이션",
      description: "중복 제거, 유사 이미지 그룹핑, 품질 점수화를 거쳐 큐레이터가 최종 선별합니다.",
    },
    {
      step: "04",
      title: "미리보기 및 주문",
      description: "일부 페이지를 확인한 뒤 한정판 포토북을 주문할 수 있습니다.",
    },
  ];

  return (
    <main className="min-h-screen bg-white text-slate-900">
      {/* 상단 네비게이션 */}
      {/* <header className="sticky top-0 z-50 border-b border-slate-200 bg-white/90 backdrop-blur">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
          <div>
            <p className="text-lg font-bold tracking-tight">FanBook</p>
            <p className="text-xs text-slate-500">Influencer PhotoBook Goods Platform</p>
          </div>

          <nav className="hidden gap-6 text-sm font-medium md:flex">
            <a href="#projects" className="transition hover:text-slate-600">
              Projects
            </a>
            <a href="#how-it-works" className="transition hover:text-slate-600">
              How it works
            </a>
            <a href="#about" className="transition hover:text-slate-600">
              About
            </a>
          </nav>

          <div className="flex items-center gap-3">
            <button className="rounded-full border border-slate-300 px-4 py-2 text-sm font-medium transition hover:bg-slate-50">
              로그인
            </button>
            <Link
              href="/projects"
              className="rounded-full bg-slate-900 px-4 py-2 text-sm font-medium text-white transition hover:bg-slate-700"
            >
              프로젝트 둘러보기
            </Link>
          </div>
        </div>
      </header> */}

      {/* 히어로 */}
      <section className="border-b border-slate-200 bg-gradient-to-b from-slate-50 to-white">
        <div className="mx-auto grid max-w-7xl gap-10 px-6 py-16 md:grid-cols-2 md:items-center md:py-24">
          <div>
            <p className="mb-3 inline-flex rounded-full border border-slate-300 px-3 py-1 text-xs font-semibold uppercase tracking-[0.2em] text-slate-600">
              limited photobook goods
            </p>

            <h1 className="text-4xl font-bold leading-tight tracking-tight md:text-6xl">
              팬의 추억과
              <br />
              아티스트의 순간을
              <br />
              한 권의 포토북으로.
            </h1>

            <p className="mt-6 max-w-xl text-base leading-7 text-slate-600 md:text-lg">
              인플루언서, 스포츠 스타, 유튜버의 프로젝트를 발견하고 사진을 업로드하세요.
              AI 필터링과 큐레이션을 거쳐 소장 가치가 높은 포토북 굿즈로 완성됩니다.
            </p>

            <div className="mt-8 flex flex-wrap gap-4">
              <a
                href="#projects"
                className="rounded-full bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-700"
              >
                진행 중인 프로젝트 보기
              </a>
              <a
                href="#how-it-works"
                className="rounded-full border border-slate-300 px-6 py-3 text-sm font-semibold transition hover:bg-slate-50"
              >
                서비스 방식 알아보기
              </a>
            </div>
          </div>

          <div className="relative">
            <div className="overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-xl">
              <img
                src="https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?auto=format&fit=crop&w=1400&q=80"
                alt="포토북 서비스 대표 이미지"
                className="h-[440px] w-full object-cover"
              />
            </div>

            <div className="absolute -bottom-6 left-6 rounded-3xl border border-slate-200 bg-white p-5 shadow-lg">
              <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">
                curator workflow
              </p>
              <p className="mt-2 text-sm font-semibold text-slate-900">
                업로드 → AI 필터링 → 큐레이션 → 미리보기 → 주문
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* 서비스 소개 */}
      <section id="about" className="mx-auto max-w-7xl px-6 py-16 md:py-24">
        <div className="grid gap-6 md:grid-cols-3">
          <article className="rounded-3xl border border-slate-200 p-6">
            <p className="text-sm font-semibold text-slate-500">For Fans</p>
            <h2 className="mt-3 text-xl font-bold">직접 참여하는 굿즈 제작</h2>
            <p className="mt-3 text-sm leading-6 text-slate-600">
              팬이 직접 업로드한 사진이 실제 포토북 제작 과정에 반영됩니다.
            </p>
          </article>

          <article className="rounded-3xl border border-slate-200 p-6">
            <p className="text-sm font-semibold text-slate-500">For Quality</p>
            <h2 className="mt-3 text-xl font-bold">AI 기반 품질 보조</h2>
            <p className="mt-3 text-sm leading-6 text-slate-600">
              기본 필터링, 유사 이미지 그룹핑, 품질 점수화를 통해 큐레이션 효율을 높입니다.
            </p>
          </article>

          <article className="rounded-3xl border border-slate-200 p-6">
            <p className="text-sm font-semibold text-slate-500">For Collecting</p>
            <h2 className="mt-3 text-xl font-bold">소장 가치 중심 포토북</h2>
            <p className="mt-3 text-sm leading-6 text-slate-600">
              특정 기간의 의미 있는 활동과 팬의 기억을 담은 한정판 굿즈를 목표로 합니다.
            </p>
          </article>
        </div>
      </section>

      {/* 실데이터 프로젝트 섹션 */}
      <section id="projects" className="bg-slate-50 py-16 md:py-24">
        <div className="mx-auto max-w-7xl px-6">
          <div className="flex flex-col justify-between gap-4 md:flex-row md:items-end">
            <div>
              <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
                featured projects
              </p>
              <h2 className="mt-3 text-3xl font-bold tracking-tight md:text-4xl">
                지금 참여할 수 있는 프로젝트
              </h2>
              <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-600 md:text-base">
                실제 등록된 프로젝트를 기준으로 메인 화면에 노출합니다.
              </p>
            </div>

            <Link
              href="/projects"
              className="inline-flex rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-white"
            >
              전체 프로젝트 보기
            </Link>
          </div>

          {featuredProjects.length === 0 ? (
            <div className="mt-10 rounded-3xl border border-dashed border-slate-300 bg-white p-10 text-center">
              <p className="text-lg font-semibold text-slate-700">
                아직 등록된 프로젝트가 없습니다.
              </p>
              <p className="mt-2 text-sm text-slate-500">
                프로젝트가 등록되면 이 영역에 자동으로 노출됩니다.
              </p>
            </div>
          ) : (
            <div className="mt-10 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
              {featuredProjects.map((project) => (
                <Link
                  key={project.id}
                  href={`/projects/${project.id}`}
                  className="block overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-sm transition hover:-translate-y-1 hover:shadow-lg"
                >
                  <div className="relative">
                    <img
                      src={project.coverImageUrl || "https://picsum.photos/seed/default/1200/800"}
                      alt={project.title}
                      className="h-64 w-full object-cover"
                    />
                    <span className="absolute left-4 top-4 rounded-full bg-white/90 px-3 py-1 text-xs font-semibold text-slate-800">
                      {getProjectPhaseLabel(project.projectPhase)}
                    </span>
                  </div>

                  <div className="p-6">
                    <p className="text-sm font-semibold text-slate-500">
                      {project.influencerDisplayName}
                    </p>

                    <h3 className="mt-2 text-xl font-bold tracking-tight">
                      {project.title}
                    </h3>

                    <p className="mt-3 line-clamp-3 text-sm leading-6 text-slate-600">
                      {project.description || "프로젝트 설명이 아직 등록되지 않았습니다."}
                    </p>

                    <div className="mt-5 flex items-center justify-between border-t border-slate-100 pt-4">
                      <div>
                        <p className="text-xs text-slate-500">업로드 마감</p>
                        <p className="text-sm font-semibold text-slate-900">
                          {formatDate(project.uploadEndAt)}
                        </p>
                      </div>

                      <span className="rounded-full bg-slate-900 px-4 py-2 text-sm font-semibold text-white">
                        자세히 보기
                      </span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          )}
        </div>
      </section>

      {/* 플로우 설명 */}
      <section id="how-it-works" className="mx-auto max-w-7xl px-6 py-16 md:py-24">
        <div className="max-w-2xl">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
            how it works
          </p>
          <h2 className="mt-3 text-3xl font-bold tracking-tight md:text-4xl">
            팬 참여형 포토북이 완성되는 방식
          </h2>
          <p className="mt-4 text-sm leading-6 text-slate-600 md:text-base">
            서비스의 전체 플로우를 단순하게 보여주는 영역입니다.
          </p>
        </div>

        <div className="mt-10 grid gap-6 md:grid-cols-2 xl:grid-cols-4">
          {phases.map((item) => (
            <article key={item.step} className="rounded-3xl border border-slate-200 p-6">
              <p className="text-sm font-semibold text-slate-500">STEP {item.step}</p>
              <h3 className="mt-3 text-xl font-bold">{item.title}</h3>
              <p className="mt-3 text-sm leading-6 text-slate-600">{item.description}</p>
            </article>
          ))}
        </div>
      </section>

      {/* CTA */}
      <section className="bg-slate-900 py-16 text-white md:py-20">
        <div className="mx-auto flex max-w-7xl flex-col gap-6 px-6 md:flex-row md:items-center md:justify-between">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-300">
              start now
            </p>
            <h2 className="mt-3 text-3xl font-bold tracking-tight md:text-4xl">
              다음 포토북 프로젝트를
              <br />
              네 플랫폼에서 시작해보자.
            </h2>
            <p className="mt-4 max-w-2xl text-sm leading-6 text-slate-300 md:text-base">
              이제 메인 페이지와 프로젝트 목록 모두 실데이터 기반으로 움직이기 시작했다.
            </p>
          </div>

          <div className="flex flex-wrap gap-4">
            <Link
              href="/projects"
              className="rounded-full bg-white px-6 py-3 text-sm font-semibold text-slate-900 transition hover:bg-slate-200"
            >
              프로젝트 보러 가기
            </Link>
          </div>
        </div>
      </section>

      {/* 푸터 */}
      <footer className="border-t border-slate-200 bg-white">
        <div className="mx-auto flex max-w-7xl flex-col gap-4 px-6 py-8 text-sm text-slate-500 md:flex-row md:items-center md:justify-between">
          <div>
            <p className="font-semibold text-slate-800">FanBook</p>
            <p className="mt-1">팬 참여형 인플루언서 포토북 굿즈 플랫폼</p>
          </div>

          <div className="flex gap-5">
            <a href="#" className="transition hover:text-slate-700">
              Terms
            </a>
            <a href="#" className="transition hover:text-slate-700">
              Privacy
            </a>
            <a href="#" className="transition hover:text-slate-700">
              Contact
            </a>
          </div>
        </div>
      </footer>
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