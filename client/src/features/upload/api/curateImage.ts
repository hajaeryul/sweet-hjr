import {
  CurateImageRequest,
  CurateImageResponse,
} from "@/features/upload/types/curation";

const BASE_URL = "http://localhost:8080";

export async function curateImage(
  projectId: string | number,
  payload: CurateImageRequest
): Promise<CurateImageResponse> {
  const response = await fetch(
    `${BASE_URL}/api/projects/${projectId}/curation/images`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    }
  );

  if (!response.ok) {
    throw new Error("큐레이션 이미지 저장에 실패했습니다.");
  }

  return response.json();
}