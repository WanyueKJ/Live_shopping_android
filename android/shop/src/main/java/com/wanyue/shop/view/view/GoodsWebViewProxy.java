package com.wanyue.shop.view.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.fragment.app.FragmentActivity;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.business.webimage.MyWebViewClient;
import com.wanyue.shop.view.dialog.GalleryDialogFragment;
import java.util.List;

public class GoodsWebViewProxy extends BaseGoodItemViewProxy {
    final String mimeType = "text/html";
    final String encoding = "utf-8";
    private WebView mWebView;
    private OpenImageJavaInterface mOpenImageJavaInterface;

    @Override
    public int getLayoutId() {
        return R.layout.view_goods_web;
    }
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mOpenImageJavaInterface=new OpenImageJavaInterface(getActivity());
        mWebView = findViewById(R.id.web);
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置能够解析Javascript
        webSettings.setDomStorageEnabled(true);//设置适应Html5的一些方法

        mWebView.addJavascriptInterface(mOpenImageJavaInterface, "imagelistener");
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d("===","newProgress=="+newProgress);
                if(newProgress==100){
                   addImageClickListener(view);
                }
            }
            private void addImageClickListener(WebView webView) {
                webView.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        +"  var temp=i;    "
                        + "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                        "    }  " +
                        "}" +
                        "})()");
            }
        });
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDatabaseEnabled(false);
        mWebView.setWebViewClient(new MyWebViewClient());
    }

     public void loadHtml(String html){
        if(mOpenImageJavaInterface!=null){
           mOpenImageJavaInterface.imageUrls= StringUtil.returnImageUrlsFromHtml(html);
        }
        if(mWebView!=null){
            html = html.replace("<img", "<img style=\"display:        ;max-width:100%;\"");
            mWebView.loadDataWithBaseURL("about：blank", html, mimeType,
                    encoding, "");
        }
     }

    public class OpenImageJavaInterface {
        private Context context;
        private List<String> imageUrls;

        public OpenImageJavaInterface(Context context) {
            this.context = context;
        }
        @android.webkit.JavascriptInterface
        public void openImage(String src) {
            if(!ClickUtil.canClick()||!ListUtil.haveData(mOpenImageJavaInterface.imageUrls)){
                return;
            }
            int index=ListUtil.index(mOpenImageJavaInterface.imageUrls,src);
            if(index==-1){
                index=0;
            }
            showGalleryDialog(index);
        }
        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }
    }

    private void showGalleryDialog(int position) {
        FragmentActivity activity=getActivity();
        if(activity==null){
            return;
        }
        GalleryDialogFragment galleryDialogFragment=new GalleryDialogFragment();
        galleryDialogFragment.setGalleryViewProxy(mOpenImageJavaInterface.imageUrls,position,getViewProxyMannger());
        galleryDialogFragment.show(activity.getSupportFragmentManager());
    }
}
