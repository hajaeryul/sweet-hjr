import PhotobookAdminClient from "@/features/photobook-admin/components/PhotobookAdminClient";

type PageProps = {
  params: {
    projectId: string;
  };
};

export default function AdminProjectPhotobookPage({ params }: PageProps) {
  return (
    <main className="mx-auto w-full max-w-7xl px-6 py-10">
      <div className="mb-8">
        <p className="text-sm font-medium text-slate-500">admin / photobook</p>
        <h1 className="mt-2 text-3xl font-bold text-slate-900">
          프로젝트 포토북 관리
        </h1>
        <p className="mt-3 text-sm leading-6 text-slate-600">
          큐레이터가 선정한 이미지를 기준으로 템플릿을 조회하고 선택한 뒤,
          실제 포토북을 생성합니다.
        </p>
      </div>

      <PhotobookAdminClient projectId={params.projectId} />
    </main>
  );
}