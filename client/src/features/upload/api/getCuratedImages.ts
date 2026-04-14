import { CuratedImagePreview } from "@/features/upload/types/curatedImage";

const BASE_URL = "http://localhost:8080";

export async function getCuratedImages(
  projectId: string | number
): Promise<CuratedImagePreview[]> {
  const response = await fetch(
    `${BASE_URL}/api/projects/${projectId}/curation/preview`,
    {
      cache: "no-store",
    }
  );

  if (!response.ok) {
    throw new Error("선정된 이미지 미리보기를 불러오지 못했습니다.");
  }

  return response.json();
}