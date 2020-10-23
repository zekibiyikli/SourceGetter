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
import java.util.ArrayList;

public class vFilmesOnline extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    public static ArrayList<String> movieUrls;
    public static ArrayList<String> movieQualities;

    @Override
    protected Void doInBackground(String... strings) {

        movieUrls=new ArrayList<>();
        movieQualities=new ArrayList<>();

        String baseUrl=strings[0];

        try {
            baseUrl=baseUrl.replace("/v/", "/api/source/");
            System.out.println(baseUrl);

            URL url = new URL (baseUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            String text = null;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                text=inputLine;
            }
            in.close();

            JSONObject jsonObject=new JSONObject(text);
            JSONArray data=jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); ++i) {
                final JSONObject jsonUrl = data.getJSONObject(i);
                System.out.println(jsonUrl);
                Connection.Response response = Jsoup.connect(jsonUrl.getString("file")).followRedirects(true).ignoreContentType(true).execute();
                String resultUrl=response.url().toString();
                movieUrls.add(resultUrl);
                movieQualities.add(jsonUrl.getString("label"));
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
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
            Log.e("XModels",String.valueOf(xModels.size()));
            SourceGetter.onComplete.onTaskCompleted(xModels, true);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}
