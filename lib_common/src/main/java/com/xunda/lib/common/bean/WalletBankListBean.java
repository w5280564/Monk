package com.xunda.lib.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WalletBankListBean {

    private Integer bankId;
    private String bankName;
    private String bankShort;
    private String icon;
}
