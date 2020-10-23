package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class Uqload extends AsyncTask<String, Void, Void> {

    static ArrayList<XModel> xModels;
    static String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            Document doc = Jsoup.connect(strings[0])
                    .get();

            String[] split1=doc.html().split("sources:");
            String[] split2=split1[1].split("\"");

           url=split2[1];
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
