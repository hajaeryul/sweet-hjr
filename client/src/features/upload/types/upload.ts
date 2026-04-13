export type MyUploadFile = {
  fileId: number;
  originalFileName: string;
  fileUrl: string;
  mimeType: string;
  fileSize: number;
  width: number;
  height: number;
};

export type MyFanUpload = {
  fanUploadId: number;
  projectId: number;
  projectTitle: string;
  status: "PENDING" | "FILTERED" | "APPROVED" | "REJECTED";
  memo: string | null;
  createdAt: string;
  files: MyUploadFile[];
};