package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeSeekFund_Bean {
    @SerializedName("name")
    private String name;
    @SerializedName("number")
    private String number;
}
