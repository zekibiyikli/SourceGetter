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

import java.util.ArrayList;

public class Fansubs extends AsyncTask<String, Void, Void> {

    ArrayList<XModel> xModels;
    String url = "";
    public static ArrayList<String> movieUrls;
    public static ArrayList<String> movieQualities;
    @Override
    protected Void doInBackground(String... strings) {

        movieUrls=new ArrayList<>();
        movieQualities=new ArrayList<>();

        try {
            Document doc=Jsoup.connect(strings[0]).get();
            Elements elements=doc.getElementsByTag("source");
            for(int i=0;i<elements.size();i++) {
                movieUrls.add(elements.get(i).attr("src"));
                movieQualities.add(elements.get(i).attr("res"));
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

