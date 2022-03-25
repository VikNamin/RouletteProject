package com.reelsspin.diamond;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private String url = "https://hirelmany.ru/4D3PyMpD";
    public static boolean checker = false;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.startButton);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        new Thread(new Runnable() {
            public void run() {
                try{
                    String content = getContent(url);
                    if (content != ""){
                        webView.post(new Runnable() {
                            public void run() {
                                webView.loadUrl(content);
                            }
                        });
                    }
                    else {
                        checker = true;
                    }
                }
                catch (IOException ex){
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkButton();
                    }
                });
            }
        }).start();

    }

    @Override
    public void onBackPressed(){
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    public void checkButton(){
        if (checker) {
            Button button = findViewById(R.id.startButton);
            button.setVisibility(View.VISIBLE);
        }
    }

    public void onClickStart(View view) {
        Intent intent = new Intent(this, RouletteActivity.class);
        startActivity(intent);
    }

    private String getContent(String path) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader= new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}