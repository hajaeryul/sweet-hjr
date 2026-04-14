import { CurationGroupItem } from "@/features/upload/types/curation";

const BASE_URL = "http://localhost:8080";

export async function getCurationGroupItems(
  projectId: string | number,
  imageGroupId: number
): Promise<CurationGroupItem[]> {
  const response = await fetch(
    `${BASE_URL}/api/projects/${projectId}/curation/groups/${imageGroupId}/items`,
    {
      cache: "no-store",
    }
  );

  if (!response.ok) {
    throw new Error("그룹 상세 이미지를 불러오지 못했습니다.");
  }

  return response.json();
}