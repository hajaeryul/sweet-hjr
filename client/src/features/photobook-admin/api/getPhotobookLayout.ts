import { PhotobookLayoutPreview } from "../types/photobookAdmin";

const BASE_URL = "http://localhost:8080";

export async function getPhotobookLayout(
  projectId: string | number,
  bookSpecUid: string
): Promise<PhotobookLayoutPreview> {
  const response = await fetch(
    `${BASE_URL}/api/admin/projects/${projectId}/photobook-layout?bookSpecUid=${encodeURIComponent(bookSpecUid)}`,
    { cache: "no-store" }
  );

  if (!response.ok) {
    throw new Error("포토북 레이아웃 조회에 실패했습니다.");
  }

  return response.json();
}