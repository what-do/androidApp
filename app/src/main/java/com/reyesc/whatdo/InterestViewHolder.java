package com.reyesc.whatdo;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Leigh on 5/2/2018.
 */

public class InterestViewHolder extends ViewHolder {
    TextView interestTag;
    CheckBox checkBox;
    LinearLayout parentLayout;

    public InterestViewHolder(View view) {
        super(view);
        interestTag = view.findViewById(R.id.interest1);
        checkBox = view.findViewById(R.id.checkBox1);
        parentLayout = view.findViewById(R.id.interestLayout);
    }
}
