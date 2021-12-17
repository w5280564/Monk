package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MogulTagListBean  {

        @SerializedName("tagList")
        private List<TagListDTO> tagList;

        @NoArgsConstructor
        @Data
        public static class TagListDTO {
            @SerializedName("tag_name")
            private String tagName;
            @SerializedName("id")
            private String id;
        }
}
