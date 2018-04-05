package com.reyesc.whatdo;

public class ActivityCard {
    private int id;

    private int image;
    private String date;
    private String title;
    private String tags;
    private String description;

    public ActivityCard(int id, int image, String date, String title, String tags, String description) {
        this.id = id;
        this.image = image;
        this.date = date;
        this.title = title;
        this.tags = tags;
        this.description = description;
    }

    public int getId(){
        return id;
    }

    public int getImage(){
        return image;
    }

    public String getDate(){
        return date;
    }

    public String getTitle(){
        return title;
    }

    public String getTags(){
        return tags;
    }

    public String getDescription(){
        return description;
    }
}
