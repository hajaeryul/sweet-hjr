import { CurationGroupSummary } from "@/features/upload/types/curation";

const BASE_URL = "http://localhost:8080";

export async function getCurationGroups(
  projectId: string | number
): Promise<CurationGroupSummary[]> {
  const response = await fetch(
    `${BASE_URL}/api/projects/${projectId}/curation/groups`,
    {
      cache: "no-store",
    }
  );

  if (!response.ok) {
    throw new Error("큐레이션 그룹 목록을 불러오지 못했습니다.");
  }

  return response.json();
}