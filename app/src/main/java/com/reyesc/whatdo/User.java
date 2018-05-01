package com.reyesc.whatdo;

import java.io.Serializable;
import java.util.ArrayList;

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
    private ArrayList<String> interests;

    private User(String id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.interests = new ArrayList<String>();
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

    public String getEmail() {
        return this.email;
    }

    public ArrayList<String> getUserInterests() {
        return this.interests;
    }

    public void addUserInterest(String i) {
        for (String interest : this.interests) {
            if (interest == i) {
                return;
            }
        }
        this.interests.add(i);
    }

    public void removeUserInterest(String i) {
        for (String interest : this.interests) {
            if (interest == i) {
                this.interests.remove(interest);
                return;
            }
        }
    }
}
