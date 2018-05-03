package com.reyesc.whatdo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PopupActivity {
    private CardView cardView;
    private ActivityCard activityCard;
    private FragmentExtension.FragmentToActivityListener fragmentToActivityListener;
    private PopupWindow popupWindow;
    private RecyclerView recyclerViewImages;
    private ArrayList<String> imagesList;
    private RecyclerView recyclerViewFriends;
    private ArrayList<String> friendsList;

    public PopupActivity(CardView cardView, ActivityCard activityCard, FragmentExtension.FragmentToActivityListener fragmentToActivityListener)  {
        this.cardView = cardView;
        this.activityCard = activityCard;
        this.fragmentToActivityListener = fragmentToActivityListener;

        LayoutInflater inflater = (LayoutInflater) cardView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.popup_activity, null);

        TextView textViewTitle = popup.findViewById(R.id.textViewTitle);
        textViewTitle.setText(activityCard.getTitle());

        if (activityCard.isSaved()){
            popup.findViewById(R.id.scrollView).setBackgroundColor(Color.parseColor("#ffffff"));
        } else if (activityCard.getTitle().contains("Sponsored")) {
            popup.findViewById(R.id.scrollView).setBackgroundColor(Color.parseColor("#FFFACD"));
        }

        View close = popup.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.recyclerViewImages = popup.findViewById(R.id.recyclerViewImages);
        this.recyclerViewFriends = popup.findViewById(R.id.recyclerViewFriends);
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewFriends.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFriends.setHasFixedSize(true);
        recyclerViewFriends.setItemAnimator(new DefaultItemAnimator());

        imagesList = new ArrayList<>();
        friendsList = new ArrayList<>();

        ActivityImagesCardAdapter imagesCardAdapter = new ActivityImagesCardAdapter(recyclerViewImages, imagesList);
        ActivityFriendsCardAdapter friendsCardAdapter = new ActivityFriendsCardAdapter(recyclerViewFriends, friendsList);

        recyclerViewImages.setAdapter(imagesCardAdapter);
        recyclerViewFriends.setAdapter(friendsCardAdapter);

        popupWindow = new PopupWindow(popup, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setOutsideTouchable(false);
    }

    public void show() {
        fragmentToActivityListener.setPopupActivity(this);
        popupWindow.showAtLocation(cardView, Gravity.NO_GRAVITY, 0, 0);
        activityCard.showingFront = false;
    }

    public void dismiss() {
        fragmentToActivityListener.setPopupActivity(null);
        popupWindow.dismiss();
        activityCard.showingFront = true;
    }
}
