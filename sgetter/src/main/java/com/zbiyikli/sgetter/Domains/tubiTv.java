package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class tubiTv extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    static ArrayList<String> urls;
    static ArrayList<String> qualities;

    @Override
    protected Void doInBackground(String... strings) {
        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        try {
            Document doc = Jsoup.connect(strings[0])
                    .userAgent("PostmanRuntime/7.26.5").get();
            String split=doc.html().split("cloudfront")[1].split("\"")[0];
            String url="https://cloudfront"+split.replace("\\u002F","/");

            getRequest(url);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
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
    }

    public static void getRequest(String baseurl){
        try {
            URL url = new URL(baseurl);
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
            linkParse(content.toString(),baseurl);
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
    }

    public static void linkParse(String text,String baseUrl) {
        String[] t= text.split("\n");
        String[] u=baseUrl.split("/");
        String token=baseUrl.split("\\?")[1];
        for(int i=2;i<t.length-1;i=i+2) {
            if(!t[i].equals("")) {
                String[] split1=t[i].split("RESOLUTION=");
                String resultUrl="https://cloudfront.tubi.video/"+u[3]+"/"+t[i+1]+"?"+token;
                urls.add(resultUrl);
                qualities.add(split1[0]);
            }
        }
    }


}
