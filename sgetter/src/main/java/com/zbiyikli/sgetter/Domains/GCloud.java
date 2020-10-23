package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class GCloud extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    public static ArrayList<String> movieUrls;
    public static ArrayList<String> movieQualities;

    @Override
    protected Void doInBackground(String... strings) {

        movieUrls=new ArrayList<>();
        movieQualities=new ArrayList<>();

        try {
            String baseurl=strings[0];
            baseurl=baseurl.replace("/v/","/api/source/");
            URL url = new URL(baseurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();

            JSONObject object=new JSONObject(content.toString());
            JSONArray array=new JSONArray(object.get("data").toString());

            for(int i=0;i<array.length();i++) {
                JSONObject result=new JSONObject(array.get(i).toString());
                Connection.Response response = Jsoup.connect(result.get("file").toString())
                        .followRedirects(true)
                        .ignoreContentType(true)
                        .execute();

                movieUrls.add(response.url().toString());
                movieQualities.add(result.get("label").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        xModels = new ArrayList<>(1);
        if (!movieUrls.isEmpty()){
            for(int i=0;i<movieUrls.size();i++){
                XModel xModel = new XModel();
                xModel.setUrl(movieUrls.get(i));
                xModel.setQuality(movieQualities.get(i));
                Log.e("Link",movieUrls.get(i));
                Log.e("Quality",movieQualities.get(i));
                xModels.add(xModel);
            }
            SourceGetter.onComplete.onTaskCompleted(xModels, true);
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
