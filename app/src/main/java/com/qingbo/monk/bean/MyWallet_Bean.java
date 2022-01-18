package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyWallet_Bean {

        @SerializedName("total")
        private String total;
        @SerializedName("can")
        private String can;
        @SerializedName("wait")
        private String wait;
}
