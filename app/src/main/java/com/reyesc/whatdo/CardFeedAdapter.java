package com.reyesc.whatdo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CardFeedAdapter extends RecyclerView.Adapter<CardFeedAdapter.CardViewHolder> {
    private List<ActivityCard> cardList;

    public CardFeedAdapter(List<ActivityCard> cardList){
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ActivityCard activityCard = cardList.get(position);

        //holder.imageView.setImageDrawable(context.getDrawable(activityCard.getImage()));
        holder.textViewDate.setText(activityCard.getDate());
        holder.textViewTitle.setText(activityCard.getTitle());
        holder.textViewTags.setText(activityCard.getTags());
        holder.textViewDescription.setText(activityCard.getDescription());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewDate, textViewTitle, textViewTags, textViewDescription;

        public CardViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewTags = itemView.findViewById(R.id.textViewTags);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
