import { MyFanUpload } from "../types/upload";

const BASE_URL = "http://localhost:8080";

/**
 * 지금은 인증이 없어서 userId를 직접 받는다.
 * 나중에 로그인 붙이면 이 파라미터는 제거하고 서버에서 인증 사용자 기준으로 바꾸면 된다.
 */
export async function getMyUploads(userId: number): Promise<MyFanUpload[]> {
  const response = await fetch(`${BASE_URL}/api/uploads/my?userId=${userId}`, {
    cache: "no-store",
  });

  if (!response.ok) {
    throw new Error("내 업로드 목록 조회에 실패했습니다.");
  }

  return response.json();
}