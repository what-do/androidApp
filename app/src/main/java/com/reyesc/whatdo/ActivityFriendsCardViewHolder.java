package com.reyesc.whatdo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

class ActivityFriendsCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView imageView;
    private CardView cardView;
    private String string;
    private PopupWindow popupWindow;

    public ActivityFriendsCardViewHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView;
        imageView = itemView.findViewById(R.id.image);
        itemView.setOnClickListener(this);
    }

    public void bindData(String string) {
        this.string = string;
    }

    @Override
    public void onClick(View v) {
        //maximize image
    }

    public CardView getCardView() {
        return cardView;
    }

    public ImageView getImageView(){
        return imageView;
    }
}
