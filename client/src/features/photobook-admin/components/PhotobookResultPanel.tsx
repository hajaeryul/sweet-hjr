"use client";

import {
  PhotobookCreateResponse,
  PhotobookDetail,
} from "../types/photobookAdmin";

const BASE_URL = "http://localhost:8080";

function toAbsoluteImageUrl(imageUrl: string) {
  if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
    return imageUrl;
  }
  return `${BASE_URL}${imageUrl}`;
}

type Props = {
  createdPhotobook: PhotobookCreateResponse | null;
  photobookDetail: PhotobookDetail | null;
};

export default function PhotobookResultPanel({
  createdPhotobook,
  photobookDetail,
}: Props) {
  return (
    <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="mb-4">
        <h2 className="text-lg font-semibold text-slate-900">생성 결과</h2>
      </div>

      {createdPhotobook && (
        <div className="mb-5 rounded-2xl border border-slate-200 bg-slate-50 p-4">
          <div className="grid gap-3 md:grid-cols-2">
            <ResultItem label="photobookId" value={createdPhotobook.photobookId} />
            <ResultItem label="status" value={createdPhotobook.status} />
            <ResultItem
              label="finalPageCount"
              value={createdPhotobook.finalPageCount ?? "-"}
            />
            <ResultItem
              label="sweetbookBookUid"
              value={createdPhotobook.sweetbookBookUid ?? "-"}
            />
          </div>
        </div>
      )}

      {!photobookDetail ? (
        <p className="text-sm text-slate-500">생성된 포토북 정보가 없습니다.</p>
      ) : (
        <div className="space-y-5">
          <div className="grid gap-3 md:grid-cols-2">
            <ResultItem label="제목" value={photobookDetail.title} />
            <ResultItem label="상태" value={photobookDetail.status} />
            <ResultItem
              label="최종 페이지 수"
              value={photobookDetail.finalPageCount ?? "-"}
            />
            <ResultItem
              label="Sweetbook UID"
              value={photobookDetail.sweetbookBookUid ?? "-"}
            />
          </div>

          {photobookDetail.lastErrorMessage && (
            <div className="rounded-xl border border-rose-200 bg-rose-50 p-4 text-sm text-rose-700">
              {photobookDetail.lastErrorMessage}
            </div>
          )}

          <div>
            <h3 className="mb-3 text-base font-semibold text-slate-900">
              저장된 페이지
            </h3>
            <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
              {photobookDetail.pages.map((page) => (
                <div
                  key={`${page.pageRole}-${page.pageNumber}-${page.curatedImageId}`}
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

function ResultItem({
  label,
  value,
}: {
  label: string;
  value: string | number;
}) {
  return (
    <div className="rounded-xl border border-slate-200 bg-slate-50 p-4">
      <p className="text-xs text-slate-500">{label}</p>
      <p className="mt-2 break-all text-sm font-medium text-slate-900">
        {value}
      </p>
    </div>
  );
}