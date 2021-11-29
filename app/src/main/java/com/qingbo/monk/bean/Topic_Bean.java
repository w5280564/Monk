package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Topic_Bean {
    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    @SerializedName("list")
        private List<ListDTO> list;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @SerializedName("id")
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @SerializedName("name")
            private String name;

            public List<ChildrenDTO> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenDTO> children) {
                this.children = children;
            }

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
                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
}
