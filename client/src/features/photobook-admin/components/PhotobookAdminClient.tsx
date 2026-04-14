"use client";

import { useEffect, useState } from "react";
import { useAuth } from "@/features/auth/context/AuthContext";
import { createPhotobook } from "../api/createPhotobook";
import { getPhotobookDetail } from "../api/getPhotobookDetail";
import { getPhotobookLayout } from "../api/getPhotobookLayout";
import { getSweetbookTemplateDetail } from "../api/getSweetbookTemplateDetail";
import { getSweetbookTemplates } from "../api/getSweetbookTemplates";
import PhotobookCreatePanel from "./PhotobookCreatePanel";
import PhotobookLayoutPreviewPanel from "./PhotobookLayoutPreview";
import PhotobookResultPanel from "./PhotobookResultPanel";
import TemplateDetailPanel from "./TemplateDetailPanel";
import TemplateSelector from "./TemplateSelector";
import {
  PhotobookCreateResponse,
  PhotobookDetail,
  PhotobookLayoutPreview,
  TemplateDetail,
  TemplateSummary,
} from "../types/photobookAdmin";

type Props = {
  projectId: string;
};

const DEFAULT_BOOK_SPEC_UID = "SQUAREBOOK_HC";

export default function PhotobookAdminClient({ projectId }: Props) {
  const { user, isLoggedIn } = useAuth();

  const [bookSpecUid, setBookSpecUid] = useState(DEFAULT_BOOK_SPEC_UID);
  const [title, setTitle] = useState(
    "리그오브레전드의 전설 T1 페이커의 팬 참여형 포토북 프로젝트"
  );

  const [layout, setLayout] = useState<PhotobookLayoutPreview | null>(null);

  const [coverTemplates, setCoverTemplates] = useState<TemplateSummary[]>([]);
  const [contentTemplates, setContentTemplates] = useState<TemplateSummary[]>([]);

  const [selectedCoverTemplateUid, setSelectedCoverTemplateUid] = useState("");
  const [selectedContentTemplateUid, setSelectedContentTemplateUid] = useState("");

  const [selectedCoverTemplateDetail, setSelectedCoverTemplateDetail] =
    useState<TemplateDetail | null>(null);
  const [selectedContentTemplateDetail, setSelectedContentTemplateDetail] =
    useState<TemplateDetail | null>(null);

  const [createdPhotobook, setCreatedPhotobook] =
    useState<PhotobookCreateResponse | null>(null);
  const [photobookDetail, setPhotobookDetail] = useState<PhotobookDetail | null>(null);

  const [isLoading, setIsLoading] = useState(true);
  const [isCreating, setIsCreating] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const canCreate =
    !!layout &&
    !!layout.cover &&
    layout.pages.length > 0 &&
    !!selectedCoverTemplateUid &&
    !!selectedContentTemplateUid &&
    !!user &&
    user.role === "ADMIN" &&
    !isCreating;

  useEffect(() => {
    async function initialize() {
      setIsLoading(true);
      setError("");
      try {
        const [layoutData, coverData, contentData] = await Promise.all([
          getPhotobookLayout(projectId, bookSpecUid),
          getSweetbookTemplates(bookSpecUid, "cover"),
          getSweetbookTemplates(bookSpecUid, "content"),
        ]);

        setLayout(layoutData);
        setCoverTemplates(coverData);
        setContentTemplates(contentData);

        if (coverData.length > 0) {
          const firstCoverUid = coverData[0].templateUid;
          setSelectedCoverTemplateUid(firstCoverUid);
          const detail = await getSweetbookTemplateDetail(firstCoverUid);
          setSelectedCoverTemplateDetail(detail);
        }

        if (contentData.length > 0) {
          const firstContentUid = contentData[0].templateUid;
          setSelectedContentTemplateUid(firstContentUid);
          const detail = await getSweetbookTemplateDetail(firstContentUid);
          setSelectedContentTemplateDetail(detail);
        }

        try {
          const detail = await getPhotobookDetail(projectId);
          setPhotobookDetail(detail);
        } catch {
          setPhotobookDetail(null);
        }
      } catch (e) {
        setError(getErrorMessage(e, "초기 데이터 조회에 실패했습니다."));
      } finally {
        setIsLoading(false);
      }
    }

    initialize();
  }, [projectId, bookSpecUid]);

  useEffect(() => {
    async function fetchCoverDetail() {
      if (!selectedCoverTemplateUid) return;
      try {
        const detail = await getSweetbookTemplateDetail(selectedCoverTemplateUid);
        setSelectedCoverTemplateDetail(detail);
      } catch (e) {
        setError(getErrorMessage(e, "커버 템플릿 상세 조회에 실패했습니다."));
      }
    }

    fetchCoverDetail();
  }, [selectedCoverTemplateUid]);

  useEffect(() => {
    async function fetchContentDetail() {
      if (!selectedContentTemplateUid) return;
      try {
        const detail = await getSweetbookTemplateDetail(selectedContentTemplateUid);
        setSelectedContentTemplateDetail(detail);
      } catch (e) {
        setError(getErrorMessage(e, "내지 템플릿 상세 조회에 실패했습니다."));
      }
    }

    fetchContentDetail();
  }, [selectedContentTemplateUid]);

  const handleCreatePhotobook = async () => {
    if (!user || user.role !== "ADMIN") {
      setError("관리자만 포토북을 생성할 수 있습니다.");
      return;
    }

    setIsCreating(true);
    setError("");
    setMessage("");

    try {
      const created = await createPhotobook(projectId, {
        adminUserId: user.id,
        title,
        bookSpecUid,
        specProfileUid: null,
        coverTemplateUid: selectedCoverTemplateUid,
        contentTemplateUid: selectedContentTemplateUid,
        uploadToSweetbook: false,
      });

      setCreatedPhotobook(created);
      setMessage("포토북 생성이 완료되었습니다.");

      const detail = await getPhotobookDetail(projectId);
      setPhotobookDetail(detail);
    } catch (e) {
      setError(getErrorMessage(e, "포토북 생성에 실패했습니다."));
    } finally {
      setIsCreating(false);
    }
  };

  if (!isLoggedIn || !user) {
    return (
      <div className="rounded-2xl border border-slate-200 bg-white p-8 text-sm text-slate-600 shadow-sm">
        관리자 로그인 후 접근할 수 있습니다.
      </div>
    );
  }

  if (user.role !== "ADMIN") {
    return (
      <div className="rounded-2xl border border-rose-200 bg-rose-50 p-8 text-sm text-rose-700 shadow-sm">
        현재 계정 역할은 {user.role}입니다. 관리자 계정으로 로그인해야 접근할 수 있습니다.
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="rounded-2xl border border-slate-200 bg-white p-8 text-sm text-slate-600 shadow-sm">
        포토북 관리 데이터를 불러오는 중입니다...
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {(error || message) && (
        <div
          className={`rounded-2xl border p-4 text-sm shadow-sm ${
            error
              ? "border-rose-200 bg-rose-50 text-rose-700"
              : "border-emerald-200 bg-emerald-50 text-emerald-700"
          }`}
        >
          {error || message}
        </div>
      )}

      <PhotobookCreatePanel
        title={title}
        setTitle={setTitle}
        bookSpecUid={bookSpecUid}
        setBookSpecUid={setBookSpecUid}
        adminUserId={user.id}
        setAdminUserId={() => {}}
        selectedCoverTemplateUid={selectedCoverTemplateUid}
        selectedContentTemplateUid={selectedContentTemplateUid}
        expectedFinalPageCount={layout?.expectedFinalPageCount ?? null}
        canCreate={canCreate}
        isCreating={isCreating}
        onCreate={handleCreatePhotobook}
      />

      <div className="grid gap-6 xl:grid-cols-2">
        <PhotobookLayoutPreviewPanel layout={layout} />
        <PhotobookResultPanel
          createdPhotobook={createdPhotobook}
          photobookDetail={photobookDetail}
        />
      </div>

      <TemplateSelector
        title="커버 템플릿 선택"
        templates={coverTemplates}
        selectedTemplateUid={selectedCoverTemplateUid}
        onSelect={setSelectedCoverTemplateUid}
      />

      <TemplateDetailPanel
        title="선택된 커버 템플릿 상세"
        detail={selectedCoverTemplateDetail}
      />

      <TemplateSelector
        title="내지 템플릿 선택"
        templates={contentTemplates}
        selectedTemplateUid={selectedContentTemplateUid}
        onSelect={setSelectedContentTemplateUid}
      />

      <TemplateDetailPanel
        title="선택된 내지 템플릿 상세"
        detail={selectedContentTemplateDetail}
      />
    </div>
  );
}

function getErrorMessage(error: unknown, fallback: string) {
  if (error instanceof Error && error.message) return error.message;
  return fallback;
}