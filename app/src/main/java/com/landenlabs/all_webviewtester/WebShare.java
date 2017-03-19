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

import android.os.Bundle;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

/**
 * Data interchange object used to share Web state between fragments.
 * Created by Dennis Lang on 9/1/2015.
 */
public interface WebShare {

    // =============================================================================================
    // WebInfo object holds WebView singleton and state information.

    public class WebInfo {
        public WebView m_webView;
        public Bundle m_webViewState;

        public boolean m_addUserClient = true;
        public boolean m_addChromeClient = true;
        public boolean m_showConsole = true;
        public boolean m_showProgress = true;
        public boolean m_showPermission = true;

        public int m_initialScale = -1; // possible values 0 to 100,  -1=don't set

        public List<String> m_memList = new ArrayList<>();

        // -----------------------------------------------------------------------------------------
        public void saveState() {
            if (m_webView != null) {
                m_webViewState = new Bundle();
                m_webViewState.putBoolean("addUserClient", m_addUserClient);
                m_webViewState.putBoolean("addChromeClient", m_addChromeClient);
                m_webViewState.putBoolean("showConsole", m_showConsole);
                m_webViewState.putBoolean("showProgress", m_showProgress);
                m_webViewState.putBoolean("showPermission", m_showPermission);
                m_webViewState.putInt("initialScale", m_initialScale);
                m_webViewState.putBundle("webView", WebSettingsBundler.getBundleFor(m_webView));
            }
        }
        
        public void restoreState() {
            if (m_webViewState != null &&  m_webViewState.keySet().contains("webView")) {
                m_addUserClient = m_webViewState.getBoolean("addUserClient");
                m_addChromeClient = m_webViewState.getBoolean("addChromeClient");
                m_showConsole = m_webViewState.getBoolean("showConsole");
                m_showProgress = m_webViewState.getBoolean("showProgress");
                m_showPermission = m_webViewState.getBoolean("showPermission");
                m_initialScale = m_webViewState.getInt("initialScale");
                if (m_webView != null)
                    WebSettingsBundler.restoreWebView(m_webView, m_webViewState.getBundle("webView"));
            }
        }
    }

    /**
     * @return WebInfo object.
     */
    WebInfo getWebInfo();

    /**
     * Set WebInfo object.
     * @param webInfo
     */
    void setWebInfo(WebInfo webInfo);

    // =============================================================================================
    // Notification of WebShare state change.

    interface ChangeListener {
        void onChange(WebInfo webInfo);
    }

    void registerViewChangeListener(ChangeListener changeListener);
    void unregisterViewChangeListener(ChangeListener changeListener);
    void notifyChange();
}
