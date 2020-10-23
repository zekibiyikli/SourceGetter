package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MyMailRu extends AsyncTask<String, Void, Void> {

    ArrayList<XModel> xModels;
    String url = "";
    public static ArrayList<String> movieUrls;
    public static ArrayList<String> movieQualities;
    public static String referer="";
    @Override
    protected Void doInBackground(String... strings) {

        referer=strings[0];
        String requestLink=getMetaData(strings[0]);

        getList(getRequest(requestLink));

        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        xModels = new ArrayList<>();
        Log.e("Size",String.valueOf(movieUrls.size()));
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

    public static String getMetaData(String url){
        Log.e("URL",url);
        try {
            if(url.contains("video/embed")){
                Document doc = Jsoup.connect(url).get();
                Elements elements=doc.select("script[type=text/plain]");
                String[] split1=elements.get(9).toString().split("\"metadataUrl\":\"//");
                String[] split2=split1[1].split("\",\"showPauseRoll\":");

                Log.e("URL",split2[0]);
                return "https://"+split2[0];
            }else{
                String user_agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36";
                Document doc = org.jsoup.Jsoup.connect(url)
                        .userAgent(user_agent)
                        .get();

                Elements scriptElements = doc.select("div");

                for (Element element :scriptElements ){
                    String metadata=element.select("div").first().attr("data-meta-url");
                    if (metadata.contains("http")){
                        Log.e("URL",metadata);
                        return metadata;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getRequest(String url){
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("Cookie", "video_key=96cabd372ad4b77959d88ad6f12f9d218dbced8a");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();

        }
        return "";
    }
    public static void getList(String request){

        movieUrls=new ArrayList<>();
        movieQualities=new ArrayList<>();
        try{
            JSONObject jsonObject=new JSONObject(request);
            JSONArray urlList=jsonObject.getJSONArray("videos");

            for (int i = 0; i <urlList.length() ; i++) {
                JSONObject objects=urlList.getJSONObject(i);
                String videos1Url="https:"+objects.getString("url").replace(".mp4", ".mp4/stream.mpd");
                movieUrls.add(videos1Url);
                movieQualities.add(objects.getString("key"));
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }
}
