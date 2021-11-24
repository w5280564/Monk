package com.xunda.lib.common.bean;


import java.util.List;

/**
 * @文件描述：分页的基类
 */
public class BaseSplitIndexBean<T>{


    private Integer endRow;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private List<T> list;
    private Integer navigateFirstPage;
    private Integer navigateLastPage;
    private Integer navigatePages;
    private List<Integer> navigatepageNums;
    private Integer nextPage;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;
    private Integer prePage;
    private Integer size;
    private Integer startRow;
    private Integer total;


    public Integer getEndRow() {
        return endRow;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public Boolean getFirstPage() {
        return isFirstPage;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public List<T> getList() {
        return list;
    }

    public Integer getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public Integer getNavigateLastPage() {
        return navigateLastPage;
    }

    public Integer getNavigatePages() {
        return navigatePages;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPages() {
        return pages;
    }

    public Integer getPrePage() {
        return prePage;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public Integer getTotal() {
        return total;
    }
}
