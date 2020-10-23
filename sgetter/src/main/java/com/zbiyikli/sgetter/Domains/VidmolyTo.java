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

public class VidmolyTo extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    static ArrayList<String> urls;
    static ArrayList<String> qualities;
    static Document doc;
    @Override
    protected Void doInBackground(String... strings) {

        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        try {
            Document doc = Jsoup.connect(strings[0]).get();

            String[] split1=doc.html().split("sources: ");
            String[] split2=split1[1].split("],");

            String resultJson=split2[0]+"]";
            resultJson=resultJson.replace("file", "\"file\"").replace("label", "\"label\"");

            JSONArray array=new JSONArray(resultJson);
            for(int i=1;i<array.length();i++) {
                JSONObject object=new JSONObject(array.get(i).toString());
                String file=object.getString("file").toString();
                String label=object.getString("label").toString();
                urls.add(file);
                qualities.add(label);
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

}