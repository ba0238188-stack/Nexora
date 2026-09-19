package com.nexora.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
private static final int FILE_PICKER = 1001;
private static final int MEDIA_PERMISSION = 1002;
private WebView webView;
private ValueCallback<Uri[]> uploadCallback;

@Override public void onCreate(Bundle savedInstanceState) {  
    super.onCreate(savedInstanceState);  
    webView = new WebView(this);  
    setContentView(webView);  
    WebSettings s = webView.getSettings();  
    s.setJavaScriptEnabled(true);  
    s.setDomStorageEnabled(true);  
    s.setDatabaseEnabled(true);  
    s.setAllowFileAccess(true);  
    s.setAllowContentAccess(true);  
    s.setMediaPlaybackRequiresUserGesture(false);  
    s.setSupportZoom(false);  
    webView.setWebViewClient(new WebViewClient() {  
        @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) { return false; }  
    });  
    webView.setWebChromeClient(new WebChromeClient() {  
        @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {  
            if (uploadCallback != null) uploadCallback.onReceiveValue(null);  
            uploadCallback = callback;  
            try {  
                Intent i = params.createIntent();  
                startActivityForResult(i, FILE_PICKER);  
                return true;  
            } catch (Exception e) {  
                uploadCallback = null;  
                return false;  
            }  
        }  
    });  
    if (android.os.Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)  
        requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, MEDIA_PERMISSION);  
    webView.loadUrl("https://ancient-dream-fbbb.abualataali80.workers.dev");  
}  

@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    super.onActivityResult(requestCode, resultCode, data);  
    if (requestCode == FILE_PICKER && uploadCallback != null) {  
        Uri[] result = WebChromeClient.FileChooserParams.parseResult(resultCode, data);  
        uploadCallback.onReceiveValue(result);  
        uploadCallback = null;  
    }  
}  

@Override public void onBackPressed() {  
    if (webView.canGoBack()) webView.goBack(); else super.onBackPressed();  
}

}
