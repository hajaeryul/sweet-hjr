export type PhotobookOrderEstimateRequest = {
  quantity: number;
};

export type PhotobookOrderEstimateResponse = {
  projectId: number;
  photobookId: number;
  photobookTitle: string;
  sweetbookBookUid: string;
  quantity: number;
  totalProductAmount: number;
  totalShippingFee: number;
  totalPackagingFee: number;
  totalAmount: number;
  paidCreditAmount: number;
  creditBalance: number;
  creditSufficient: boolean;
  currency: string;
};

export type PhotobookOrderCreateRequest = {
  userId: number;
  quantity: number;
  recipientName: string;
  recipientPhone: string;
  postalCode: string;
  address1: string;
  address2?: string;
  memo?: string;
  requestKey?: string;
};

export type PhotobookOrderItemSummary = {
  itemUid: string;
  bookUid: string;
  bookSpecUid: string;
  bookSpecName: string;
  quantity: number;
  pageCount: number;
  unitPrice: number;
  itemAmount: number;
  itemStatus: number;
  itemStatusDisplay: string;
};

export type PhotobookOrderCreateResponse = {
  projectId: number;
  photobookId: number;
  userId: number;
  userNickname: string;
  orderUid: string;
  orderStatus: number;
  orderStatusDisplay: string;
  isTest: boolean;
  totalProductAmount: number;
  totalShippingFee: number;
  totalPackagingFee: number;
  totalAmount: number;
  paidCreditAmount: number;
  creditBalanceAfter: number;
  recipientName: string;
  recipientPhone: string;
  postalCode: string;
  address1: string;
  address2: string;
  shippingMemo: string;
  orderedAt: string;
  items: PhotobookOrderItemSummary[];
};