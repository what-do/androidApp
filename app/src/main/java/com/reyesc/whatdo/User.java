package com.reyesc.whatdo;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leigh on 4/30/2018.
 */

/**
 * Singleton User class
 */
//TODO: how to delete user account from db?
public class User implements Serializable {

    private static User user_instance = null;

    private String id;
    private String email;
    private String username;
    private ArrayList<Interest> interests;
    private ArrayList<String> friends;
    private ArrayList<Group> groups;
    private ArrayList<String> sentInterests;

    private User(String id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.interests = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    public static User getInstance(String id, String email, String username) {
        if (user_instance == null) {
            user_instance = new User(id, email, username);
        }
        return user_instance;
    }

    public void clear() {
        user_instance = null;
    }

    public String getUserName() {
        return this.username;
    }

    public String getUserId() {
        return this.id;
    }

    public String getUserEmail() {
        return this.email;
    }

    public ArrayList<Interest> getUserInterests() {
        return this.interests;
    }

    public ArrayList<String> getUserFriends() { return this.friends; }

    public ArrayList<Group> getUserGroups() {return this.groups; }

    public void addUserInterest(String i) {
        for (Interest interest : this.interests) {
            if (i.equals(interest.getTag())) {
                interest.select();
                return;
            }
        }
        this.interests.add(new Interest(i));
    }

    public void removeUserInterest(String i) {
        for (Interest interest : this.interests) {
            if (interest.getTag().equals(i)) {
                interest.deselect();
            }
        }
    }

    public void addFriend (String userName) {
        for (String friend : this.friends) {
            if (friend == userName) {
                return;
            }
        }
        this.friends.add(userName);
    }

    public void removeFriend (String userName) {
        for (String friend : this.friends) {
            if (friend == userName) {
                this.friends.remove(friend);
                return;
            }
        }
    }

    public void addGroup (String groupName) {
        for (Group group : this.groups) {
            if (group.getGroupName() == groupName) {
                return;
            }
        }
        Group newGroup = new Group(groupName);
        this.groups.add(newGroup);
    }

    public void removeGroup (String groupName) {
        for (Group group : this.groups) {
            if (group.getGroupName() == groupName) {
                this.groups.remove(group.getGroupName());
                return;
            }
        }
    }

    public ArrayList<String> sendInterests(){
        sentInterests = new ArrayList<>();
        for (Interest i : interests){
            if (i.isInterested() == true) {
                sentInterests.add(i.getTag());
            }
        }
        return sentInterests;
    }



}
