package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class Okru extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    @Override
    protected Void doInBackground(String... strings) {
        try {
            if(!strings[0].contains("m.ok.ru")){
                strings[0]=strings[0].replace("ok.ru","m.ok.ru");
                strings[0]=strings[0].replace("videoembed","video");
            }

            Log.e("URL",strings[0]);
            Document doc = Jsoup.connect(strings[0])
                    .get();

            Element el=doc.select("div.mvplayer_cont").select("a").first();
            if(el!=null){
                url=el.attr("href");
                Log.e("URL",url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        xModels = new ArrayList<>(1);
        XModel xModel = new XModel();
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
