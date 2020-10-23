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

public class SpeedVideo extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            /*
            Document doc = Jsoup.connect(strings[0])
                    .get();


            String[] split1=doc.html().split("var linkfile =\"");
            String[] split2=split1[1].split("\"");
            //url=split2[0];

            Connection.Response response = Jsoup.connect(split2[0]).followRedirects(true).ignoreContentType(true).execute();
            url = response.url().toString();

            /*
            String s=split2[0];
            String[] resultSplit=s.split("v.mp4");
            url=resultSplit[0].replace("https://speedvideo.net/getvideo///", "http://163.172.209.31")+"v.mp4";
            */

            /*
            Document doc=Jsoup.connect(strings[0])
                    .get();

            Elements elements=doc.getElementsByTag("script");
            String[] split1=elements.get(6).toString().split("var linkfile =\"");
            String[] split2=split1[1].split("\";");
            String[] split3=split2[1].split("/");

            url="https://cdnvideo32.speedvideo.net/"+split3[9]+"/v.mp4";

             */
            Document doc=Jsoup.connect(strings[0])
                    .get();

            Elements elements=doc.getElementsByTag("script");
            String[] split1=elements.get(6).toString().split("var linkfile =\"");
            String[] split2=split1[1].split("\";");


            String base=split2[0];
            url=base;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        xModels = new ArrayList<>(1);
        XModel xModel = new XModel();
        Log.e("URL2",url);
        if (!url.equals("")){
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);

    }

    private static SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }

}
