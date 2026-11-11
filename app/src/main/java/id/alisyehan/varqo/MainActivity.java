package id.alisyehan.varqo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.WindowInsets;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;
    private static final String HOME = "file:///android_asset/index.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(246, 251, 253));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(false);
        webView.getSettings().setAllowFileAccessFromFileURLs(false);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new PrintBridge(), "VARQOAndroid");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // APK tidak membuka situs dari luar; semuanya tetap pada file lokal.
                String url = request.getUrl().toString();
                return !url.startsWith("file:///android_asset/");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Original HTML tetap dipakai; hanya cetak dan gaya bingkai HP
                // yang disesuaikan ketika dibuka sebagai aplikasi Android.
                view.evaluateJavascript("window.print = function(){VARQOAndroid.print();};"
                    + "document.documentElement.classList.add('varqo-android');"
                    + "var s=document.createElement('style');"
                    + "s.textContent='html.varqo-android body{padding:0;background:#f6fbfd;display:block}'"
                    + "+'html.varqo-android .phone{width:100%;min-height:100vh;box-shadow:none}'"
                    + "+'html.varqo-android .phone::before,html.varqo-android .phone::after{display:none!important}'"
                    + "+'html.varqo-android .topbar{padding-top:10px;height:50px}'"
                    + "+'html.varqo-android .sacred-minimal-splash{min-height:100vh}';"
                    + "document.head.appendChild(s);", null);
            }
        });

        // Android 15+: beri ruang untuk status bar dan navigation bar.
        if (Build.VERSION.SDK_INT >= 35) {
            webView.setOnApplyWindowInsetsListener((view, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsets.Type.systemBars());
                view.setPadding(0, systemBars.top, 0, systemBars.bottom);
                return insets;
            });
        }

        setContentView(webView);
        webView.loadUrl(HOME);
    }

    private class PrintBridge {
        @JavascriptInterface
        public void print() {
            runOnUiThread(() -> {
                PrintManager manager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                if (manager == null || webView == null) return;
                PrintDocumentAdapter adapter = webView.createPrintDocumentAdapter("VARQO");
                manager.print("VARQO", adapter, new PrintAttributes.Builder().build());
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (webView == null) { finish(); return; }
        webView.evaluateJavascript("(function(){"
            + "var x=document.getElementById('dashboardPage');"
            + "if(x&&x.classList.contains('active')){showPage('splashPage');return 'back';}"
            + "return 'exit';})()", result -> {
                if (result == null || result.contains("exit")) finish();
            });
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.removeJavascriptInterface("VARQOAndroid");
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
