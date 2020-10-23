package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class VoidBoost extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    static ArrayList<String> urls;
    static ArrayList<String> qualities;
    boolean isInside = false;
    static Document doc;

    @Override
    protected Void doInBackground(String... strings) {

        urls = new ArrayList<>();
        qualities = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(strings[0])
                    .get();

            String[] split1 = doc.html().split("\'file\': \'");
            String[] split2 = split1[1].split("\'");

            String[] split11 = split2[0].split(",");
            for (int i = 0; i < split11.length; i++) {
                String quality = split11[i].split("]")[0].substring(1);
                String url = split11[i].split("]")[1].split(" or ")[1];
                urls.add(url);
                qualities.add(quality);
            }

        } catch (Exception e) {
            System.out.println(e);
        }


        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        if (!url.equals("") || !urls.isEmpty()) {
            xModels = new ArrayList<>(1);
            for (int i = 0; i < urls.size(); i++) {
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

