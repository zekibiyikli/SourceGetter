package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class FilmModu extends AsyncTask<String, Void, Void> {

    ArrayList<XModel> xModels;
    String url = "";
    public static ArrayList<String> movieUrls;
    public static ArrayList<String> movieQualities;
    @Override
    protected Void doInBackground(String... strings) {

        movieUrls=new ArrayList<>();
        movieQualities=new ArrayList<>();

        try {
            String json="";
            Document doc = Jsoup.connect(strings[0]).get();
            String[] split=doc.select("script").get(0).data().split("\\'");
            String jsonUrl="";
            if(strings[0].contains("altyazili")){
                jsonUrl="https://www.filmmodu.org/get-source?movie_id="+split[1]+"&type=en";
                json = Jsoup.connect(jsonUrl).ignoreContentType(true).execute().body();
                JSONObject jsonObject=new JSONObject(json);
                String jsonsubtitle=jsonObject.getString("subtitle");
                String subtitle="https://www.filmmodu.org"+jsonsubtitle.toString();
                JSONArray  data=jsonObject.getJSONArray("sources");
                for (int i = 0; i < data.length(); ++i) {
                    final JSONObject lastjson = data.getJSONObject(i);
//                    String resultUrl=lastjson.getString("src")+"***"+subtitle;
                    //String resultUrl=lastjson.getString("src");
                    String resultUrl=lastjson.getString("src");
                    Log.e("Url",resultUrl);
                    movieUrls.add(resultUrl);
                    movieQualities.add(lastjson.getString("label"));
                }
            }else{
                jsonUrl="https://www.filmmodu.org/get-source?movie_id="+split[1]+"&type=tr";
                json = Jsoup.connect(jsonUrl).ignoreContentType(true).execute().body();
                JSONObject jsonObject=new JSONObject(json);
                JSONArray  data=jsonObject.getJSONArray("sources");
                for (int i = 0; i < data.length(); ++i) {
                    final JSONObject lastjson = data.getJSONObject(i);
                    movieUrls.add(lastjson.getString("src"));
                    movieQualities.add(lastjson.getString("label"));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
}

