import { ProjectPhase } from "../types/project";

export function getProjectPhaseLabel(phase: ProjectPhase): string {
  switch (phase) {
    case "UPLOAD_OPEN":
      return "사진 업로드 진행 중";
    case "PREVIEW_OPEN":
      return "미리보기 공개 중";
    case "ORDER_OPEN":
      return "주문 진행 중";
    case "BEFORE_UPLOAD":
      return "오픈 예정";
    case "CLOSED":
      return "종료됨";
    case "DRAFT":
      return "준비 중";
    default:
      return "상태 확인 필요";
  }
}