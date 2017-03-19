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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.landenlabs.all_webviewtester.ui.ComboBox;
import com.landenlabs.all_webviewtester.ui.Ui;
import com.landenlabs.all_webviewtester.util.WLog;

/**
 * A fragment which manage WebView's WebSettings object state.
 * Created by Dennis Lang on 9/1/2015.
 */
public class WebSettingsFrag extends WebFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        WebShare.ChangeListener, AdapterView.OnItemSelectedListener {

    private final WLog wLog = WLog.DBG;
    private View m_rootView;

    private CheckBox m_ws_allow_java_ck;
    private CheckBox m_ws_dom_storage_ck;
    private CheckBox m_ws_java_open_window_ck;
    private CheckBox m_ws_save_form_ck;
    private CheckBox m_ws_user_client_ck;
    private CheckBox m_ws_chrom_agent_ck;
    private CheckBox m_ws_content_access_ck;
    private CheckBox m_ws_file_access_ck;
    private CheckBox m_ws_file_access_url_ck;
    private CheckBox m_ws_universal_access_ck;
    private CheckBox m_ws_cache_ck;
    private CheckBox m_ws_block_image_ck;
    private CheckBox m_ws_load_images_ck;
    private CheckBox m_ws_block_loads_ck;
    private CheckBox m_ws_builtin_zoom_ck;
    private CheckBox m_ws_display_zoom_ck;
    private CheckBox m_ws_support_zoom_ck;
    private CheckBox m_ws_geolocation_ck;
    private CheckBox m_ws_overlay_ck;
    private CheckBox m_ws_wide_ck;
    // private CheckBox m_ws_overview_ck;

    private EditText m_ws_initialScale_et;
    private EditText m_ws_user_agent_et;
    private ComboBox m_ws_cache_mode_cb;
    private ComboBox m_ws_cache_max_cb;

    private CheckBox m_wss_show_console_ck;
    private CheckBox m_wss_show_progress_ck;

    private Button m_ws_apply_btn;


    private WebShare m_webShare;

    // ---------------------------------------------------------------------------------------------
    public WebSettingsFrag() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_rootView = inflater.inflate(R.layout.web_settings, container, false);

        m_webShare.registerViewChangeListener(this);
        setupWebSettings(m_rootView);
        setupWebSettingsListeners(this);
        return m_rootView;
    }

    @Override
    public int getFragId() {
        return R.id.web_settings_id;
    }

    @Override
    public String getName() {
        return "WebSettings";
    }

    @Override
    public String getDescription() {
        return "WebSettings Description";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        m_webShare.unregisterViewChangeListener(this);
    }

    // =============================================================================================
    // WebShare

    @Override
    public void onChange(WebShare.WebInfo webInfo) {
        updateWebSettingsUI();
    }

    // =============================================================================================
    // Adapter

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateWebSettingsUI();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        updateWebSettingsUI();
    }

    // =============================================================================================
    // Implementents View.OnClickListener

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ws_apply_btn:
                applyChange();
                break;
        }
    }

    // =============================================================================================
    // Implementents CompoundButton.onCheckedChanged

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if ( m_webShare.getWebInfo().m_webView == null)
            return;

        WebSettings webSettings = m_webShare.getWebInfo().m_webView.getSettings();

        int id = buttonView.getId();
        switch (id) {
            case R.id.ws_allow_java:
                webSettings.setJavaScriptEnabled(isChecked);
                break;
            case R.id.ws_dom_storage:
                webSettings.setDomStorageEnabled(isChecked);
                break;
            case R.id.ws_java_open_window:
                webSettings.setSupportMultipleWindows(isChecked);
                break;
            case R.id.ws_save_form:
                webSettings.setSaveFormData(isChecked);
                break;
            case R.id.ws_user_client:
                break;
            case R.id.ws_chrome_client:
                break;
            case  R.id.ws_content_access:
                webSettings.setAllowContentAccess(isChecked);
                break;
            case R.id.ws_file_access:
                webSettings.setAllowFileAccess(isChecked);
                break;

            case R.id.ws_cache:
                break;
            case R.id.ws_block_image:
                webSettings.setBlockNetworkImage(isChecked);
                break;
            case R.id.ws_load_images:
                webSettings.setLoadsImagesAutomatically(isChecked);
                break;
            case R.id.ws_block_loads:
                webSettings.setBlockNetworkLoads(isChecked);
                break;
            case R.id.ws_builtin_zoom:
                webSettings.setBuiltInZoomControls(isChecked);
                break;
            case R.id.ws_display_builtin_zoom:
                webSettings.setDisplayZoomControls(isChecked);
                break;
            case R.id.ws_support_zoom:
                webSettings.setSupportZoom(isChecked);
                break;
            case R.id.ws_geolocation:
                webSettings.setGeolocationEnabled(isChecked);
                break;
            case R.id.ws_overlay:
                webSettings.setLoadWithOverviewMode(isChecked);
                break;
            case R.id.ws_wide:
                webSettings.setUseWideViewPort(isChecked);
                break;
            // case R.id.ws_overview:
            //     webSettings.setLoadWithOverviewMode(isChecked);
            //    break;
            case R.id.wss_show_console:
                break;
            case R.id.wss_show_progress:
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            switch (id) {
                case R.id.ws_file_access_url:
                    webSettings.setAllowFileAccessFromFileURLs(isChecked);
                    break;
                case R.id.ws_universal_access:
                    webSettings.setAllowUniversalAccessFromFileURLs(isChecked);
                    break;
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void setupWebSettings(View rootView) {
        m_ws_allow_java_ck = Ui.needViewById(rootView, R.id.ws_allow_java);
        m_ws_dom_storage_ck = Ui.needViewById(rootView, R.id.ws_dom_storage);
        m_ws_java_open_window_ck = Ui.needViewById(rootView, R.id.ws_java_open_window);
        m_ws_save_form_ck = Ui.needViewById(rootView, R.id.ws_save_form);
        m_ws_user_client_ck = Ui.needViewById(rootView, R.id.ws_user_client);
        m_ws_chrom_agent_ck = Ui.needViewById(rootView, R.id.ws_chrome_client);
        m_ws_content_access_ck = Ui.needViewById(rootView, R.id.ws_content_access);
        m_ws_file_access_ck = Ui.needViewById(rootView, R.id.ws_file_access);
        m_ws_file_access_url_ck = Ui.needViewById(rootView, R.id.ws_file_access_url);
        m_ws_universal_access_ck = Ui.needViewById(rootView, R.id.ws_universal_access);
        m_ws_cache_ck = Ui.needViewById(rootView, R.id.ws_cache);
        m_ws_block_image_ck = Ui.needViewById(rootView, R.id.ws_block_image);
        m_ws_load_images_ck = Ui.needViewById(rootView, R.id.ws_load_images);
        m_ws_block_loads_ck = Ui.needViewById(rootView, R.id.ws_block_loads);
        m_ws_builtin_zoom_ck = Ui.needViewById(rootView, R.id.ws_builtin_zoom);
        m_ws_display_zoom_ck = Ui.needViewById(rootView, R.id.ws_display_builtin_zoom);
        m_ws_support_zoom_ck = Ui.needViewById(rootView, R.id.ws_support_zoom);
        m_ws_geolocation_ck = Ui.needViewById(rootView, R.id.ws_geolocation);
        m_ws_overlay_ck = Ui.needViewById(rootView, R.id.ws_overlay);
        m_ws_wide_ck = Ui.needViewById(rootView, R.id.ws_wide);
        // m_ws_overview_ck = Ui.needViewById(rootView, R.id.ws_overview);
        m_wss_show_console_ck = Ui.needViewById(rootView, R.id.wss_show_console);
        m_wss_show_progress_ck = Ui.needViewById(rootView, R.id.wss_show_progress);

        m_ws_initialScale_et = Ui.needViewById(rootView, R.id.ws_initial_scale);
        m_ws_user_agent_et = Ui.needViewById(rootView, R.id.ws_user_agent);
        m_ws_cache_mode_cb = Ui.needViewById(rootView, R.id.ws_cache_mode_cb);
        m_ws_cache_max_cb = Ui.needViewById(rootView, R.id.ws_cache_max_cb);

        m_ws_apply_btn = Ui.needViewById(rootView, R.id.ws_apply_btn);
    }

    private void setupWebSettingsListeners(WebSettingsFrag obj) {
        m_ws_allow_java_ck.setOnCheckedChangeListener(obj);
        m_ws_dom_storage_ck.setOnCheckedChangeListener(obj);
        m_ws_java_open_window_ck.setOnCheckedChangeListener(obj);
        m_ws_save_form_ck.setOnCheckedChangeListener(obj);
        m_ws_user_client_ck.setOnCheckedChangeListener(obj);
        m_ws_chrom_agent_ck.setOnCheckedChangeListener(obj);
        m_ws_content_access_ck.setOnCheckedChangeListener(obj);
        m_ws_file_access_ck.setOnCheckedChangeListener(obj);
        m_ws_file_access_url_ck.setOnCheckedChangeListener(obj);
        m_ws_universal_access_ck.setOnCheckedChangeListener(obj);
        m_ws_cache_ck.setOnCheckedChangeListener(obj);
        m_ws_block_image_ck.setOnCheckedChangeListener(obj);
        m_ws_load_images_ck.setOnCheckedChangeListener(obj);
        m_ws_block_loads_ck.setOnCheckedChangeListener(obj);
        m_ws_builtin_zoom_ck.setOnCheckedChangeListener(obj);
        m_ws_display_zoom_ck.setOnCheckedChangeListener(obj);
        m_ws_support_zoom_ck.setOnCheckedChangeListener(obj);
        m_ws_geolocation_ck.setOnCheckedChangeListener(obj);
        m_ws_overlay_ck.setOnCheckedChangeListener(obj);
        m_ws_wide_ck.setOnCheckedChangeListener(obj);
        // m_ws_overview_ck.setOnCheckedChangeListener(obj);
        m_wss_show_console_ck.setOnCheckedChangeListener(obj);
        m_wss_show_progress_ck.setOnCheckedChangeListener(obj);

        // m_ws_user_agent_et.addTextChangedListener(this);
        m_ws_cache_mode_cb.setOnItemSelectedListener(obj);
        m_ws_cache_max_cb.setOnItemSelectedListener(obj);

        m_ws_apply_btn.setOnClickListener(this);
    }

    private void updateWebSettingsUI() {
        setupWebSettingsListeners(null);

        wLog.d("updateWebSettingsU");
        WebSettings webSettings = m_webShare.getWebInfo().m_webView.getSettings();
        m_ws_allow_java_ck.setChecked(webSettings.getJavaScriptEnabled());
        m_ws_dom_storage_ck.setChecked(webSettings.getDomStorageEnabled());

        m_ws_java_open_window_ck.setChecked(webSettings.getJavaScriptCanOpenWindowsAutomatically());
        m_ws_user_client_ck.setChecked(m_webShare.getWebInfo().m_addUserClient);
        m_ws_initialScale_et.setText(""+Math.round(m_webShare.getWebInfo().m_webView.getScale()));
        m_ws_user_agent_et.setText(webSettings.getUserAgentString());
        m_ws_chrom_agent_ck.setChecked(m_webShare.getWebInfo().m_addChromeClient);
        m_ws_content_access_ck.setChecked(webSettings.getAllowContentAccess());
        m_ws_file_access_ck.setChecked(webSettings.getAllowFileAccess());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            m_ws_file_access_url_ck.setChecked(webSettings.getAllowFileAccessFromFileURLs());
            m_ws_universal_access_ck.setChecked(webSettings.getAllowUniversalAccessFromFileURLs());
        }
    //    m_ws_cache_ck.setChecked(webSettings.getAppCacheEnabled());
        m_ws_block_image_ck.setChecked(webSettings.getBlockNetworkImage());
        m_ws_load_images_ck.setChecked(webSettings.getLoadsImagesAutomatically());
        m_ws_block_loads_ck.setChecked(webSettings.getBlockNetworkLoads());
        m_ws_builtin_zoom_ck.setChecked(webSettings.getBuiltInZoomControls());
        m_ws_support_zoom_ck.setChecked(webSettings.supportZoom());
     //   m_ws_geolocation_ck.setChecked(webSettings.getGeolocationEnabled());
        m_ws_overlay_ck.setChecked(webSettings.getLoadWithOverviewMode());
        m_ws_wide_ck.setChecked(webSettings.getUseWideViewPort());

        m_wss_show_console_ck.setChecked(m_webShare.getWebInfo().m_showConsole);
        m_wss_show_progress_ck.setChecked(m_webShare.getWebInfo().m_showProgress);

        // Ui.needViewById(rootView, R.id.ws_cache_mode_cb)
        // Ui.needViewById(rootView, R.id.ws_cache_max_cb)

        setupWebSettingsListeners(this);
    }

    private void applyChange() {
        WebView webView = m_webShare.getWebInfo().m_webView;

        if (false) {
            try {
                int scale = Integer.valueOf(m_ws_initialScale_et.getText().toString());
                if (scale > 1) {
                    webView.setInitialScale(scale);
                }
            } catch (NumberFormatException ex) {
                m_ws_initialScale_et.setText("100");
                webView.setInitialScale(100);
            }
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(m_ws_user_agent_et.getText().toString());
        m_webShare.getWebInfo().m_addUserClient = m_ws_user_client_ck.isChecked();
        m_webShare.getWebInfo().m_addChromeClient = m_ws_chrom_agent_ck.isChecked();

        m_webShare.getWebInfo().m_showConsole = m_wss_show_console_ck.isChecked();
        m_webShare.getWebInfo().m_showProgress = m_wss_show_progress_ck.isChecked();
        // m_webShare.getWebInfo().m_showPermission = m_wss_show_permission_ck.isChecked();

        try {
            int cacheMode = Integer.parseInt(m_ws_cache_mode_cb.getText());
            m_webShare.getWebInfo().m_webView.getSettings().setCacheMode(cacheMode);
        } catch (Exception ex) {}

        try {
            int cacheSize = Integer.parseInt(m_ws_cache_max_cb.getText());
            m_webShare.getWebInfo().m_webView.getSettings().setAppCacheMaxSize(cacheSize);
        } catch (Exception ex) {}

        m_webShare.notifyChange();
    }
}