package org.mbc.czo.function.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.mbc.czo.function.product.constant.ItemSellStatus;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";

}