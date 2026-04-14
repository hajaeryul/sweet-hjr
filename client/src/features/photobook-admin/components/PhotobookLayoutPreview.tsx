"use client";

import { PhotobookLayoutPreview } from "../types/photobookAdmin";

type Props = {
  layout: PhotobookLayoutPreview | null;
};

const BASE_URL = "http://localhost:8080";

function toAbsoluteImageUrl(imageUrl: string) {
  if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
    return imageUrl;
  }
  return `${BASE_URL}${imageUrl}`;
}

export default function PhotobookLayoutPreviewPanel({ layout }: Props) {
  return (
    <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="mb-4">
        <h2 className="text-lg font-semibold text-slate-900">레이아웃 미리보기</h2>
      </div>

      {!layout ? (
        <p className="text-sm text-slate-500">레이아웃 정보가 없습니다.</p>
      ) : (
        <div className="space-y-6">
          <div className="grid gap-3 sm:grid-cols-3">
            <div className="rounded-xl border border-slate-200 bg-slate-50 p-4">
              <p className="text-xs text-slate-500">커버 포함 여부</p>
              <p className="mt-2 text-lg font-semibold text-slate-900">
                {layout.cover ? "있음" : "없음"}
              </p>
            </div>
            <div className="rounded-xl border border-slate-200 bg-slate-50 p-4">
              <p className="text-xs text-slate-500">내지 후보 수</p>
              <p className="mt-2 text-lg font-semibold text-slate-900">
                {layout.contentPageCount}
              </p>
            </div>
            <div className="rounded-xl border border-slate-200 bg-slate-50 p-4">
              <p className="text-xs text-slate-500">예상 최종 페이지 수</p>
              <p className="mt-2 text-lg font-semibold text-slate-900">
                {layout.expectedFinalPageCount}
              </p>
            </div>
          </div>

          {layout.cover && (
            <div>
              <h3 className="mb-3 text-base font-semibold text-slate-900">
                커버 후보
              </h3>
              <div className="overflow-hidden rounded-2xl border border-slate-200">
                <img
                  src={toAbsoluteImageUrl(layout.cover.imageUrl)}
                  alt="cover preview"
                  className="h-80 w-full object-cover"
                />
                <div className="space-y-2 p-4">
                  <p className="text-sm font-medium text-slate-900">
                    priority {layout.cover.priorityOrder}
                  </p>
                  {layout.cover.caption && (
                    <p className="text-sm text-slate-600">{layout.cover.caption}</p>
                  )}
                </div>
              </div>
            </div>
          )}

          <div>
            <h3 className="mb-3 text-base font-semibold text-slate-900">
              내지 후보
            </h3>
            <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
              {layout.pages.map((page) => (
                <div
                  key={page.curatedImageId}
                  className="overflow-hidden rounded-2xl border border-slate-200"
                >
                  <img
                    src={toAbsoluteImageUrl(page.imageUrl)}
                    alt={`page-${page.pageNumber}`}
                    className="h-52 w-full object-cover"
                  />
                  <div className="space-y-2 p-4">
                    <div className="flex items-center justify-between text-sm">
                      <span className="font-medium text-slate-900">
                        {page.pageRole}
                      </span>
                      <span className="text-slate-500">{page.pageNumber}p</span>
                    </div>
                    <p className="text-xs text-slate-500">
                      priority {page.priorityOrder}
                    </p>
                    {page.caption && (
                      <p className="text-sm text-slate-600">{page.caption}</p>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </section>
  );
}