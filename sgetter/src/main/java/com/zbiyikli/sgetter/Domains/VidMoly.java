package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

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

public class VidMoly extends AsyncTask<String, Void, Void> {
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
            /*
            String baseUrl=strings[0];

            Document doc = Jsoup.connect(baseUrl)
                    .get();

            String[] split1=doc.html().split("file:\"");
            String[] split2=split1[1].split("\"");


            getRequest2(split2[0],baseUrl);
             */

            String baseUrl=strings[0];

            Document doc = Jsoup.connect(baseUrl)
                    .get();

            String[] split1=doc.html().split("sources: ");
            String[] split2=split1[1].split("],");

            String resultJson=split2[0]+"]";
            resultJson=resultJson.replace("file", "\"file\"").replace("label", "\"label\"");

            JSONArray array=new JSONArray(resultJson);
            for(int i=0;i<array.length();i++) {
                JSONObject object =new JSONObject(array.get(i).toString());
                if(object.get("file").toString().contains(".mp4")) {
                    urls.add(object.get("file")+"+++"+baseUrl);
                    qualities.add(object.get("label").toString());
                }
            }


        }catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        Log.e("URL","4 GİRDİ");
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

    public static void getRequest2(String baseurl,String urll){
        try {
            URL url = new URL(baseurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("referer",urll);
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();
            linkParse2(content.toString(),urll);
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
    }

    public static void linkParse2(String text,String baseUrl) {
        String[] t= text.split("\n");
        for(int i=1;i<t.length-1;i=i+2) {
            if(!t[i].equals("")) {
                String[] split1=t[i].split("RESOLUTION=");
                String[] split2=split1[1].split(",CODECS");
                urls.add(t[i+1]+"+++"+baseUrl);
                qualities.add(split2[0]);
            }
        }
    }


}