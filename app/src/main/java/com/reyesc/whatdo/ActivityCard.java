package com.reyesc.whatdo;

public class ActivityCard {
    private int id;
    private int image;
    private String date;
    private String title;
    private String tags;
    private String description;
    private boolean sponsored;
    private boolean saved;
    public boolean showingFront;

    public ActivityCard(int id, int image, String date, String title, String tags, String description, boolean sponsored) {
        this.id = id;
        this.image = image;
        this.date = date;
        this.title = title;
        this.tags = tags;
        this.description = description;
        this.sponsored = sponsored;
        this.saved = false;
        showingFront = true;
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

    public boolean isSponsored() { return sponsored;}

    public boolean isSaved() { return saved;}

    public void setSaved(boolean saved){
        this.saved = saved;
    }
}
