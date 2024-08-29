package com.shop.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishItemDto {

    @NotNull(message = "상품 아이디는 필수 입력값입니다")
    private Long productId;
    
    @Min(value = 1, message = "최소 1개 이상 담아주세요")
    private int wishCount;
}
