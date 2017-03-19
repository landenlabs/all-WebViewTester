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
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.landenlabs.all_webviewtester.ui.ComboBox;
import com.landenlabs.all_webviewtester.ui.MemUtil;
import com.landenlabs.all_webviewtester.ui.Ui;
import com.landenlabs.all_webviewtester.util.StringAppender;
import com.landenlabs.all_webviewtester.util.WLog;

/**
 * A fragment which manages a WebView object and various client attachments.
 * Created by Dennis Lang on 9/1/2015.
 *
 */
public class WebViewFrag extends WebFragment  implements View.OnClickListener, WebShare.ChangeListener  {

    private WebShare m_webShare;
    private WebShare.WebInfo m_webInfo;

    private ComboBox m_urlCb;
    private TextView m_statusTv;
    private TextView m_statusTitle;
    private WebView m_webView;

    private final WLog wLog = WLog.DBG;
    private void Logi(String msg, Object... args) {
        wLog.i(WLog.PID + msg, args);
    }

    // =============================================================================================
    public WebViewFrag() {
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
        super.onCreateView(inflater, container, savedInstanceState);
        m_webInfo = m_webShare.getWebInfo();

        View rootView = inflater.inflate(R.layout.web_view, container, false);
        Logi(" webView onCreateView %s", (container != null) ? container.toString() : "");
        m_urlCb = Ui.needViewById(rootView, R.id.webUrlCb);
        m_webView = Ui.needViewById(rootView, R.id.webView);
        m_statusTv = Ui.needViewById(rootView, R.id.webStatusTv);
        m_statusTitle = Ui.needViewById(rootView, R.id.webStatusBtn);
        Ui.needViewById(rootView, R.id.webStatusClearBtn).setOnClickListener(this);
        Ui.needViewById(rootView, R.id.webStatusShowTb).setOnClickListener(this);
        Ui.needViewById(rootView, R.id.webNavPrev).setOnClickListener(this);
        Ui.needViewById(rootView, R.id.webNavGo).setOnClickListener(this);
        Ui.needViewById(rootView, R.id.webNavNext).setOnClickListener(this);

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
        Logi(" webView onResume");
        m_webInfo.m_webView = m_webView;
        if (m_webInfo.m_webViewState != null) {
            m_webInfo.restoreState();
            // m_webInfo.m_webViewState = null;
            Logi(" webView onResume URL=" + m_webView.getUrl());
            m_urlCb.setText(m_webView.getUrl());
        }
        m_webShare.setWebInfo(m_webInfo);   // Calls setupWebView()
    }

    @Override
    public int getFragId() {
        return R.id.web_view_id;
    }

    @Override
    public String getName() {
        return "WebView";
    }

    @Override
    public String getDescription() {
        return "WebView Description";
    }

    @Override
    public void onDestroyView() {
        m_webShare.unregisterViewChangeListener(this);
        m_webInfo.saveState();
        Logi(" webView onDestroyView URL=" + m_webView.getUrl());
        m_webInfo.m_webView = null;
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
        setupWebView();
    }

    // =============================================================================================
    // Implementents View.OnClickListener
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.webStatusShowTb:
                Toast.makeText(view.getContext(), "Toggle Status", Toast.LENGTH_LONG).show();
                m_statusTv.setVisibility((m_statusTv.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                break;
            case  R.id.webStatusClearBtn:
                m_statusTv.setText("");
                break;
            case R.id.webNavPrev:
                m_webView.goBack();
                Toast.makeText(view.getContext(), "Go Backward", Toast.LENGTH_LONG).show();
                break;
            case R.id.webNavNext:
                m_webView.goForward();
                Toast.makeText(view.getContext(), "Go Forward", Toast.LENGTH_LONG).show();
                break;
            case R.id.webNavGo:
                m_webView.loadUrl(m_urlCb.getText());
                Toast.makeText(view.getContext(), "Load url " + m_urlCb.getText(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    // =============================================================================================
    StringAppender m_strAppender = new StringAppender();

    private void appendStatus(String msg) {
        CharSequence currentStatus = m_statusTv.getText();
        if (currentStatus.length() < 2000) {
            m_strAppender.start(currentStatus).append("\n").append(msg);
            m_statusTv.setText(m_strAppender.toString());
            m_statusTv.bringToFront();
        }
    }

    private void onPageDone(String url) {
        if (!url.startsWith("about")) {
            m_urlCb.setText(url);
            m_webShare.getWebInfo().m_memList.add(MemUtil.getMem(getActivity()) + url);
        }
    }

    private void setupWebView() {
        WebSettings webSettings = m_webView.getSettings();
        Logi(" setupWebView");

        if (m_webInfo.m_addUserClient) {
            m_webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    appendStatus(String.format("Override %s", url));
                    appendStatus(MemUtil.getMem(WebViewFrag.this.getActivity()));
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (m_webInfo.m_showProgress)
                        appendStatus(String.format("Start %s", url));
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (m_webInfo.m_showProgress)
                        appendStatus(String.format("Finished %s", url));
                    super.onPageFinished(view, url);
                    WebViewFrag.this.onPageDone(url);
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    if (m_webInfo.m_showProgress)
                        appendStatus(String.format("  Load %s", url));
                    super.onLoadResource(view, url);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    appendStatus(String.format("  Error %s\n%s", description, failingUrl));
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    appendStatus(String.format("  SslError %s", error.getUrl()));
                    super.onReceivedSslError(view, handler, error);
                }

                @Override
                public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                    appendStatus(String.format("  HttpAuthRequest %s", host));
                    super.onReceivedHttpAuthRequest(view, handler, host, realm);
                }

                @Override
                public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                    // appendStatus(String.format("  ClientCertRequest %s", request.getHost()));
                    super.onReceivedClientCertRequest(view, request);
                }
            });
        }  else {
            m_webView.setWebViewClient(null);
        }

        if (m_webInfo.m_addChromeClient) {
            m_webView.setWebChromeClient(new WebChromeClient() {

                @Override
                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                    appendStatus(consoleMessage.message());
                    return super.onConsoleMessage(consoleMessage);
                }

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    // if (m_webInfo.m_showProgress)
                    //    appendStatus(String.format("Progress %d", newProgress));
                    m_statusTitle.setText(String.format("Status %d%%", newProgress));
                    super.onProgressChanged(view, newProgress);
                }

                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    if (m_webInfo.m_showConsole)
                        appendStatus(String.format("JS Alert %s Result:%s", message, result.toString()));
                    return super.onJsAlert(view, url, message, result);
                }

                @Override
                public void onPermissionRequest(PermissionRequest request) {
                    if (m_webInfo.m_showPermission)
                        appendStatus(String.format("Permission Request:%s", request.toString()));
                    super.onPermissionRequest(request);
                }

            });
        } else {
            m_webView.setWebChromeClient(null);
        }
    }

    private int calcWebScale(View viewPort) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;     //  viewPort.getWidth();
        Double val = (Double.valueOf(width) / dm.widthPixels) * 100d;
        return val.intValue();
    }

    private WebSettings.ZoomDensity calcWebZoom() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                return WebSettings.ZoomDensity.FAR;

            case DisplayMetrics.DENSITY_MEDIUM:
                return WebSettings.ZoomDensity.MEDIUM;

            case DisplayMetrics.DENSITY_LOW:
                return WebSettings.ZoomDensity.CLOSE;

            default:
                return WebSettings.ZoomDensity.MEDIUM;
        }
    }
}
