"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { useAuth } from "@/features/auth/context/AuthContext";
import { getCurationGroups } from "@/features/upload/api/getCurationGroups";
import { getCurationGroupItems } from "@/features/upload/api/getCurationGroupItems";
import { curateImage } from "@/features/upload/api/curateImage";
import {
  CurationGroupItem,
  CurationGroupSummary,
} from "@/features/upload/types/curation";
import { CuratedImagePreview } from "@/features/upload/types/curatedImage";
import { getCuratedImages } from "@/features/upload/api/getCuratedImages";


const BASE_URL = "http://localhost:8080";

type PageProps = {
  params: {
    projectId: string;
  };
};

export default function CurationProjectPage({ params }: PageProps) {
  const { projectId } = params;
  const { isLoggedIn, user } = useAuth();

  const [groups, setGroups] = useState<CurationGroupSummary[]>([]);
  const [selectedGroup, setSelectedGroup] = useState<CurationGroupSummary | null>(
    null
  );
  const [groupItems, setGroupItems] = useState<CurationGroupItem[]>([]);
  const [selectedImageId, setSelectedImageId] = useState<number | null>(null);

  const [selectedReason, setSelectedReason] = useState("");
  const [priorityOrder, setPriorityOrder] = useState(1);

  const [isLoadingGroups, setIsLoadingGroups] = useState(true);
  const [isLoadingItems, setIsLoadingItems] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [message, setMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const [curatedImages, setCuratedImages] = useState<CuratedImagePreview[]>([]);

  const isAllowedRole = useMemo(() => {
    if (!user) return false;
    return user.role === "ADMIN" || user.role === "CURATOR";
  }, [user]);

  useEffect(() => {
    if (!isLoggedIn || !user || !isAllowedRole) {
      setIsLoadingGroups(false);
      return;
    }

    const fetchGroups = async () => {
      try {
        setIsLoadingGroups(true);
        setErrorMessage("");

        const result = await getCurationGroups(projectId);
        setGroups(result);

        if (result.length > 0) {
          setSelectedGroup(result[0]);
        }
      } catch (error) {
        console.error(error);
        setErrorMessage("큐레이션 그룹 목록을 불러오는 중 문제가 발생했습니다.");
      } finally {
        setIsLoadingGroups(false);
      }
    };

    fetchGroups();
  }, [isLoggedIn, user, isAllowedRole, projectId]);

  useEffect(() => {
    if (!selectedGroup) {
      setGroupItems([]);
      setSelectedImageId(null);
      return;
    }

    const fetchGroupItems = async () => {
      try {
        setIsLoadingItems(true);
        setErrorMessage("");
        setMessage("");

        const result = await getCurationGroupItems(
          projectId,
          selectedGroup.imageGroupId
        );

        setGroupItems(result);
        setSelectedImageId(result.length > 0 ? result[0].fileId : null);
      } catch (error) {
        console.error(error);
        setErrorMessage("그룹 상세 이미지를 불러오는 중 문제가 발생했습니다.");
      } finally {
        setIsLoadingItems(false);
      }
    };

    fetchGroupItems();
  }, [projectId, selectedGroup]);

  useEffect(() => {
    const fetchCurated = async () => {
        try {
        const result = await getCuratedImages(projectId);
        setCuratedImages(result);
        } catch (e) {
        console.error(e);
        }
    };

    fetchCurated();
  }, [projectId]);

  const handleSelectGroup = (group: CurationGroupSummary) => {
    setSelectedGroup(group);
    setSelectedImageId(null);
    setSelectedReason("");
    setMessage("");
  };

  const selectedImageSet = useMemo(() => {
    return new Set(curatedImages.map((img) => img.sourceImageId));
  }, [curatedImages]);

  const handleSubmitCuration = async () => {
    if (!user) {
      setMessage("로그인이 필요합니다.");
      return;
    }

    if (!selectedGroup) {
      setMessage("선택된 그룹이 없습니다.");
      return;
    }

    if (!selectedImageId) {
      setMessage("선정할 이미지를 먼저 선택해 주세요.");
      return;
    }

    if (selectedImageSet.has(selectedImageId)) {
      setMessage("이미 선정된 이미지입니다.");
      return; 
    }

    try {
      setIsSubmitting(true);
      setMessage("");
      setErrorMessage("");

      const result = await curateImage(projectId, {
        sourceImageId: selectedImageId,
        imageGroupId: selectedGroup.imageGroupId,
        selectedBy: user.id,
        selectedReason,
        priorityOrder,
      });

      setMessage(
        `큐레이션 저장 완료: curatedImageId=${result.curatedImageId}, priority=${result.priorityOrder}`
      );
    } catch (error) {
      console.error(error);
      setErrorMessage("큐레이션 저장 중 문제가 발생했습니다.");
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!isLoggedIn || !user) {
    return (
      <main className="mx-auto max-w-4xl px-6 py-16">
        <section className="rounded-[2rem] border border-slate-200 bg-white p-10 text-center shadow-sm">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
            curation
          </p>
          <h1 className="mt-3 text-3xl font-bold tracking-tight">
            로그인이 필요합니다
          </h1>
          <p className="mt-3 text-sm leading-6 text-slate-600">
            큐레이터 페이지는 로그인 후 접근할 수 있습니다.
          </p>

          <Link
            href={`/projects/${projectId}`}
            className="mt-6 inline-flex rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-slate-50"
          >
            프로젝트로 돌아가기
          </Link>
        </section>
      </main>
    );
  }

  if (!isAllowedRole) {
    return (
      <main className="mx-auto max-w-4xl px-6 py-16">
        <section className="rounded-[2rem] border border-slate-200 bg-white p-10 text-center shadow-sm">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
            curation
          </p>
          <h1 className="mt-3 text-3xl font-bold tracking-tight">
            접근 권한이 없습니다
          </h1>
          <p className="mt-3 text-sm leading-6 text-slate-600">
            현재 페이지는 ADMIN 또는 CURATOR만 접근할 수 있습니다.
          </p>

          <Link
            href={`/projects/${projectId}`}
            className="mt-6 inline-flex rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-slate-50"
          >
            프로젝트로 돌아가기
          </Link>
        </section>
      </main>
    );
  }

  return (
    <main className="mx-auto max-w-7xl px-6 py-16">
      <section className="flex items-start justify-between gap-4">
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
            curation
          </p>
          <h1 className="mt-3 text-3xl font-bold tracking-tight md:text-4xl">
            프로젝트 이미지 큐레이션
          </h1>
          <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-600 md:text-base">
            AI 분석 및 그룹핑된 이미지 중에서 프로젝트 상세 페이지에 노출할 대표 이미지를
            선택합니다.
          </p>
          <p className="mt-2 text-sm text-slate-500">
            현재 사용자: {user.nickname} ({user.role})
          </p>
        </div>

        <Link
          href={`/projects/${projectId}`}
          className="rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold transition hover:bg-slate-50"
        >
          프로젝트로 돌아가기
        </Link>
      </section>

      {errorMessage && (
        <div className="mt-6 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {errorMessage}
        </div>
      )}

      {message && (
        <div className="mt-6 rounded-2xl border border-green-200 bg-green-50 px-4 py-3 text-sm text-green-700">
          {message}
        </div>
      )}

      <section className="mt-10 grid gap-8 lg:grid-cols-[360px_minmax(0,1fr)]">
        <aside className="rounded-[2rem] border border-slate-200 bg-white p-5 shadow-sm">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-bold">이미지 그룹</h2>
            <span className="text-sm text-slate-500">{groups.length}개</span>
          </div>

          {isLoadingGroups ? (
            <div className="mt-6 rounded-2xl bg-slate-50 p-6 text-sm text-slate-500">
              그룹 목록을 불러오는 중입니다.
            </div>
          ) : groups.length === 0 ? (
            <div className="mt-6 rounded-2xl bg-slate-50 p-6 text-sm text-slate-500">
              아직 생성된 이미지 그룹이 없습니다.
            </div>
          ) : (
            <div className="mt-6 space-y-4">
              {groups.map((group) => {
                const isSelected = selectedGroup?.imageGroupId === group.imageGroupId;

                return (
                  <button
                    key={group.imageGroupId}
                    type="button"
                    onClick={() => handleSelectGroup(group)}
                    className={`w-full rounded-[1.5rem] border p-4 text-left transition ${
                      isSelected
                        ? "border-slate-900 bg-slate-50"
                        : "border-slate-200 hover:bg-slate-50"
                    }`}
                  >
                    <div className="overflow-hidden rounded-xl bg-slate-100">
                      {group.representativeImageUrl ? (
                        <img
                          src={`${BASE_URL}${group.representativeImageUrl}`}
                          alt={`group-${group.imageGroupId}`}
                          className="aspect-[4/3] w-full object-cover"
                        />
                      ) : (
                        <div className="aspect-[4/3] w-full bg-slate-100" />
                      )}
                    </div>

                    <div className="mt-3">
                      <p className="text-sm font-semibold text-slate-900">
                        그룹 #{group.imageGroupId}
                      </p>
                      <p className="mt-1 text-xs text-slate-500">
                        groupKey: {group.groupKey}
                      </p>
                      <p className="mt-2 text-sm text-slate-600">
                        이미지 수: {group.itemCount}장
                      </p>
                    </div>
                  </button>
                );
              })}
            </div>
          )}
        </aside>

        <section className="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm">
          <div className="flex flex-col gap-2 md:flex-row md:items-end md:justify-between">
            <div>
              <h2 className="text-xl font-bold">그룹 상세 이미지</h2>
              <p className="mt-1 text-sm text-slate-500">
                그룹 내 이미지 중 하나를 선택한 뒤 큐레이션 저장을 진행합니다.
              </p>
            </div>

            {selectedGroup && (
              <div className="text-sm text-slate-500">
                선택 그룹: #{selectedGroup.imageGroupId}
              </div>
            )}
          </div>

          {isLoadingItems ? (
            <div className="mt-6 rounded-2xl bg-slate-50 p-6 text-sm text-slate-500">
              그룹 상세 이미지를 불러오는 중입니다.
            </div>
          ) : !selectedGroup ? (
            <div className="mt-6 rounded-2xl bg-slate-50 p-6 text-sm text-slate-500">
              왼쪽에서 그룹을 선택해 주세요.
            </div>
          ) : groupItems.length === 0 ? (
            <div className="mt-6 rounded-2xl bg-slate-50 p-6 text-sm text-slate-500">
              그룹에 포함된 이미지가 없습니다.
            </div>
          ) : (
            <>
              <div className="mt-6 grid gap-5 sm:grid-cols-2 xl:grid-cols-3">
                {groupItems.map((item) => {
                    const isSelected = selectedImageId === item.fileId;
                    const isAlreadyCurated = selectedImageSet.has(item.fileId);

                    return (
                        <button
                        key={item.fileId}
                        type="button"
                        disabled={isAlreadyCurated}
                        onClick={() => {
                            if (!isAlreadyCurated) {
                            setSelectedImageId(item.fileId);
                            }
                        }}
                        className={`overflow-hidden rounded-[1.5rem] border bg-white text-left shadow-sm transition ${
                            isSelected
                            ? "border-slate-900 ring-2 ring-slate-200"
                            : "border-slate-200 hover:bg-slate-50"
                        } ${isAlreadyCurated ? "opacity-50 cursor-not-allowed" : ""}`}
                        >
                        <div className="aspect-[4/5] overflow-hidden bg-slate-100">
                            <img
                            src={`${BASE_URL}${item.fileUrl}`}
                            alt={item.originalFileName}
                            className="h-full w-full object-cover"
                            />
                        </div>

                        <div className="p-4">

                            {/* 상단 */}
                            <div className="flex items-center justify-between">
                            <p className="text-sm font-semibold text-slate-900">
                                {item.originalFileName}
                            </p>

                            {isAlreadyCurated && (
                                <span className="rounded-full bg-blue-100 px-3 py-1 text-[11px] font-semibold text-blue-700">
                                선정됨
                                </span>
                            )}
                            </div>

                            {/* AI 상태 */}
                            <div className="mt-2">
                            <AiReviewBadge status={item.aiReviewStatus} />
                            </div>

                            <p className="mt-2 text-xs text-slate-500">
                            {item.aiReviewSummary}
                            </p>

                        </div>
                        </button>
                    );
                })}
              </div>

              <div className="mt-8 rounded-[1.5rem] border border-slate-200 bg-slate-50 p-5">
                <h3 className="text-lg font-bold">큐레이션 저장</h3>

                <div className="mt-5 grid gap-5 md:grid-cols-2">
                  <div>
                    <label className="block text-sm font-semibold text-slate-700">
                      선택된 이미지 ID
                    </label>
                    <input
                      value={selectedImageId ?? ""}
                      readOnly
                      className="mt-2 w-full rounded-xl border border-slate-300 bg-white px-4 py-3 text-sm"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-semibold text-slate-700">
                      우선순위
                    </label>
                    <input
                      type="number"
                      min={1}
                      value={priorityOrder}
                      onChange={(e) => setPriorityOrder(Number(e.target.value))}
                      className="mt-2 w-full rounded-xl border border-slate-300 bg-white px-4 py-3 text-sm outline-none transition focus:border-slate-500"
                    />
                  </div>
                </div>

                <div className="mt-5">
                  <label className="block text-sm font-semibold text-slate-700">
                    선정 이유
                  </label>
                  <textarea
                    value={selectedReason}
                    onChange={(e) => setSelectedReason(e.target.value)}
                    placeholder="예: 구도가 안정적이고 해상도가 좋아 대표 이미지로 적합"
                    className="mt-2 h-28 w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-sm outline-none transition focus:border-slate-500"
                  />
                </div>

                <button
                  type="button"
                  onClick={handleSubmitCuration}
                  disabled={isSubmitting || !selectedImageId}
                  className="mt-6 rounded-full bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:bg-slate-400"
                >
                  {isSubmitting ? "저장 중..." : "큐레이션 저장"}
                </button>
              </div>
            </>
          )}
        </section>
      </section>
    </main>
  );
}

function formatFileSize(bytes: number) {
  if (!bytes || Number.isNaN(bytes)) {
    return "0 B";
  }

  const units = ["B", "KB", "MB", "GB"];
  let size = bytes;
  let unitIndex = 0;

  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024;
    unitIndex += 1;
  }

  return `${size.toFixed(unitIndex === 0 ? 0 : 1)} ${units[unitIndex]}`;
}

function formatScore(value: number | null) {
  if (value === null || Number.isNaN(value)) {
    return "-";
  }

  return value.toFixed(2);
}

function AiReviewBadge({
  status,
}: {
  status: "PASS" | "FLAGGED" | "REJECT_SUGGESTED" | "UNKNOWN";
}) {
  if (status === "PASS") {
    return (
      <span className="rounded-full bg-green-100 px-3 py-1 text-[11px] font-semibold text-green-700">
        PASS
      </span>
    );
  }

  if (status === "FLAGGED") {
    return (
      <span className="rounded-full bg-yellow-100 px-3 py-1 text-[11px] font-semibold text-yellow-700">
        주의
      </span>
    );
  }

  if (status === "REJECT_SUGGESTED") {
    return (
      <span className="rounded-full bg-red-100 px-3 py-1 text-[11px] font-semibold text-red-700">
        비추천
      </span>
    );
  }

  return (
    <span className="rounded-full bg-slate-100 px-3 py-1 text-[11px] font-semibold text-slate-600">
      분석 전
    </span>
  );
}