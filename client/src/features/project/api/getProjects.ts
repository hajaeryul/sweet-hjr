import { ProjectSummary } from "../types/project";

// 나중에 환경변수로 뺄 수 있지만 지금은 테스트 목적이라 고정값으로 둔다.
const BASE_URL = "http://localhost:8080";

export async function getProjects(): Promise<ProjectSummary[]> {
  const response = await fetch(`${BASE_URL}/api/projects`, {
    // 개발 중에는 항상 최신 데이터 보려고 no-store 사용
    cache: "no-store",
  });

  if (!response.ok) {
    throw new Error("프로젝트 목록 조회에 실패했습니다.");
  }

  return response.json();
}