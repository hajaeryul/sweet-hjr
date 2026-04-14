"use client";

import { TemplateDetail } from "../types/photobookAdmin";

type Props = {
  title: string;
  detail: TemplateDetail | null;
};

export default function TemplateDetailPanel({ title, detail }: Props) {
  return (
    <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="mb-4">
        <h2 className="text-lg font-semibold text-slate-900">{title}</h2>
      </div>

      {!detail ? (
        <p className="text-sm text-slate-500">선택된 템플릿이 없습니다.</p>
      ) : (
        <div className="space-y-4">
          {detail.layoutThumbnailUrl ? (
            <img
              src={detail.layoutThumbnailUrl}
              alt={detail.templateName}
              className="h-72 w-full rounded-2xl border border-slate-200 object-cover"
            />
          ) : (
            <div className="flex h-72 items-center justify-center rounded-2xl border border-slate-200 bg-slate-100 text-sm text-slate-400">
              썸네일 없음
            </div>
          )}

          <div>
            <p className="text-lg font-semibold text-slate-900">
              {detail.templateName}
            </p>
            <p className="mt-1 text-xs text-slate-500">{detail.templateUid}</p>
          </div>

          <div className="flex flex-wrap gap-2 text-xs">
            <span className="rounded-full bg-slate-100 px-2 py-1 text-slate-700">
              {detail.theme ?? "테마 없음"}
            </span>
            <span className="rounded-full bg-slate-100 px-2 py-1 text-slate-700">
              필수 {detail.requiredParameterCount}개
            </span>
            <span className="rounded-full bg-slate-100 px-2 py-1 text-slate-700">
              {detail.templateKind}
            </span>
          </div>

          <div className="space-y-3">
            {detail.parameters.map((parameter) => (
              <div
                key={parameter.key}
                className="rounded-xl border border-slate-200 bg-slate-50 p-3"
              >
                <div className="flex items-center justify-between gap-3">
                  <p className="font-medium text-slate-900">{parameter.key}</p>
                  <span
                    className={`rounded-full px-2 py-1 text-xs font-medium ${
                      parameter.required
                        ? "bg-rose-100 text-rose-700"
                        : "bg-slate-200 text-slate-700"
                    }`}
                  >
                    {parameter.required ? "필수" : "선택"}
                  </span>
                </div>

                <p className="mt-2 text-xs text-slate-500">
                  binding: {parameter.binding ?? "-"} / type:{" "}
                  {parameter.type ?? "-"}
                </p>

                {parameter.description && (
                  <p className="mt-2 text-sm text-slate-700">
                    {parameter.description}
                  </p>
                )}
              </div>
            ))}
          </div>
        </div>
      )}
    </section>
  );
}