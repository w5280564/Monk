package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Topic_Bean {
    @SerializedName("list")
        private List<ListDTO> list;
        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("children")
            private List<ChildrenDTO> children;

            @NoArgsConstructor
            @Data
            public static class ChildrenDTO {
                @SerializedName("id")
                private String id;
                @SerializedName("pid")
                private String pid;
                @SerializedName("name")
                private String name;
            }
        }
}
