package com.machine.thee.presentation;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

import com.getcapacitor.Plugin;

import java.util.function.Function;

public class SecondaryDisplay extends Presentation {

    CapacitorPresentationPlugin capPlugin = new CapacitorPresentationPlugin();

    protected String url = "";
    protected String baseUrl = "";

    public SecondaryDisplay(Context outerContext, Display display, String baseUrl) {
        super(outerContext, display);
        this.baseUrl = baseUrl;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_display);

         WebView webView = findViewById(R.id.secondary_webview);
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                webSettings.setDomStorageEnabled(true);
                webSettings.setDatabaseEnabled(true);
                webSettings.setMediaPlaybackRequiresUserGesture(false);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//                 webSettings.setAppCacheEnabled(true);
                webSettings.setAllowContentAccess(true);
                webSettings.setAllowFileAccess(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setSupportMultipleWindows(true);
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);


                webSettings.setAllowFileAccessFromFileURLs(true);

                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.setWebContentsDebuggingEnabled(true);

                String path = url;

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String _url) {
                capPlugin.notifyToSuccess(webView, _url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    capPlugin.notifyToFail(webView, error.getErrorCode());
                } else {
                    capPlugin.notifyToFail(webView, 400);
                }
            }
        });

        webView.loadDataWithBaseURL(baseUrl, path, "text/html", "utf-8", baseUrl);
    }

    public void loadUrl(String url) {
       this.url = url;
    }

    public void loadBaseUrl(String baseUrl) {
           this.baseUrl = baseUrl;
    }
}