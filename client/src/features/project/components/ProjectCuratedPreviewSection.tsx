"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { useAuth } from "@/features/auth/context/AuthContext";
import { getCuratedImages } from "@/features/upload/api/getCuratedImages";
import { CuratedImagePreview } from "@/features/upload/types/curatedImage";

const BASE_URL = "http://localhost:8080";

type Props = {
  projectId: number;
};

export default function ProjectCuratedPreviewSection({ projectId }: Props) {
  const { isLoggedIn, user } = useAuth();
  const [images, setImages] = useState<CuratedImagePreview[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const canCurate = useMemo(() => {
    if (!isLoggedIn || !user) return false;
    return user.role === "ADMIN" || user.role === "CURATOR";
  }, [isLoggedIn, user]);

  const previewImages = [...images]
  .sort((a, b) => a.priorityOrder - b.priorityOrder)
  .slice(0, 12);

  useEffect(() => {
    const fetchCuratedImages = async () => {
      try {
        setIsLoading(true);
        setErrorMessage("");

        const result = await getCuratedImages(projectId);
        setImages(result);
      } catch (error) {
        console.error(error);
        setErrorMessage("선정된 이미지 미리보기를 불러오는 중 문제가 발생했습니다.");
      } finally {
        setIsLoading(false);
      }
    };

    fetchCuratedImages();
  }, [projectId]);

  return (
    <section className="mt-12 rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm">
      <div className="flex flex-col gap-4 md:flex-row md:items-end md:justify-between">
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
            preview
          </p>
          <h2 className="mt-2 text-2xl font-bold tracking-tight">
            선정된 사진 미리보기
          </h2>
          <p className="mt-2 text-sm leading-6 text-slate-600">
            큐레이터가 선정한 사진 일부를 미리 확인할 수 있습니다.
          </p>
        </div>

        {canCurate && (
          <Link
            href={`/curation/projects/${projectId}`}
            className="inline-flex rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-slate-50"
          >
            큐레이터 선정 창으로 가기
          </Link>
        )}
      </div>

      {isLoading ? (
        <div className="mt-6 rounded-2xl bg-slate-50 p-6 text-sm text-slate-500">
          선정 이미지를 불러오는 중입니다.
        </div>
      ) : errorMessage ? (
        <div className="mt-6 rounded-2xl border border-red-200 bg-red-50 p-6 text-sm text-red-700">
          {errorMessage}
        </div>
      ) : images.length === 0 ? (
        <div className="mt-6 rounded-2xl border border-dashed border-slate-300 p-10 text-center">
          <p className="text-lg font-semibold text-slate-800">
            아직 선정된 이미지가 없습니다.
          </p>
          <p className="mt-2 text-sm text-slate-500">
            큐레이터가 이미지를 선정하면 이 영역에 미리보기가 표시됩니다.
          </p>

          {canCurate && (
            <Link
              href={`/curation/projects/${projectId}`}
              className="mt-6 inline-flex rounded-full bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700"
            >
              지금 선정하러 가기
            </Link>
          )}
        </div>
      ) : (
        <div className="mt-6 grid gap-5 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {previewImages.map((image) => (
            <article
              key={image.curatedImageId}
              className="overflow-hidden rounded-[1.5rem] border border-slate-200 bg-white shadow-sm"
            >
              <div className="aspect-[4/5] overflow-hidden bg-slate-100">
                {image.fileUrl ? (
                  <img
                    src={`${BASE_URL}${image.fileUrl}`}
                    alt={`curated-${image.curatedImageId}`}
                    className="h-full w-full object-cover"
                  />
                ) : (
                  <div className="h-full w-full bg-slate-100" />
                )}
              </div>

              <div className="p-4">
                <p className="text-sm font-semibold text-slate-900">
                  우선순위 #{image.priorityOrder}
                </p>

                {image.selectedReason && (
                  <p className="mt-2 text-xs leading-5 text-slate-500">
                    {image.selectedReason}
                  </p>
                )}
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}