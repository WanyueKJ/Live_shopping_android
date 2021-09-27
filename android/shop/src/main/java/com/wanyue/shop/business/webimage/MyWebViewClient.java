package com.wanyue.shop.business.webimage;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.wanyue.common.utils.L;

public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        L.e("url=="+request.getUrl().toString());
        return super.shouldOverrideUrlLoading(view, request);
    }

    private void addImageClickListener(WebView webView) {
            webView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        window.imagelistener.openImage(this.);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                    "    }  " +
                    "}" +
                    "})()");
        }
    }
