package com.reyesc.whatdo;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Leigh on 5/2/2018.
 */

public class FriendViewHolder extends ViewHolder {
    TextView friendName;
    Button removeFriend;
    Button acceptReq;
    RelativeLayout parentLayout;

    public FriendViewHolder(View view) {
        super(view);
        friendName = view.findViewById(R.id.friendName);
        removeFriend = view.findViewById(R.id.removeFriend);
        acceptReq = view.findViewById(R.id.acceptReq);
        parentLayout = view.findViewById(R.id.friendLayout);
    }
}
