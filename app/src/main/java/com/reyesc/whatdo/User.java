package com.reyesc.whatdo;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Singleton User class
 */
//TODO: how to delete user account from db?
public class User implements Serializable {

    private static User user_instance = null;

    private String id;
    private String email;
    private String username;
    private String displayname;
    private ArrayList<Interest> interests;
    private ArrayList<Friend> friends;
    //private ArrayList<Group> groups;
    private ArrayList<String> sentInterests;
    private String activityFilter = "I'm by myself!";

    private User(String id, String email, String username, String displayname) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.displayname = displayname;
        this.interests = new ArrayList<>();
        this.friends = new ArrayList<>();
    }

    public static User getInstance(String id, String email, String username, String displayname) {
        if (user_instance == null) {
            user_instance = new User(id, email, username, displayname);
        }
        return user_instance;
    }


    public static User getInstance() {
        return user_instance;
    }

    public void clear() {
        user_instance = null;
    }

    public String getUserName() {
        return this.username;
    }

    public void setUserName(String name) {this.username = name;}

    public String getDisplayName() { return this.displayname;}

    public String getUserId() { return this.id; }

    public String getUserEmail() {
        return this.email;
    }

    public ArrayList<Interest> getUserInterests() {
        return this.interests;
    }

    public ArrayList<Friend> getUserFriends() { return this.friends; }

    public ArrayList<String> getUserFriendsStringArray(){

        ArrayList<String> friendUserNames = new ArrayList<>();
        friendUserNames.add("I'm by myself!");
        for (Friend f : friends){
            friendUserNames.add(f.getFriendUserName());

        }
        return friendUserNames;
    }
    public void addUserInterest(String i) {
        for (Interest interest : this.interests) {
            if (i.toLowerCase().equals(interest.getTag().toLowerCase())) {
                interest.select();
                return;
            }
        }
        this.interests.add(new Interest(i));
    }

    public void removeUserInterest(String i) {
        for (Interest interest : this.interests) {
            if (interest.getTag().toLowerCase().equals(i.toLowerCase())) {
                interest.deselect();
                int a = 0;
            }
        }
    }

    public String findFriendId(String friend){
        for (Friend f : this.friends){
            if (f.getFriendUserName().toLowerCase().equals(friend.toLowerCase())){
                return f.getFriendId();
            }

        }
        return null;
    }

    public void addFriend (Friend friend) {
        for (Friend f : this.friends) {
            if (f.getFriendId() == friend.getFriendId()) {
                return;
            }
        }
        this.friends.add(friend);
    }

    public void removeFriend (String id) {
        for (Friend friend : this.friends) {
            if (friend.getFriendId() == id) {
                this.friends.remove(friend);
                return;
            }
        }
    }
    public String getActivityFilter(){
        return activityFilter;
    }

    public void setActivityFilter(String activityFilter) {
        this.activityFilter = activityFilter;
    }



    /*public void addGroup (String groupName) {
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
    }*/

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
