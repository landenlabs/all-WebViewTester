package com.landenlabs.all_webviewtester;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;

/**
 * Created by Dennis Lang on 8/15/16.
 */
public abstract class WebFragment  extends Fragment {

    public abstract int getFragId();
    public abstract String getName();
    public abstract String getDescription();

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        /*
        if (! getRetainInstance()) {
            // Required to prevent duplicate id when Fragment re-created.
            int fragId = getFragId();
            Fragment fragment = (getFragmentManager().findFragmentById(fragId));
            if (fragment != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }
        }
        */
    }

    Resources.Theme getTheme() {
        return this.getContext().getTheme();
    }

    Drawable getDrawable(int resId) {
        if (Build.VERSION.SDK_INT >= 21) {
            return getResources().getDrawable(resId, getTheme());
        }
        return getResources().getDrawable(resId);
    }
}
