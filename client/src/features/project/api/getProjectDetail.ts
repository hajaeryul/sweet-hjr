import { ProjectDetail } from "../types/project";

const BASE_URL = "http://localhost:8080";

export async function getProjectDetail(projectId: string): Promise<ProjectDetail> {
  const response = await fetch(`${BASE_URL}/api/projects/${projectId}`, {
    cache: "no-store",
  });

  if (!response.ok) {
    throw new Error("프로젝트 상세 조회에 실패했습니다.");
  }

  return response.json();
}