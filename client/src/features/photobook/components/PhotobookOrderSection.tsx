"use client";

import { useMemo, useState } from "react";
import { useAuth } from "@/features/auth/context/AuthContext";
import { estimatePhotobookOrder } from "../api/estimatePhotobookOrder";
import { createPhotobookOrder } from "../api/createPhotobookOrder";
import {
  PhotobookOrderCreateResponse,
  PhotobookOrderEstimateResponse,
} from "../types/order";

type Props = {
  projectId: number;
  orderAvailable: boolean;
};

type OrderFormState = {
  quantity: number;
  recipientName: string;
  recipientPhone: string;
  postalCode: string;
  address1: string;
  address2: string;
  memo: string;
};

const INITIAL_FORM: OrderFormState = {
  quantity: 1,
  recipientName: "",
  recipientPhone: "",
  postalCode: "",
  address1: "",
  address2: "",
  memo: "",
};

export default function PhotobookOrderSection({
  projectId,
  orderAvailable,
}: Props) {
  const { isLoggedIn, user } = useAuth();

  const [form, setForm] = useState<OrderFormState>(INITIAL_FORM);
  const [estimate, setEstimate] =
    useState<PhotobookOrderEstimateResponse | null>(null);
  const [orderResult, setOrderResult] =
    useState<PhotobookOrderCreateResponse | null>(null);
  const [isEstimating, setIsEstimating] = useState(false);
  const [isOrdering, setIsOrdering] = useState(false);
  const [message, setMessage] = useState("");

  const canOrder = useMemo(() => {
    return orderAvailable && isLoggedIn && !!user;
  }, [orderAvailable, isLoggedIn, user]);

  const updateField = (
    key: keyof OrderFormState,
    value: string | number
  ) => {
    setForm((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const handleEstimate = async () => {
    try {
      setIsEstimating(true);
      setMessage("");
      setOrderResult(null);

      const result = await estimatePhotobookOrder(projectId, {
        quantity: form.quantity,
      });

      setEstimate(result);
    } catch (error) {
      console.error(error);
      setMessage(
        error instanceof Error
          ? error.message
          : "견적 조회 중 문제가 발생했습니다."
      );
    } finally {
      setIsEstimating(false);
    }
  };

  const handleOrder = async () => {
    if (!user) {
      setMessage("로그인 후 주문할 수 있습니다.");
      return;
    }

    if (!estimate) {
      setMessage("먼저 견적 확인을 진행해 주세요.");
      return;
    }

    if (!form.recipientName.trim()) {
      setMessage("수령인 이름을 입력해 주세요.");
      return;
    }

    if (!form.recipientPhone.trim()) {
      setMessage("연락처를 입력해 주세요.");
      return;
    }

    if (!form.postalCode.trim()) {
      setMessage("우편번호를 입력해 주세요.");
      return;
    }

    if (!form.address1.trim()) {
      setMessage("기본 주소를 입력해 주세요.");
      return;
    }

    try {
      setIsOrdering(true);
      setMessage("");

      const result = await createPhotobookOrder(projectId, {
        userId: user.id,
        quantity: form.quantity,
        recipientName: form.recipientName,
        recipientPhone: form.recipientPhone,
        postalCode: form.postalCode,
        address1: form.address1,
        address2: form.address2,
        memo: form.memo,
        requestKey: crypto.randomUUID(),
      });

      setOrderResult(result);
      setMessage("포토북 주문이 완료되었습니다.");
    } catch (error) {
      console.error(error);
      setMessage(
        error instanceof Error
          ? error.message
          : "주문 생성 중 문제가 발생했습니다."
      );
    } finally {
      setIsOrdering(false);
    }
  };

  return (
    <section className="rounded-3xl border border-black/10 bg-white p-6 shadow-sm">
      <div className="mb-6 flex items-start justify-between gap-4">
        <div>
          <div className="mb-2 text-sm font-semibold uppercase tracking-wide text-black/50">
            order
          </div>
          <h2 className="text-2xl font-bold text-black">포토북 주문</h2>
          <p className="mt-2 text-sm leading-6 text-black/65">
            주문 기간에만 견적 확인과 실제 주문이 가능합니다.
          </p>
        </div>

        <span
          className={`rounded-full px-3 py-1 text-xs font-semibold ${
            orderAvailable
              ? "bg-emerald-100 text-emerald-700"
              : "bg-gray-100 text-gray-500"
          }`}
        >
          {orderAvailable ? "주문 가능" : "주문 불가"}
        </span>
      </div>

      {!isLoggedIn && (
        <div className="mb-4 rounded-2xl bg-amber-50 px-4 py-3 text-sm text-amber-700">
          주문은 로그인한 사용자만 가능합니다.
        </div>
      )}

      {!orderAvailable && (
        <div className="mb-4 rounded-2xl bg-gray-100 px-4 py-3 text-sm text-gray-600">
          현재 이 프로젝트는 주문 기간이 아닙니다.
        </div>
      )}

      <div className="grid gap-4 md:grid-cols-2">
        <label className="flex flex-col gap-2 text-sm text-black/70">
          수량
          <input
            type="number"
            min={1}
            max={100}
            value={form.quantity}
            onChange={(e) => updateField("quantity", Number(e.target.value))}
            className="rounded-2xl border border-black/10 px-4 py-3 outline-none transition focus:border-black"
            disabled={!canOrder}
          />
        </label>

        <label className="flex flex-col gap-2 text-sm text-black/70">
          수령인 이름
          <input
            type="text"
            value={form.recipientName}
            onChange={(e) => updateField("recipientName", e.target.value)}
            className="rounded-2xl border border-black/10 px-4 py-3 outline-none transition focus:border-black"
            disabled={!canOrder}
          />
        </label>

        <label className="flex flex-col gap-2 text-sm text-black/70">
          연락처
          <input
            type="text"
            value={form.recipientPhone}
            onChange={(e) => updateField("recipientPhone", e.target.value)}
            className="rounded-2xl border border-black/10 px-4 py-3 outline-none transition focus:border-black"
            disabled={!canOrder}
          />
        </label>

        <label className="flex flex-col gap-2 text-sm text-black/70">
          우편번호
          <input
            type="text"
            value={form.postalCode}
            onChange={(e) => updateField("postalCode", e.target.value)}
            className="rounded-2xl border border-black/10 px-4 py-3 outline-none transition focus:border-black"
            disabled={!canOrder}
          />
        </label>

        <label className="flex flex-col gap-2 text-sm text-black/70 md:col-span-2">
          기본 주소
          <input
            type="text"
            value={form.address1}
            onChange={(e) => updateField("address1", e.target.value)}
            className="rounded-2xl border border-black/10 px-4 py-3 outline-none transition focus:border-black"
            disabled={!canOrder}
          />
        </label>

        <label className="flex flex-col gap-2 text-sm text-black/70 md:col-span-2">
          상세 주소
          <input
            type="text"
            value={form.address2}
            onChange={(e) => updateField("address2", e.target.value)}
            className="rounded-2xl border border-black/10 px-4 py-3 outline-none transition focus:border-black"
            disabled={!canOrder}
          />
        </label>

        <label className="flex flex-col gap-2 text-sm text-black/70 md:col-span-2">
          배송 메모
          <textarea
            value={form.memo}
            onChange={(e) => updateField("memo", e.target.value)}
            className="min-h-[96px] rounded-2xl border border-black/10 px-4 py-3 outline-none transition focus:border-black"
            disabled={!canOrder}
          />
        </label>
      </div>

      <div className="mt-6 flex flex-wrap gap-3">
        <button
          type="button"
          onClick={handleEstimate}
          disabled={!canOrder || isEstimating}
          className="rounded-full bg-black px-5 py-3 text-sm font-semibold text-white transition disabled:cursor-not-allowed disabled:bg-black/30"
        >
          {isEstimating ? "견적 확인 중..." : "견적 확인하기"}
        </button>

        <button
          type="button"
          onClick={handleOrder}
          disabled={!canOrder || isOrdering}
          className="rounded-full border border-black px-5 py-3 text-sm font-semibold text-black transition disabled:cursor-not-allowed disabled:border-black/20 disabled:text-black/30"
        >
          {isOrdering ? "주문 요청 중..." : "포토북 주문하기"}
        </button>
      </div>

      {message && (
        <div className="mt-4 rounded-2xl bg-black/5 px-4 py-3 text-sm text-black/70">
          {message}
        </div>
      )}

      {estimate && (
        <div className="mt-6 rounded-3xl border border-black/10 bg-black/[0.03] p-5">
          <div className="mb-4 text-lg font-bold text-black">견적 확인</div>

          <div className="grid gap-3 text-sm text-black/70 md:grid-cols-2">
            <InfoRow label="포토북명" value={estimate.photobookTitle} />
            <InfoRow label="수량" value={`${estimate.quantity}권`} />
            <InfoRow
              label="상품 금액"
              value={formatWon(estimate.totalProductAmount)}
            />
            <InfoRow
              label="배송비"
              value={formatWon(estimate.totalShippingFee)}
            />
            <InfoRow
              label="포장비"
              value={formatWon(estimate.totalPackagingFee)}
            />
            <InfoRow
              label="총 금액"
              value={formatWon(estimate.totalAmount)}
              strong
            />
            <InfoRow
              label="크레딧 잔액"
              value={formatWon(estimate.creditBalance)}
            />
            <InfoRow
              label="결제 가능 여부"
              value={estimate.creditSufficient ? "가능" : "확인 필요"}
            />
          </div>
        </div>
      )}

      {orderResult && (
        <div className="mt-6 rounded-3xl border border-emerald-200 bg-emerald-50 p-5">
          <div className="mb-4 text-lg font-bold text-emerald-700">
            주문 완료
          </div>

          <div className="grid gap-3 text-sm text-emerald-900 md:grid-cols-2">
            <InfoRow label="주문번호" value={orderResult.orderUid} />
            <InfoRow label="주문상태" value={orderResult.orderStatusDisplay} />
            <InfoRow
              label="총 결제 금액"
              value={formatWon(orderResult.totalAmount)}
              strong
            />
            <InfoRow label="수령인" value={orderResult.recipientName} />
            <InfoRow label="연락처" value={orderResult.recipientPhone} />
            <InfoRow
              label="배송지"
              value={`${orderResult.address1} ${orderResult.address2 ?? ""}`}
            />
          </div>
        </div>
      )}
    </section>
  );
}

function InfoRow({
  label,
  value,
  strong = false,
}: {
  label: string;
  value: string;
  strong?: boolean;
}) {
  return (
    <div className="flex items-center justify-between gap-4 rounded-2xl bg-white px-4 py-3">
      <span className="text-black/50">{label}</span>
      <span className={strong ? "font-bold text-black" : "text-black"}>
        {value}
      </span>
    </div>
  );
}

function formatWon(value: number) {
  return `${new Intl.NumberFormat("ko-KR").format(value ?? 0)}원`;
}