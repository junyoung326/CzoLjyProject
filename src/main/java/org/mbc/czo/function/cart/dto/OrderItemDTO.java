package org.mbc.czo.function.cart.dto;

import lombok.Getter;
import lombok.Setter;
import org.mbc.czo.function.cart.domain.OrderItem;

@Getter
@Setter
public class OrderItemDTO {

    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl;

    // 생성자 안에서 필드 초기화
    public OrderItemDTO(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
