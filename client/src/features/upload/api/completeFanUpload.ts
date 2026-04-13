type CompleteFanUploadResponse = {
  fanUploadId: number;
  status: string;
  totalFileCount: number;
};

const BASE_URL = "http://localhost:8080";

export async function completeFanUpload(
  projectId: string,
  fanUploadId: number,
  userId: number
): Promise<CompleteFanUploadResponse> {
  const response = await fetch(
    `${BASE_URL}/api/projects/${projectId}/uploads/${fanUploadId}/complete`,
    {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ userId }),
    }
  );

  if (!response.ok) {
    throw new Error("업로드 완료 처리에 실패했습니다.");
  }

  return response.json();
}