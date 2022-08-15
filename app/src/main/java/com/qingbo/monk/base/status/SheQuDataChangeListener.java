package com.qingbo.monk.base.status;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 数据抽象
 */
public interface SheQuDataChangeListener {
    /**
     * 删除
     */
    public static final int TYPE_DELETE = 1;
    /**
     * 点赞
     */
    public static final int TYPE_PRAISE = 2;
    /**
     * 留言
     */
    public static final int TYPE_COMMENT = 3;
    /**
     * 关注用户
     */
    public static final int TYPE_FOLLOW = 4;
    /**
     * 收藏
     */
    public static final int TYPE_COLLECT = 5;


    @IntDef({TYPE_DELETE, TYPE_PRAISE, TYPE_COMMENT, TYPE_FOLLOW})
    @Retention(RetentionPolicy.SOURCE)
    @interface ChangeType {

    }

    /**
     * @param id    唯一ID
     * @param type  变更类型 删除 点赞、留言、关注、收藏
     * @param flag  操作flag 点赞、关注 true：我点赞了 false：取消点赞或关注
     * @param value 最新点赞、留言数量、最新关注状态（1：已关注 0：未关注）
     */
    void onDataChange(String id, @ChangeType int type, boolean flag, long value);


}
