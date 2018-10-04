package reciclapp.reciclapp.Reciclaje;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import reciclapp.reciclapp.R;

public class GuiaReciclaje extends AppCompatActivity {

    private WebView pagina;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_reciclaje);

        pagina = findViewById(R.id.wvPagina_guiaReciclaje);

        WebSettings webSettings = pagina.getSettings();
        pagina.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        pagina.loadUrl("https://www.ecoembes.com/es/ciudadanos/envases-y-proceso-reciclaje/como-reciclar-bien");
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
