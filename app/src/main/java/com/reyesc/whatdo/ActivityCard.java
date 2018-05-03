package com.reyesc.whatdo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class ActivityCard {
    private int idx;
    private String id;
    private String imageURI;
    private Bitmap image;
    private String date;
    private String name;
    private String tags;
    private String description;
    private String address;
    private String yelp;
    private boolean sponsored;
    private boolean saved;
    public boolean showingFront;

    public ActivityCard(int idx, String id, String imageURI, String date, String name, String tags, String description, String address, String yelp, boolean sponsored) {
        this.idx = idx;
        this.id = id;
        this.imageURI = imageURI;
        this.date = date;
        this.name = name;
        this.tags = tags;
        this.description = description;
        this.address = address;
        this.yelp = yelp;
        this.sponsored = sponsored;
        this.saved = false;
        showingFront = true;
        try {
            new ActivityCard.LoadImage().execute(this.imageURI);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            String imgUrl = urls[0];
            Bitmap img = null;
            try {
                InputStream in = new URL(imgUrl).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap result) {
            image = result;
        }
    }

    public int getIdx() { return idx; }

    public String getId(){
        return id;
    }

    public String getImageURI(){
        return imageURI;
    }

    public Bitmap getImage(){
        return image;
    }

    public String getDate(){
        return date;
    }

    public String getName(){
        return name;
    }

    public String getTags(){
        return tags;
    }

    public String getDescription(){
        return description;
    }

    public String getAddress() { return address; }

    public String getYelp() { return yelp; }

    public boolean isSponsored() { return sponsored;}

    public boolean isSaved() { return saved;}

    public void setSaved(boolean saved){
        this.saved = saved;
    }
}
