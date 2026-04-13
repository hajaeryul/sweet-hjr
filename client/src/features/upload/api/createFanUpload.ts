type CreateFanUploadRequest = {
  userId: number;
  memo: string;
};

type CreateFanUploadResponse = {
  fanUploadId: number;
  projectId: number;
  userId: number;
  status: string;
  memo: string;
};

const BASE_URL = "http://localhost:8080";

export async function createFanUpload(
  projectId: string,
  payload: CreateFanUploadRequest
): Promise<CreateFanUploadResponse> {
  const response = await fetch(`${BASE_URL}/api/projects/${projectId}/uploads`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error("업로드 세션 생성에 실패했습니다.");
  }

  return response.json();
}