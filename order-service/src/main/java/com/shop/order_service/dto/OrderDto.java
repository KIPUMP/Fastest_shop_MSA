package com.shop.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long productId;

    @Min(value = 1, message = "상품 수량은 1개 이상이어야 합니다.")
    private int orderCount;
}
