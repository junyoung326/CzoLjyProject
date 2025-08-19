package org.mbc.czo.function.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDTO {

    private Long cartItemId;

    private String ItemNm;

    private int price;

    private int count;

    private String imgUrl;

    public CartDetailDTO(Long cartItemId, String ItemNm, int price, int count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.ItemNm = ItemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }
}
