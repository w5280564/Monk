package com.qingbo.monk.bean;


import java.util.List;

public class ServiceChatBean {


    private String Keyword;
    private String Content;
    private List<LinkBean> Link;


    public String getKeyword() {
        return Keyword;
    }

    public void setKeyword(String Keyword) {
        this.Keyword = Keyword;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public List<LinkBean> getLink() {
        return Link;
    }

    public void setLink(List<LinkBean> Link) {
        this.Link = Link;
    }


    public static class LinkBean {
        /**
         * title : 活动地点
         * title_url : http://59.37.4.70:881/index/Booth/plan.html
         */

        private String title;
        private String title_url;
        private int type;//1/2/3/4:参展商/采购商/媒体/普通用户


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle_url() {
            return title_url;
        }

        public void setTitle_url(String title_url) {
            this.title_url = title_url;
        }

        public int getType() {
            return type;
        }
    }
}
