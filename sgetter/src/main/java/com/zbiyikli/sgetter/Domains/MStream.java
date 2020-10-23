package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MStream extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    @Override
    protected Void doInBackground(String... strings) {
        Document doc;
        try {

            doc = Jsoup.connect(strings[0])
                    .get();
            Elements element=doc.getElementsByTag("meta");
            String resultUrl=element.get(5).attr("content").replace("/snapshot.jpg",".mp4");
            resultUrl=resultUrl.replace("/splash.jpg",".mp4");
            url=resultUrl;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        xModels = new ArrayList<>(1);
        XModel xModel = new XModel();
        Log.e("URL",url);
        if (!url.equals("")){
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}
