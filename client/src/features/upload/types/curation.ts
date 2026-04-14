export type CurationGroupSummary = {
  imageGroupId: number;
  groupKey: string;
  representativeImageId: number | null;
  representativeImageUrl: string | null;
  itemCount: number;
};

export type CurationGroupItem = {
  fileId: number;
  userId: number;
  userNickname: string;
  originalFileName: string;
  fileUrl: string;
  width: number;
  height: number;
  fileSize: number;

  aiReviewStatus: "PASS" | "FLAGGED" | "REJECT_SUGGESTED" | "UNKNOWN";
  aiReviewSummary: string;

  resolutionScore: number | null;
  brightnessScore: number | null;
  blurScore: number | null;
  noiseScore: number | null;

  watermarkDetected: boolean | null;
  textDetected: boolean | null;
  inappropriateFlag: boolean | null;
};

export type CurateImageRequest = {
  sourceImageId: number;
  imageGroupId: number;
  selectedBy: number;
  selectedReason?: string;
  priorityOrder: number;
};

export type CurateImageResponse = {
  curatedImageId: number;
  projectId: number;
  sourceImageId: number;
  imageGroupId: number;
  sourceType: string;
  priorityOrder: number;
};