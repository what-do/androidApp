package com.reyesc.whatdo;

import android.support.annotation.NonNull;

public class Friend implements Comparable<Friend> {
    final int BEFORE = -1;
    final int EQUAL = 0;
    final int AFTER = 1;

    private String id;
    private String userName;
    private Boolean isReq;

    public Friend(String id, String userName) {
        this.id = id;
        this.userName = userName;
        this.isReq = false;
    }
    public String getFriendId() {
        return this.id;
    }
    public String getFriendUserName() {
        return this.userName;
    }
    public Boolean isReq() { return this.isReq; }
    public void setReq(Boolean req) { this.isReq = req; }

    @Override
    public int compareTo(@NonNull Friend friend) {
        if (! this.getFriendUserName().equalsIgnoreCase(friend.getFriendUserName()) ) {
            return this.getFriendUserName().compareTo(friend.getFriendUserName());
        }
        if(! (this.isReq()==friend.isReq()) ) {
            return this.isReq().compareTo(friend.isReq());
        }
        return this.getFriendId().compareTo(friend.getFriendId());
    }
}
