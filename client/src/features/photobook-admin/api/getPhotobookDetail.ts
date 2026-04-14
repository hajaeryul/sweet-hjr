import { PhotobookDetail } from "../types/photobookAdmin";

const BASE_URL = "http://localhost:8080";

export async function getPhotobookDetail(
  projectId: string | number
): Promise<PhotobookDetail> {
  const response = await fetch(
    `${BASE_URL}/api/admin/projects/${projectId}/photobooks`,
    { cache: "no-store" }
  );

  if (!response.ok) {
    throw new Error("생성된 포토북 상세 조회에 실패했습니다.");
  }

  return response.json();
}