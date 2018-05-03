package com.reyesc.whatdo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
        textViewTitle = itemView.findViewById(R.id.textViewTitleFront);
        textViewTags = itemView.findViewById(R.id.textViewTagsFront);
        textViewDescription = itemView.findViewById(R.id.textViewDescriptionFront);

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
//        imageView.setImageDrawable(cardView.getContext().getDrawable(activityCard.getImage()));
//        textViewDate.setText(activityCard.getDate());
        textViewTitle.setText(activityCard.getTitle());
//        textViewTags.setText(activityCard.getTags());
//        textViewDescription.setText(activityCard.getDescription());
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
}
