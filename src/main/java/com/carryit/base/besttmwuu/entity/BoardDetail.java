package com.carryit.base.besttmwuu.entity;

import java.io.Serializable;

public class BoardDetail implements Serializable {
    private Board board;
    private String follow; //"0"未关注,1关注
    private long topic;
    private long followCount;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public long getTopic() {
        return topic;
    }

    public void setTopic(long topic) {
        this.topic = topic;
    }

    public long getFollowCount() {
        return followCount;
    }

    public void setFollowCount(long followCount) {
        this.followCount = followCount;
    }
}
