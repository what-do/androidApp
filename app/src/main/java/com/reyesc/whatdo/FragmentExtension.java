package com.reyesc.whatdo;

import android.support.v4.app.Fragment;

public class FragmentExtension extends Fragment {
    protected FragmentToActivityListener fragmentToActivityListener;

    public interface FragmentToActivityListener {
        void fromFeedToCollection(ActivityCard card);
        void fromCollectionToFeed(ActivityCard card);
        void toasting (String string);
    }
}
