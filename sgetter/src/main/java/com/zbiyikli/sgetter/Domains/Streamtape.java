package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Streamtape extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {

            String baseUrl=strings[0];
            Document doc=Jsoup.connect(baseUrl).get();
            Elements scripts=doc.getElementsByTag("script");
            for(int i=0;i<scripts.size();i++) {
                String script=scripts.get(i).data();
                if(script.contains("document.getElementById(\"videolink\").innerHTML")) {
                    url="https:"+scripts.get(i).data().replace("document.getElementById(\"videolink\").innerHTML = \"", "").replace("\";", "");
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        xModels = new ArrayList<>(1);
        XModel xModel = new XModel();
        if (!url.equals("")){
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);

    }
}
