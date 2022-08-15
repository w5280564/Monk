package com.qingbo.monk.base.status;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据变更
 */
public class ArticleDataChange {
    /**
     * 单例
     */
    private static ArticleDataChange articleDataChange;

    /**
     * 同步数据的回调接口
     */
    private Map<Integer, SheQuDataChangeListener> dataChangeListenerMap = new HashMap<>();


    /**
     * 构造
     */
    private ArticleDataChange() {

    }

    /**
     * 使用
     */
    public static ArticleDataChange ins() {
        if (articleDataChange == null) {
            articleDataChange = new ArticleDataChange();
        }
        return articleDataChange;
    }

    /**
     * @param id    唯一ID
     * @param type  变更类型 删除 点赞、留言、关注、收藏
     * @param flag  操作flag 点赞、关注 true：我点赞了 false：取消点赞或关注
     * @param value 最新点赞、留言数量、最新关注状态（1：已关注 0：未关注）
     */
    public void onDataChange(String id, int type, boolean flag, long value) {
        for (Integer key : dataChangeListenerMap.keySet()) {
            dataChangeListenerMap.get(key).onDataChange(id, type, flag, value);
        }
    }

    /**
     * 列表通过单例获取对象设置回调
     * @param listener
     */
    public void setArticleDataChangeListener(SheQuDataChangeListener listener){
        dataChangeListenerMap.put(listener.hashCode(),listener);
    }

    /**
     * 移除
     * @param listener
     */
    public  void  remove(SheQuDataChangeListener listener){
        dataChangeListenerMap.remove(listener.hashCode());
    }

}
