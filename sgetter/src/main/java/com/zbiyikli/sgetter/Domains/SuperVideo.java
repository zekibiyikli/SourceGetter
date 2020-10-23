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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SuperVideo extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    static ArrayList<String> urls;
    static ArrayList<String> qualities;
    static Document doc;

    @Override
    protected Void doInBackground(String... strings) {
        Log.e("URL","4 Girdi");

        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        Document doc = null;

        try{

            doc = Jsoup.connect(strings[0])
                    .get();
            Elements elements=doc.getElementsByTag("script");
            String eval=elements.get(13).data();
            JsUnpacker jsUnpacker = new JsUnpacker(eval);

            String json=jsUnpacker.unpack().split("sources:")[1].split(",image")[0];
            JSONArray array=new JSONArray(json);
            JSONObject object=new JSONObject(array.get(0).toString());
            String resultUrl=object.getString("file");
            getRequest(resultUrl,"https://supervideo.tv/",strings[0]);


        }catch (Exception e){
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
        Log.e("URL",url);
        super.onPostExecute(aVoid);
    }

    public static void getRequest(String baseurl,String host,String referer){
        final String accept="*/*";
        final String accept_encoding = "gzip, deflate, br";
        final String accept_language = "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7,ar-EG;q=0.6,ar;q=0.5,az-AZ;q=0.4,az;q=0.3";
        final String connection = "keep-alive";
        final String origin = "https://supervideo.tv";
        final String sec_fetch_mode="cors";
        final String sec_fetch_site="cross-site";
        final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36";

        try {
            URLConnection conn = new URL(baseurl).openConnection();
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Referer", referer);
            conn.setRequestProperty("Accept-Language", accept_language);
            conn.setRequestProperty("Accept-Encoding", accept_encoding);
            conn.setRequestProperty("Accept", accept);
            conn.setRequestProperty("Origin", origin);
            conn.setRequestProperty("Connection", connection);
            conn.setRequestProperty("Host", host);
            conn.setRequestProperty("Sec-Fetch-Mode", sec_fetch_mode);
            conn.setRequestProperty("Sec-Fetch-Site", sec_fetch_site);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();
            System.out.println(content.toString());
            linkParse(content.toString());
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
    }

    public static void linkParse(String text) {
        String[] t= text.split("\n");

        int a=0;
        for(int i=1;i<t.length;i=i+2) {
            if (!t[i].equals("")) {
                String[] split1=t[i].split("RESOLUTION=");
                String[] split2=split1[1].split(",FRAME-RATE");
                urls.add(t[i+1]);
                qualities.add(split2[0]);
                a++;
            }else {
                break;
            }
        }
    }

}
