import AdminProjectList from "@/features/photobook-admin/components/AdminProjectList";
import { getProjects } from "@/features/project/api/getProjects";

export default async function AdminProjectsPage() {
  const projects = await getProjects();

  return (
    <main className="mx-auto w-full max-w-7xl px-6 py-10">
      <div className="mb-8">
        <p className="text-sm font-medium text-slate-500">admin / projects</p>
        <h1 className="mt-2 text-3xl font-bold text-slate-900">
          관리자 프로젝트 목록
        </h1>
        <p className="mt-3 text-sm leading-6 text-slate-600">
          프로젝트를 확인하고, 각 프로젝트별 포토북 관리 페이지로 이동할 수 있습니다.
        </p>
      </div>

      <AdminProjectList projects={projects} />
    </main>
  );
}