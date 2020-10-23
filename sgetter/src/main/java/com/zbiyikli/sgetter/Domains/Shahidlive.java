package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class Shahidlive extends AsyncTask<String, Void, Void> {

    ArrayList<XModel> xModels;
    String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            String baseUrl=strings[0];
            Document doc= Jsoup.connect(baseUrl)
                    .get();

            String iframe="https:"+doc.getElementsByTag("iframe").attr("src");
            Document doc2= Jsoup.connect(iframe)
                    .get();

            url="https:"+doc2.getElementsByTag("source").attr("src");

        }catch(Exception e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        xModels = new ArrayList<>(1);
        XModel xModel = new XModel();
        if (!url.equals("")){
            Log.e("URL",url);
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}

