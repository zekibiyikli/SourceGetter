package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class Openplay extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    @Override
    protected Void doInBackground(String... strings) {
        Document doc;
        try {
            doc = Jsoup.connect(strings[0])
                    .get();

            String[] split1=doc.html().split("\"file\": \"");
            String[] split2=split1[1].split("\",");

            url=split2[0];

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        xModels = new ArrayList<>(1);
        XModel xModel = new XModel();
        if (!url.equals("")) {
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}

