package com.reyesc.whatdo;

import java.io.Serializable;

/**
 * Created by Leigh on 5/2/2018.
 */

public class Interest implements Serializable {
    private String tag;
    private Boolean interested;

    public Interest(String interest) {
        this.tag = interest;
        interested = false; //default
    }

    public void setTag(String interest) {
        this.tag = interest;
    }

    public String getTag() {
        return this.tag;
    }

    public Boolean isInterested() {
        return this.interested;
    }

    public void select() {
        this.interested = true;
    }

    public void deselect() {
        this.interested = false;
    }
}
