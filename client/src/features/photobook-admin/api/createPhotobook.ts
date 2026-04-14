import {
  PhotobookCreateRequest,
  PhotobookCreateResponse,
} from "../types/photobookAdmin";

const BASE_URL = "http://localhost:8080";

export async function createPhotobook(
  projectId: string | number,
  body: PhotobookCreateRequest
): Promise<PhotobookCreateResponse> {
  const response = await fetch(
    `${BASE_URL}/api/admin/projects/${projectId}/photobooks`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
      cache: "no-store",
    }
  );

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || "포토북 생성에 실패했습니다.");
  }

  return response.json();
}