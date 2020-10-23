package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Imdb extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    static ArrayList<String> urls;
    static ArrayList<String> qualities;
    int count=0;

    @Override
    protected Void doInBackground(String... strings) {
        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        try {
            String url=strings[0];
            if(!url.contains("/embed/video/")){
                url=url.replace("/video/","/embed/video/");
            }

            Document doc=Jsoup.connect(url).get();
            String split=doc.html().split("\"playbackData\":\\[\"")[1].split("\"\\],\"videoInfoKey\":")[0].replace("\\", "");
            System.out.println(split);
            JSONArray array1=new JSONArray(split);
            JSONObject object=new JSONObject(array1.get(0).toString());
            JSONArray array=new JSONArray(object.get("videoLegacyEncodings").toString());
            JSONObject object2=new JSONObject(array.get(0).toString());
            String resultUrl=object2.getString("url").trim();

            getRequest(resultUrl);

        } catch (Exception e) {
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

    public static void getRequest(String baseurl){
        try {
            URL url = new URL(baseurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();
            linkParse(baseurl,content.toString());
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
    }

    public static void linkParse(String baseUrl,String text) {
        baseUrl=baseUrl.split("/hls-")[0];
        String[] t= text.split("\n");
        for(int i=1;i<t.length-1;i=i+2) {
            if(!t[i].equals("")) {
                String[] split1=t[i].split("RESOLUTION=");
                String[] split2=split1[1].split(",CODECS");
                urls.add(baseUrl+"/"+t[i+1]);
                qualities.add(split2[0]);
            }
        }
    }


}

