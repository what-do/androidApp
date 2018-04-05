package com.reyesc.whatdo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

class CardViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView textViewDate, textViewTitle, textViewTags, textViewDescription;
    private RelativeLayout viewBackground, viewForeground;

    public CardViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView);
        textViewDate = itemView.findViewById(R.id.textViewDate);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewTags = itemView.findViewById(R.id.textViewTags);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
        viewBackground = itemView.findViewById(R.id.view_background);
        viewForeground = itemView.findViewById(R.id.view_foreground);
    }

    public ImageView getImageView(){
        return imageView;
    }

    public TextView getTextViewDate() {
        return textViewDate;
    }

    public TextView getTextViewTitle() {
        return textViewTitle;
    }

    public TextView getTextViewTags() {
        return textViewTags;
    }

    public TextView getTextViewDescription() {
        return textViewDescription;
    }

    public RelativeLayout getViewBackground() {
        return viewBackground;
    }

    public RelativeLayout getViewForeground() {
        return viewForeground;
    }
}
