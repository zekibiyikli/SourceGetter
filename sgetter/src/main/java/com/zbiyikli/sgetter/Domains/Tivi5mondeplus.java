package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Tivi5mondeplus extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    static ArrayList<String> urls;
    static ArrayList<String> qualities;
    boolean isInside=false;
    static Document doc;

    @Override
    protected Void doInBackground(String... strings) {

        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        try {
            Document doc = Jsoup.connect(strings[0])
                    .get();

            String baseUrl=doc.html().split("\"contentUrl\": \"")[1].split("\"")[0];

//            Elements el=doc.getElementsByTag("script");
//            String resultJson=el.get(26).data().toString().trim().replaceAll("\\s+", "");
//
//            JSONObject object=new JSONObject(resultJson);
//            System.out.println(object.get("contentUrl"));

            URL url = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();

            String[] t= content.toString().split("\n");
            for(int i=1;i<t.length-2;i=i+2) {
                if(!t[i].equals("")) {
                    String[] split11=t[i].split("RESOLUTION=");
                    String[] split22=split11[1].split(",CODECS");
                    urls.add(t[i+1]);
                    qualities.add(split22[0]);
                }
            }


        }catch(Exception e) {
            e.printStackTrace();
        }


        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        if(!url.equals("") || !urls.isEmpty()){
            xModels = new ArrayList<>(1);
            for(int i=0;i<urls.size();i++){
                final XModel xModel = new XModel();
                xModel.setUrl(urls.get(i));
                xModel.setQuality(qualities.get(i));
                xModels.add(xModel);
            }
            SourceGetter.onComplete.onTaskCompleted(xModels, true);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}