package com.reyesc.whatdo;

import java.util.ArrayList;

/**
 * Created by Leigh on 4/30/2018.
 */

public class Group {
    private String name;
    private ArrayList<String> friends;
    private ArrayList<String> interests;

    public Group(String name, ArrayList<String> friends, ArrayList<String> interests) {
        this.name = name;
        this.friends = friends;
        this.interests = interests;
    }

    public String getGroupName() {
        return this.name;
    }

    public void setGroupName(String newName) {
        this.name = newName;
    }

    public ArrayList<String> getFriends() {
        return this.friends;
    }

    public void addFriend(String name) {
        for (String friend : this.friends) {
            if (friend == name) {
                return;
            }
        }
        this.friends.add(name);
    }

    public void removeFriend(String name) {
        for (String friend : this.friends) {
            if (friend == name) {
                this.friends.remove(friend);
                return;
            }
        }
    }

    public ArrayList<String> getGroupInterests() {
        return this.interests;
    }

    public void addGroupInterest(String i) {
        for (String interest : this.interests) {
            if (interest == i) {
                return;
            }
        }
        this.interests.add(i);
    }

    public void removeGroupInterest(String i) {
        for (String interest : this.interests) {
            if (interest == i) {
                this.interests.remove(interest);
                return;
            }
        }
    }
}
