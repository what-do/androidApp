package com.reyesc.whatdo;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class CardTouchHelper extends ItemTouchHelper.SimpleCallback {
    private CardTouchHelperListener listener;

    public CardTouchHelper(int dragDirs, int swipeDirs, CardTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((CardViewHolder) viewHolder).getCardView();

        View itemView = viewHolder.itemView;
        float width = itemView.getWidth();

        if(Math.abs(dX) < width) {
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        } else {
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, Math.signum(dX)*width, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((CardViewHolder) viewHolder).getCardView();
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    public interface CardTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return .35f;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return .1f;
    }
}
