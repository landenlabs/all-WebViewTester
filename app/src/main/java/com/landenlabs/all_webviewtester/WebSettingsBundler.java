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

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Implement in/out bundling of  webview's websettings object.
 * Created by Dennis Lang on 9/1/2015.
 */
public class WebSettingsBundler {
    
    public static Bundle getBundleFor(WebSettings webSettings) {
        Bundle b = new Bundle();

        b.putBoolean("m_ws_allow_java_ck", webSettings.getJavaScriptEnabled());
        b.putBoolean("m_ws_dom_storage_ck", webSettings.getDomStorageEnabled());
        b.putBoolean("m_ws_java_open_window_ck", webSettings.getJavaScriptCanOpenWindowsAutomatically());
        b.putBoolean("m_ws_save_form_ck", webSettings.getSaveFormData());
        b.putString("m_ws_user_agent_et", webSettings.getUserAgentString());
        b.putBoolean("m_ws_content_access_ck", webSettings.getAllowContentAccess());
        b.putBoolean("m_ws_file_access_ck", webSettings.getAllowFileAccess());
                
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            b.putBoolean("m_ws_file_access_url_ck", webSettings.getAllowFileAccessFromFileURLs());
            b.putBoolean("m_ws_universal_access_ck", webSettings.getAllowUniversalAccessFromFileURLs());
        }
        //    m_ws_cache_ck", webSettings.getAppCacheEnabled());
        b.putBoolean("m_ws_block_image_ck", webSettings.getBlockNetworkImage());
        b.putBoolean("m_ws_load_images_ck", webSettings.getLoadsImagesAutomatically());
        b.putBoolean("m_ws_block_loads_ck", webSettings.getBlockNetworkLoads());
        b.putBoolean("m_ws_builtin_zoom_ck", webSettings.getBuiltInZoomControls());
        b.putBoolean("m_ws_display_zoom_ck", webSettings.getDisplayZoomControls());
        b.putBoolean("m_ws_support_zoom_ck", webSettings.supportZoom());
        //   m_ws_geolocation_ck", webSettings.getGeolocationEnabled());
        b.putBoolean("m_ws_overlay_ck", webSettings.getLoadWithOverviewMode());
        b.putBoolean("m_ws_wide_ck", webSettings.getUseWideViewPort());
        b.putBoolean("m_ws_overview_ck", webSettings.getLoadWithOverviewMode());
        
        // Ui.needViewById(rootView, R.id.ws_cache_mode_cb)
        // Ui.needViewById(rootView, R.id.ws_cache_max_cb)
        
        return  b;
    }

    public static Bundle getBundleFor(WebView webView) {
        Bundle b = new Bundle();
        b.putBundle("webSettings", getBundleFor(webView.getSettings()));
        b.putString("webUrl", webView.getOriginalUrl());
        Bundle state = new Bundle();
        webView.saveState(state);
        b.putBundle("webState", state);
        return b;
    }

    public static void  restoreSettings(WebSettings webSettings, Bundle b) {
        webSettings.setJavaScriptEnabled(b.getBoolean("m_ws_allow_java_ck"));
        webSettings.setDomStorageEnabled(b.getBoolean("m_ws_dom_storage_ck"));
        webSettings.setJavaScriptCanOpenWindowsAutomatically(b.getBoolean("m_ws_java_open_window_ck"));
        webSettings.setSaveFormData(b.getBoolean("m_ws_save_form_ck"));
        webSettings.setUserAgentString(b.getString("m_ws_user_agent_et"));
        webSettings.setAllowContentAccess(b.getBoolean("m_ws_content_access_ck"));
        webSettings.setAllowFileAccess(b.getBoolean("m_ws_file_access_ck"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(b.getBoolean("m_ws_file_access_url_ck"));
            webSettings.setAllowUniversalAccessFromFileURLs(b.getBoolean("m_ws_universal_access_ck"));
        }
        //    m_ws_cache_ck", webwebSettings.settings.getAppCacheEnabled()));
        webSettings.setBlockNetworkImage(b.getBoolean("m_ws_block_image_ck"));
        webSettings.setLoadsImagesAutomatically(b.getBoolean("m_ws_load_images_ck"));
        webSettings.setBlockNetworkLoads(b.getBoolean("m_ws_block_loads_ck"));
        webSettings.setBuiltInZoomControls(b.getBoolean("m_ws_builtin_zoom_ck"));
        webSettings.setDisplayZoomControls(b.getBoolean("m_ws_display_zoom_ck"));
        webSettings.setSupportZoom(b.getBoolean("m_ws_support_zoom_ck"));
        //   m_ws_geolocation_ck", webwebSettings.settings.getGeolocationEnabled()));
        webSettings.setLoadWithOverviewMode(b.getBoolean("m_ws_overlay_ck"));
        webSettings.setUseWideViewPort(b.getBoolean("m_ws_wide_ck"));
        webSettings.setLoadWithOverviewMode(b.getBoolean("m_ws_overview_ck"));

        // Ui.needViewById(rootView, R.id.ws_cache_max_cb)
    }

    public static void restoreWebView(WebView webView, Bundle b) {
        restoreSettings(webView.getSettings(), b.getBundle("webSettings"));
        webView.restoreState(b.getBundle("webState"));
        webView.loadUrl(b.getString("webUrl"));
    }
}
