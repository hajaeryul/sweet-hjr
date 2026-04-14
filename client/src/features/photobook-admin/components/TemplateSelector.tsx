"use client";

import { TemplateSummary } from "../types/photobookAdmin";

type Props = {
  title: string;
  templates: TemplateSummary[];
  selectedTemplateUid: string;
  onSelect: (templateUid: string) => void;
};

export default function TemplateSelector({
  title,
  templates,
  selectedTemplateUid,
  onSelect,
}: Props) {
  return (
    <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="mb-4">
        <h2 className="text-lg font-semibold text-slate-900">{title}</h2>
        <p className="mt-1 text-sm text-slate-500">
          템플릿을 선택하면 우측 상세 패널에서 필수 파라미터를 확인할 수 있습니다.
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {templates.map((template) => {
          const isSelected = selectedTemplateUid === template.templateUid;

          return (
            <button
              key={template.templateUid}
              type="button"
              onClick={() => onSelect(template.templateUid)}
              className={`overflow-hidden rounded-2xl border text-left transition ${
                isSelected
                  ? "border-slate-900 ring-2 ring-slate-200"
                  : "border-slate-200 hover:border-slate-400"
              }`}
            >
              {template.layoutThumbnailUrl ? (
                <img
                  src={template.layoutThumbnailUrl}
                  alt={template.templateName}
                  className="h-52 w-full object-cover"
                />
              ) : (
                <div className="flex h-52 items-center justify-center bg-slate-100 text-sm text-slate-400">
                  썸네일 없음
                </div>
              )}

              <div className="space-y-2 p-4">
                <div>
                  <p className="text-base font-semibold text-slate-900">
                    {template.templateName}
                  </p>
                  <p className="mt-1 text-xs text-slate-500">
                    {template.templateUid}
                  </p>
                </div>

                <div className="flex flex-wrap gap-2 text-xs">
                  <span className="rounded-full bg-slate-100 px-2 py-1 text-slate-700">
                    {template.theme ?? "테마 없음"}
                  </span>
                  <span className="rounded-full bg-slate-100 px-2 py-1 text-slate-700">
                    필수 {template.requiredParameterCount}개
                  </span>
                </div>
              </div>
            </button>
          );
        })}
      </div>
    </section>
  );
}