package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

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

public class Puhutv extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    static ArrayList<String> urls;
    static ArrayList<String> qualities;
    static Document doc;

    @Override
    protected Void doInBackground(String... strings) {

        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        Document doc;
        try {
            doc = Jsoup.connect(strings[0])
                    .get();

            String[] split1=doc.html().split("player.video.loader");
            String[] split2=split1[1].split(";");
            String[] split3=split2[0].split("\'");

            System.out.println(split3[3]);

            String serverUrl="https://dygvideo.dygdigital.com/api/video_info?akamai=true&PublisherId=29&ReferenceId=zeki&SecretKey=NtvApiSecret2014*";
            serverUrl=serverUrl.replace("zeki", split3[3]);

            System.out.println(serverUrl);

            URL url = new URL (serverUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            String text = null;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                text=inputLine;
            }
            in.close();

            Log.e("Zeki",text);

            JSONObject mainObject=new JSONObject(text.toString());
            JSONObject dataObject=new JSONObject(mainObject.get("data").toString());
            JSONObject flavorObject=new JSONObject(dataObject.get("flavors").toString());
            String playlist=flavorObject.getString("hls").toString();

            String base=playlist.split("playlist")[0];

            Log.e("Zeki",playlist);
            Log.e("Zeki",base);

            getRequest(playlist,base);

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

    public static void getRequest(String baseurl,String base){
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
            linkParse3(content.toString(),base);
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
    }

    public static void linkParse3(String text,String base) {
        String[] t= text.split("\n");
        int baseIndex=1;
        if(t[1].contains("#EXT-X-VERSION")){
            baseIndex=baseIndex+1;
        }
        for(int i=baseIndex;i<t.length-1;i=i+2) {
            Log.e("Zeki",t[i]);
            if (!t[i].equals("")) {
                String[] split1 = t[i].split("RESOLUTION=");
                if (split1.length > 1) {
                    String[] split2 = split1[1].split(",");
                    Log.e("Zeki",base + t[i + 1]);
                    Log.e("Zeki",split2[0]);
                    urls.add(base + t[i + 1]);
                    qualities.add(split2[0]);
                }
            }
        }
    }
}
