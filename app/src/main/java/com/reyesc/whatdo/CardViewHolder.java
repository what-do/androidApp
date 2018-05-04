package com.reyesc.whatdo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View frontView, backView;
    private ImageView imageView;
    private TextView textViewDate, textViewTitle, textViewTags, textViewDescription;
    private CardView cardView;
    private ActivityCard activityCard;
    private PopupWindow popupWindow;
    private FragmentExtension.FragmentToActivityListener fragmentToActivityListener;

    public CardViewHolder(View itemView, FragmentExtension.FragmentToActivityListener fragmentToActivityListener) {
        super(itemView);

        this.fragmentToActivityListener = fragmentToActivityListener;
        cardView = (CardView) itemView;
        frontView = itemView.findViewById(R.id.cardViewFront);
        backView = itemView.findViewById(R.id.cardViewBack);
        imageView = itemView.findViewById(R.id.imageViewFront);
        textViewDate = itemView.findViewById(R.id.textViewDateFront);
        textViewDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        textViewTitle = itemView.findViewById(R.id.textViewTitleFront);
        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        textViewTags = itemView.findViewById(R.id.textViewTagsFront);
        textViewTags.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        textViewDescription = itemView.findViewById(R.id.textViewDescriptionFront);
        textViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);

        itemView.setOnClickListener(this);
    }

    public void bindData(ActivityCard activityCard) {
        this.activityCard = activityCard;
        if(activityCard.showingFront){
            frontView.setAlpha(1.0f);
            backView.setAlpha(0.0f);
        }else {
            backView.setAlpha(1.0f);
            frontView.setAlpha(0.0f);
        }

        textViewDate.setText(activityCard.getDate());
        textViewTitle.setText(activityCard.getName());
        textViewTags.setText(activityCard.getTags());
        //textViewDescription.setText(activityCard.getDescription());
        textViewDescription.setText(activityCard.getAddress());
        cropBitmap();
    }

    @Override
    public void onClick(View v) {
        if(activityCard.showingFront){
            activityCard.showingFront = false;
            PopupActivity popupActivity = new PopupActivity(cardView, activityCard, fragmentToActivityListener);
            popupActivity.show();
            //flipView(itemView.getContext(), frontView, backView,false);
        }else {
            activityCard.showingFront = true;
            //flipView(itemView.getContext(), frontView, backView,true);
        }

    }

    public void flipView(Context context, View back, View front, boolean showFront) {

        AnimatorSet showAnim = new AnimatorSet();

        if (showFront) {
            AnimatorSet frontIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_front_in);
            AnimatorSet backOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_back_out);
            frontIn.setTarget(back);
            backOut.setTarget(front);
            showAnim.playTogether(frontIn, backOut);
        } else {
            AnimatorSet frontOut  = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_front_out);
            AnimatorSet backIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_back_in);
            frontOut.setTarget(back);
            backIn.setTarget(front);
            showAnim.playTogether(backIn, frontOut);
        }
        showAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        showAnim.start();
    }

    public CardView getCardView() {
        return cardView;
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

    public void cropBitmap() {
        DisplayMetrics displayMetrics = cardView.getContext().getResources().getDisplayMetrics();
        Float ratioWH = ((displayMetrics.widthPixels / displayMetrics.density) - 20) / 320;
        Float ratioHW = 320 / ((displayMetrics.widthPixels / displayMetrics.density) - 20);
        Bitmap image = activityCard.getImage();
        if (image != null) {
            int height = image.getHeight();
            int width = image.getWidth();
            if (height > width) {
                int startY = height / 2 - width / 2;
                height = Math.round(width / ratioWH);
                startY = startY + height > image.getHeight() ? image.getHeight() - height : startY;
                startY = startY < 0 ? 0 : startY;
                image = Bitmap.createBitmap(image, 0, startY, width, height);
            } else if (width > height){
                int startX = width / 2 - height / 2;
                width = Math.round(height / ratioHW);
                startX = startX + width > image.getWidth() ? image.getWidth() - width : startX;
                startX = startX < 0 ? 0 : startX;
                image = Bitmap.createBitmap(image, startX, 0, width, height);
            }
            imageView.setImageBitmap(image);
        }
    }
}
