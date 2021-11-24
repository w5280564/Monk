package com.xunda.lib.common.common.eventbus;


public class CheckVideoButtonEvent {
    public final static int CHECK_VIDEO = 1;

    public int type;
    public boolean isCheckVideo;


    public CheckVideoButtonEvent(int type,boolean isCheckVideo) {
        this.type = type;
        this.isCheckVideo = isCheckVideo;
    }
}
