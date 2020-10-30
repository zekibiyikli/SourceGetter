package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mediafire extends AsyncTask<String, Void, Void> {

    static ArrayList<XModel> xModels;
    static String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            Document doc=Jsoup.connect(strings[0]).get();
            url=doc.getElementsByClass("input popsok").get(0).attr("href");

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
        super.onPostExecute(aVoid);
    }
}
