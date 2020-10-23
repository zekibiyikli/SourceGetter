package com.zbiyikli.sgetter.Domains;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class UpToStream2 {
    private static WebView webView;
    private static OnDone onDone;
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void get(Context context, String js, final OnDone onDone){
        this.onDone = onDone;
        webView = new WebView(context);
        Log.e("Zeki",js);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                System.out.println(consoleMessage.message());
                String msg = consoleMessage.message().toLowerCase();
                if (msg.contains("sources is not defined")){
                    destroyWebView();
                    onDone.result(null);
                }else if (consoleMessage.message().toLowerCase().contains("error")){
                    destroyWebView();
                    System.out.println("Retry");
                    onDone.result(null);
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.addJavascriptInterface(new MyInterface(),"xGetter");
        inject(js);
    }

    private static void inject(String js){
        webView.loadUrl("javascript: (function() { \"use strict\"; "+js+";\nxGetter.fuck(JSON.stringify(sources));})()");
    }

    private static void destroyWebView() {
        webView.loadUrl("about:blank");
    }

    private static class MyInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void fuck(final String html) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    result(html);
                }
            });
        }
    }

    private static void result(String result){
        destroyWebView();
        onDone.result(result);
    }

    public interface OnDone{
        void result(String result);
        void retry();
    }
}
