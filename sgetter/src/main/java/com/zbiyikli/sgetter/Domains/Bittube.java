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

public class Bittube extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    static ArrayList<String> urls;
    static ArrayList<String> qualities;

    @Override
    protected Void doInBackground(String... strings) {

        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        try {

            String baseUrl=strings[0];
            String id=baseUrl.split("/")[5];
            String jsonUrl="https://bittube.video/api/v1/videos/"+id;
            URL url = new URL (jsonUrl);
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

            JSONObject object=new JSONObject(text.toString());
            JSONArray files=new JSONArray(object.get("files").toString());

            for(int i=0;i<files.length();i++) {
                JSONObject item =new JSONObject(files.get(i).toString());
                JSONObject resolution=new JSONObject(item.get("resolution").toString());
                String sourceQuality=resolution.getString("label").toString();
                String sourceUrl=item.getString("fileUrl").toString();
                if(!sourceQuality.equals("0p")) {
                    urls.add(sourceUrl);
                    qualities.add(sourceQuality);
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
