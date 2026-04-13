type UploadedFileItemResponse = {
  fileId: number;
  originalFileName: string;
  fileUrl: string;
  mimeType: string;
  fileSize: number;
  width: number;
  height: number;
};

type UploadFilesResponse = {
  fanUploadId: number;
  uploadedCount: number;
  files: UploadedFileItemResponse[];
};

const BASE_URL = "http://localhost:8080";

export async function uploadFiles(
  projectId: string,
  fanUploadId: number,
  userId: number,
  files: File[]
): Promise<UploadFilesResponse> {
  const formData = new FormData();

  formData.append("userId", String(userId));

  files.forEach((file) => {
    formData.append("files", file);
  });

  const response = await fetch(
    `${BASE_URL}/api/projects/${projectId}/uploads/${fanUploadId}/files`,
    {
      method: "POST",
      body: formData,
    }
  );

  if (!response.ok) {
    throw new Error("파일 업로드에 실패했습니다.");
  }

  return response.json();
}