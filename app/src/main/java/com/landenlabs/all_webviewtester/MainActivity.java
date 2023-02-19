/*
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  @author Dennis Lang  (3/21/2015)
 *  @see http://landenlabs.com/
 */

package com.landenlabs.all_webviewtester;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.landenlabs.all_webviewtester.util.ShareUtil;

import java.util.HashSet;
import java.util.Locale;

/**
 * Main Activity to WebTester app.
 * Created by Dennis Lang on 9/1/2015.
 */
public class MainActivity extends AppCompatActivity implements  WebShare {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    WebInfo m_webInfo  = new WebInfo();
    HashSet<ChangeListener> m_webViewChangeListeners = new HashSet<ChangeListener>();

    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            m_webInfo.m_webViewState = savedInstanceState.getBundle("webInfo");
        }
        setContentView(R.layout.main_page);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle("WebTester");
            mActionBar.setSubtitle("v" + BuildConfig.VERSION_NAME + " API" + Build.VERSION.SDK_INT +
                    (BuildConfig.DEBUG ? " Debug" : ""));
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME );
            mActionBar.setIcon(R.drawable.webtester);
        }

        // Set up the ViewPager with the sections adapter.
        // View Pager only available in Portraite mode.
        // Landscape changes to split screen mode.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        if (mViewPager != null) {
            // Keeping all pages prevents crash on duplicate fragment problem on some OS
            //  See WebFragment  onDestroyView
            mViewPager.setOffscreenPageLimit(2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewPager != null) {
            if (mSectionsPagerAdapter == null) {
                // Create the adapter that will return a fragment for each section of the activity.
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(mSectionsPagerAdapter);
                // Optionally set limit of pages to keep.
                // mViewPager.setOffscreenPageLimit(mSectionsPagerAdapter.getCount());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        m_webInfo.saveState();
        outState.putBundle("webInfo", m_webInfo.m_webViewState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_share) {
            share();
            return true;    // handled
        }

        return super.onOptionsItemSelected(item);
    }

    private void share() {
        final Activity activity = this;
        final String toEmail = "wsimobile1@gmail.com";
        View rootView = mViewPager.getRootView();
        Snackbar.make(rootView, "Sharing Log Files", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        ShareUtil.shareScreen(activity, rootView, toEmail, "Amazon Ad Test", null);
        // ShareUtil.shareFileLog(activity);
    }

    // =============================================================================================
    // WebShare
    @Override
    public WebInfo getWebInfo() {
        return m_webInfo;
    }

    @Override
    public void setWebInfo(WebInfo webInfo) {
        m_webInfo = webInfo;
        notifyChange();
    }

    @Override
    public void registerViewChangeListener(WebShare.ChangeListener changeListener) {
        m_webViewChangeListeners.add(changeListener);
    }

    @Override
    public void unregisterViewChangeListener(WebShare.ChangeListener changeListener) {
        m_webViewChangeListeners.remove(changeListener);
    }

    @Override
    public void notifyChange() {
        for (ChangeListener changeListener : m_webViewChangeListeners)
            changeListener.onChange(m_webInfo);
    }

    // =============================================================================================
    // SectionsPagerAdapter - implement page swipes
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            // ActionBar actionBar = getActionBar();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PageFragment (defined as a static inner class below).
            return PageFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // View swipe page count.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.webSetTitle).toUpperCase(l);
                case 1:
                    return getString(R.string.webViewTitle).toUpperCase(l);
                case 2:
                    return getString(R.string.webMemTitle).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A page fragment with selectable layouts per page number.
     */
    public static class PageFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_page_number = "page_number";
        int m_pageNum = 0;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PageFragment newInstance(int sectionNumber) {
            PageFragment fragment = new PageFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_page_number, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PageFragment() {
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            if (args != null)
                m_pageNum = args.getInt(ARG_page_number);

            int layoutResId = -1;
            switch (m_pageNum) {
                case 1:
                    layoutResId = R.layout.web_settings_frag;
                    break;
                case 2:
                    layoutResId = R.layout.web_view_frag;
                    break;
                case 3:
                    layoutResId = R.layout.web_memory_frag;
                    break;
            }

            View rootView = inflater.inflate(layoutResId, container, false);
            return rootView;
        }
    }
}
