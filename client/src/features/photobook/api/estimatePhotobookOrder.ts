import {
  PhotobookOrderEstimateRequest,
  PhotobookOrderEstimateResponse,
} from "../types/order";

const BASE_URL = "http://localhost:8080";

export async function estimatePhotobookOrder(
  projectId: number,
  payload: PhotobookOrderEstimateRequest
): Promise<PhotobookOrderEstimateResponse> {
  const response = await fetch(
    `${BASE_URL}/api/projects/${projectId}/photobooks/order-estimate`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      cache: "no-store",
      body: JSON.stringify(payload),
    }
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "포토북 견적 조회에 실패했습니다.");
  }

  return response.json();
}