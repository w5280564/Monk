package com.xunda.lib.common.common.eventbus;


/**
 * 手动暂停播放
 */
public class ManualPausePalyEvent {
    public final static int MANUAL_PAUSE = 1;

    public int type;
    public boolean isSelected;


    public ManualPausePalyEvent(int type, boolean isSelected) {
        this.type = type;
        this.isSelected = isSelected;
    }
}
