package reciclapp.reciclapp.Reciclaje;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import reciclapp.reciclapp.R;

public class Manualidades extends AppCompatActivity
{
    private WebView pagina;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualidades);

        pagina = findViewById(R.id.wvPagina_manualidades);

        WebSettings webSettings = pagina.getSettings();
        pagina.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        pagina.loadUrl("https://artes.uncomo.com/manualidades-de-materiales-reciclados/");
    }

    @Override
    public void onBackPressed() {
        if (pagina.canGoBack())
        {
            pagina.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}