package com.reyesc.whatdo;

import java.util.ArrayList;

/**
 * Created by Leigh on 4/30/2018.
 */

public class Group {
    private String groupName;
    private ArrayList<String> groupMembers;
    private ArrayList<String> groupInterests;

    public Group(String groupName) {
        this.groupName = groupName;
        this.groupMembers = new ArrayList<>();
        this.groupInterests = new ArrayList<>();
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String newName) {
        this.groupName = newName;
    }

    public ArrayList<String> getMembers() {
        return this.groupMembers;
    }

    public void addMember(String name) {
        for (String member : this.groupMembers) {
            if (member == name) {
                return;
            }
        }
        this.groupMembers.add(name);
    }

    public void removeMember(String name) {
        for (String member : this.groupMembers) {
            if (member == name) {
                this.groupMembers.remove(member);
                return;
            }
        }
    }

    public ArrayList<String> getGroupInterests() {
        return this.groupInterests;
    }

    public void addGroupInterest(String i) {
        for (String interest : this.groupInterests) {
            if (interest == i) {
                return;
            }
        }
        this.groupInterests.add(i);
    }

    public void removeGroupInterest(String i) {
        for (String interest : this.groupInterests) {
            if (interest == i) {
                this.groupInterests.remove(interest);
                return;
            }
        }
    }
}
