"use client";

type Props = {
  title: string;
  setTitle: (value: string) => void;
  bookSpecUid: string;
  setBookSpecUid: (value: string) => void;
  adminUserId: number;
  setAdminUserId: (value: number) => void;
  selectedCoverTemplateUid: string;
  selectedContentTemplateUid: string;
  expectedFinalPageCount: number | null;
  canCreate: boolean;
  isCreating: boolean;
  onCreate: () => void;
};

export default function PhotobookCreatePanel({
  title,
  setTitle,
  bookSpecUid,
  setBookSpecUid,
  adminUserId,
  setAdminUserId,
  selectedCoverTemplateUid,
  selectedContentTemplateUid,
  expectedFinalPageCount,
  canCreate,
  isCreating,
  onCreate,
}: Props) {
  return (
    <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="mb-4">
        <h2 className="text-lg font-semibold text-slate-900">포토북 생성</h2>
        <p className="mt-1 text-sm text-slate-500">
          선택된 템플릿과 현재 큐레이션 레이아웃 기준으로 포토북을 생성합니다.
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-2">
        <label className="flex flex-col gap-2">
          <span className="text-sm font-medium text-slate-800">관리자 ID</span>
          <input
            type="number"
            value={adminUserId}
            onChange={(e) => setAdminUserId(Number(e.target.value))}
            className="h-11 rounded-xl border border-slate-300 px-3 text-sm"
          />
        </label>

        <label className="flex flex-col gap-2">
          <span className="text-sm font-medium text-slate-800">판형</span>
          <input
            value={bookSpecUid}
            onChange={(e) => setBookSpecUid(e.target.value)}
            className="h-11 rounded-xl border border-slate-300 px-3 text-sm"
          />
        </label>
      </div>

      <label className="mt-4 flex flex-col gap-2">
        <span className="text-sm font-medium text-slate-800">포토북 제목</span>
        <input
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="h-11 rounded-xl border border-slate-300 px-3 text-sm"
        />
      </label>

      <div className="mt-5 grid gap-3 md:grid-cols-3">
        <div className="rounded-xl border border-slate-200 bg-slate-50 p-4">
          <p className="text-xs text-slate-500">선택된 커버 템플릿</p>
          <p className="mt-2 break-all text-sm font-medium text-slate-900">
            {selectedCoverTemplateUid || "-"}
          </p>
        </div>

        <div className="rounded-xl border border-slate-200 bg-slate-50 p-4">
          <p className="text-xs text-slate-500">선택된 내지 템플릿</p>
          <p className="mt-2 break-all text-sm font-medium text-slate-900">
            {selectedContentTemplateUid || "-"}
          </p>
        </div>

        <div className="rounded-xl border border-slate-200 bg-slate-50 p-4">
          <p className="text-xs text-slate-500">예상 최종 페이지 수</p>
          <p className="mt-2 text-sm font-medium text-slate-900">
            {expectedFinalPageCount ?? "-"}
          </p>
        </div>
      </div>

      <div className="mt-5">
        <button
          type="button"
          onClick={onCreate}
          disabled={!canCreate}
          className="inline-flex h-11 items-center justify-center rounded-xl bg-slate-900 px-5 text-sm font-semibold text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:bg-slate-300"
        >
          {isCreating ? "생성 중..." : "포토북 생성"}
        </button>
      </div>
    </section>
  );
}