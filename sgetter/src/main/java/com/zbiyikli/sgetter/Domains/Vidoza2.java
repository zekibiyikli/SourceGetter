package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Vidoza2 extends AsyncTask<String, Void, Void> {

    static ArrayList<XModel> xModels;
    static String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            Document doc = Jsoup.connect(strings[0]).get();
            Elements elements=doc.getElementsByTag("source");
            url=elements.get(0).attr("src");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    protected void onPostExecute(final Void aVoid) {
        if(!url.equals("")){
            xModels = new ArrayList<>(1);
            final XModel xModel = new XModel();
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
        }else{
            SourceGetter.onComplete.onError();
        }
        Log.e("URL",url);
        super.onPostExecute(aVoid);
    }
}
