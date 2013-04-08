package com.crowdspeak;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JsResult;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final WebView webview = (WebView) findViewById(R.id.webView1);
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();
		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        final Context c = this;
		webview.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading (WebView view, String url)
			{
				if(url.equals("http://9.wut.se:5000/me"))
				{
					CookieManager cookieManager = CookieManager.getInstance();
					Log.v("Login",cookieManager.getCookie("http://9.wut.se:5000/me"));
					Intent intent = new Intent(c, MessageNetworkManager.class);
					intent.putExtra("session", cookieManager.getCookie("http://9.wut.se:5000/me"));
					startService(intent);
					Intent intent2 = new Intent(c, MainActivity.class);
			        startActivity(intent2);
					return true;
				}
				webview.loadUrl(url);  
				return true;
			}
		});

		webview.loadUrl("http://9.wut.se:5000/login");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		
		return true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		CookieSyncManager.getInstance().startSync();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		CookieSyncManager.getInstance().stopSync();
	}
}