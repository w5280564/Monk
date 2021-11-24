package com.xunda.lib.common.bean;


import java.io.Serializable;

/**
 * 通讯录列表
 */
public class FriendBean implements Serializable{

    /**
     * user_id : 79
     * friend_id : 74
     * friend_name : 秀秀
     * add_time : 2018-07-31 13:56:05
     * username : zhang
     */

    private int user_id;
    private int friend_id;
    private String friend_name;
    private String add_time;
    private String username;
    private String letter;
    private String head_img;
    private boolean isChecked;

    private int group_id;//
    private int to_invite;//邀请人参加视频会议添加   1:未邀请  2：已邀请


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getTo_invite() {
        return to_invite;
    }

    public void setTo_invite(int to_invite) {
        this.to_invite = to_invite;
    }
}
