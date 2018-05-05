package com.reyesc.whatdo;

import android.support.annotation.NonNull;

public class Friend implements Comparable<Friend> {

    private String id;
    private String userName;

    public Friend(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }
    public String getFriendId() {
        return this.id;
    }
    public String getFriendUserName() {
        return this.userName;
    }

    @Override
    public int compareTo(@NonNull Friend friend) {
        if (! this.getFriendUserName().equalsIgnoreCase(friend.getFriendUserName()) ) {
            return this.getFriendUserName().compareTo(friend.getFriendUserName());
        }
        return this.getFriendId().compareTo(friend.getFriendId());
    }
}
