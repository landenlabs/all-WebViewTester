package com.landenlabs.all_webviewtester;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.landenlabs.all_webviewtester.ui.MemUtil;
import com.landenlabs.all_webviewtester.ui.Ui;
import com.landenlabs.all_webviewtester.util.WLog;

import java.util.List;

/**
 * Created by ldennis on 8/15/16.
 */
public class WebMemoryFrag extends WebFragment  implements View.OnClickListener, WebShare.ChangeListener  {

    private WebShare m_webShare;
    private WebShare.WebInfo m_webInfo;

    private final WLog wLog = WLog.DBG;
    private void Logi(String msg, Object... args) {
        wLog.i(WLog.PID + msg, args);
    }

    // =============================================================================================
    public WebMemoryFrag() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof WebShare) {
            m_webShare = (WebShare) activity;

        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement WebShare");
        }
    }

    ViewGroup mListGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        m_webInfo = m_webShare.getWebInfo();

        View rootView = inflater.inflate(R.layout.web_memory, container, false);
        mListGroup = Ui.viewById(rootView, R.id.web_mem_list);

        if (savedInstanceState != null) {
            // m_webView.restoreState(savedInstanceState);
            // Logi(" webView restoreState URL=" + m_webView.getUrl());
        }

        m_webShare.registerViewChangeListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Logi(" webMem onResume");
        update();
    }

    @Override
    public int getFragId() {
        return R.id.web_memory_id;
    }

    @Override
    public String getName() {
        return "WebMemory";
    }

    @Override
    public String getDescription() {
        return "WebMemory Description";
    }

    @Override
    public void onDestroyView() {
        m_webShare.unregisterViewChangeListener(this);
        // m_webInfo.saveState();
        // m_webInfo.m_webView = null;
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Logi(" webView saveState URL=" + m_webView.getUrl());
        // m_webView.saveState(outState);
    }

    // =============================================================================================
    // WebShare.onChange
    @Override
    public void onChange(WebShare.WebInfo webInfo) {
        update();
    }

    // =============================================================================================
    // Implementents View.OnClickListener
    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    public void update() {
        List<String> list = m_webShare.getWebInfo().m_memList;
        mListGroup.removeAllViews();

        if (list != null) {
            list.add(MemUtil.getMem(this.getActivity()) + " ...");
            for (String item : list) {
                TextView textView = new TextView(mListGroup.getContext());
                textView.setText(item);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0f);
                mListGroup.addView(textView);
            }


        }
    }

}
