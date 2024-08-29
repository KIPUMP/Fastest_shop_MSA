package com.shop.order_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WishOrderDto {
    private Long wishItemId;
    private List<WishOrderDto> wishOrderDtoList;
}
