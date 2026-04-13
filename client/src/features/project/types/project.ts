export type ProjectPhase =
  | "DRAFT"
  | "BEFORE_UPLOAD"
  | "UPLOAD_OPEN"
  | "PREVIEW_OPEN"
  | "ORDER_OPEN"
  | "CLOSED";

export type ProjectSummary = {
  id: number;
  title: string;
  description: string;
  coverImageUrl: string;
  status: string;
  influencerId: number;
  influencerName: string;
  influencerDisplayName: string;
  uploadStartAt: string;
  uploadEndAt: string;
  uploadAvailable: boolean;
  projectPhase: ProjectPhase;
};

export type ProjectDetail = {
  id: number;
  title: string;
  description: string;
  coverImageUrl: string;
  status: string;
  influencerId: number;
  influencerName: string;
  influencerDisplayName: string;
  influencerProfileImageUrl: string;
  createdByUserId: number;
  createdByNickname: string;
  uploadStartAt: string;
  uploadEndAt: string;
  previewStartAt: string;
  previewEndAt: string;
  orderStartAt: string;
  orderEndAt: string;
  uploadAvailable: boolean;
  previewAvailable: boolean;
  orderAvailable: boolean;
  projectPhase: ProjectPhase;
};