import {
  PhotobookOrderCreateRequest,
  PhotobookOrderCreateResponse,
} from "../types/order";

const BASE_URL = "http://localhost:8080";

export async function createPhotobookOrder(
  projectId: number,
  payload: PhotobookOrderCreateRequest
): Promise<PhotobookOrderCreateResponse> {
  const response = await fetch(
    `${BASE_URL}/api/projects/${projectId}/photobooks/orders`,
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
    throw new Error(message || "포토북 주문 생성에 실패했습니다.");
  }

  return response.json();
}