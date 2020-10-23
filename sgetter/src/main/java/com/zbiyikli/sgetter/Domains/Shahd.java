package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Shahd extends AsyncTask<String, Void, Void> {
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
            String resultUrl=strings[0];
            if (!resultUrl.contains("?content=1")){
                resultUrl=resultUrl+"?content=1";
            }
            Document doc = Jsoup.connect(resultUrl)
                    .get();

            Elements el=doc.select("source");

            for(int i=0;i<el.size();i++) {
                if(el.get(i).attr("src").toString().contains("google")) {
                    urls.add(el.get(i).attr("src").toString().trim());
                    qualities.add(el.get(i).attr("label"));
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