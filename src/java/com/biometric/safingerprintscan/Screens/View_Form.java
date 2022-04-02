package com.biometric.safingerprintscan.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.biometric.safingerprintscan.R;

public class View_Form extends AppCompatActivity {

    WebView webview;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_form);

        webview = findViewById(R.id.webview);
        progressDialog = new ProgressDialog(View_Form.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Opening form . . .");

        webview.requestFocus();
        webview.getSettings().setJavaScriptEnabled(true);
        String url = getIntent().getStringExtra("form_url");

        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webview.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if(newProgress < 100){
                    progressDialog.show();
                }
                if(newProgress == 100){
                    progressDialog.dismiss();
                }

            }
        });

    }
}