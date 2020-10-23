package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.json.JSONArray;
import org.json.JSONObject;
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

public class Vidia extends AsyncTask<String, Void, Void> {

    ArrayList<XModel> xModels;
    String url = "";
    static ArrayList<String> urls;
    static ArrayList<String> qualities;

    @Override
    protected Void doInBackground(String... strings) {
        urls=new ArrayList<>();
        qualities=new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://vidia.tv/embed-kku6f658piti.html")
                    .get();
            Elements elements=doc.getElementsByTag("script");
            String eval=elements.get(9).data();
            JsUnpacker jsUnpacker = new JsUnpacker(eval);
            String json=jsUnpacker.unpack().split("sources:")[1].split(",tracks")[0];
            JSONArray array=new JSONArray(json);
            JSONObject object=new JSONObject(array.get(0).toString());
            url=object.getString("file").replace("dash", "hls").toString().replace("manifest.mpd", "master.m3u8");
            Log.e("Zeki",url);
            getRequest(url,strings[0]);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("Zeki","Girdi3");
        if(!urls.isEmpty()){
            xModels = new ArrayList<>();
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

    public static void getRequest(String baseurl,String referer){
        Log.e("Zeki","Girdi1");
        try {
            URL url = new URL(baseurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");
            con.setRequestProperty("referer",referer);
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();
            linkParse(content.toString(),referer);
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
    }

    public static void linkParse(String text,String referer) {
        Log.e("Zeki","Girdi2");
        String[] t= text.split("\n");
        for(int i=1;i<t.length-1;i=i+2) {
            if(!t[i].equals("")) {
                String[] split1=t[i].split("RESOLUTION=");
                String[] split2=split1[1].split(",FRAME-RATE");
                urls.add(t[i+1]+"+++"+referer);
                qualities.add(split2[0]);
            }
        }
    }


}
