import { TemplateSummary } from "../types/photobookAdmin";

const BASE_URL = "http://localhost:8080";

export async function getSweetbookTemplates(
  bookSpecUid: string,
  templateKind: "cover" | "content"
): Promise<TemplateSummary[]> {
  const response = await fetch(
    `${BASE_URL}/api/admin/sweetbook/templates?bookSpecUid=${encodeURIComponent(bookSpecUid)}&templateKind=${templateKind}`,
    { cache: "no-store" }
  );

  if (!response.ok) {
    throw new Error(`${templateKind} 템플릿 목록 조회에 실패했습니다.`);
  }

  return response.json();
}