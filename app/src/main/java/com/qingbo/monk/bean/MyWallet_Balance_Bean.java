package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyWallet_Balance_Bean {

    @SerializedName("amount")
    private String amount;
}
