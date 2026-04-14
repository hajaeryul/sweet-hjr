export type CuratedImagePreview = {
  curatedImageId: number;
  sourceImageId: number;
  fileUrl: string | null;
  priorityOrder: number;
  selectedReason: string | null;
};