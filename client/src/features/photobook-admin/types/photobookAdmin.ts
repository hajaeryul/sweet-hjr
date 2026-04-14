export type PhotobookLayoutPage = {
  curatedImageId: number;
  pageNumber: number;
  pageRole: string;
  imageUrl: string;
  caption: string | null;
  priorityOrder: number;
};

export type PhotobookLayoutPreview = {
  projectId: number;
  contentPageCount: number;
  expectedFinalPageCount: number;
  cover: PhotobookLayoutPage | null;
  pages: PhotobookLayoutPage[];
};

export type TemplateSummary = {
  templateUid: string;
  templateName: string;
  description: string | null;
  theme: string | null;
  templateKind: string;
  bookSpecUid: string;
  status: string;
  layoutThumbnailUrl: string | null;
  requiredParameterCount: number;
};

export type TemplateParameter = {
  key: string;
  binding: string | null;
  required: boolean | null;
  type: string | null;
  label: string | null;
  description: string | null;
};

export type TemplateDetail = {
  templateUid: string;
  templateName: string;
  description: string | null;
  theme: string | null;
  templateKind: string;
  bookSpecUid: string;
  status: string;
  layoutThumbnailUrl: string | null;
  mockupThumbnailUrl: string | null;
  requiredParameterCount: number;
  parameters: TemplateParameter[];
};

export type PhotobookCreateRequest = {
  adminUserId: number;
  title: string;
  bookSpecUid: string;
  specProfileUid: string | null;
  coverTemplateUid: string;
  contentTemplateUid: string;
  uploadToSweetbook: boolean;
};

export type PhotobookCreateResponse = {
  photobookId: number;
  projectId: number;
  sweetbookBookUid: string | null;
  status: string;
  finalPageCount: number | null;
};

export type PhotobookPagePreview = {
  curatedImageId: number;
  pageNumber: number;
  pageRole: string;
  imageUrl: string;
  caption: string | null;
  priorityOrder: number;
};

export type PhotobookDetail = {
  photobookId: number;
  projectId: number;
  title: string;
  bookSpecUid: string;
  sweetbookBookUid: string | null;
  status: string;
  finalPageCount: number | null;
  lastErrorMessage: string | null;
  pages: PhotobookPagePreview[];
};