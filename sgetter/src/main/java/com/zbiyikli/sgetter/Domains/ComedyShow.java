package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class ComedyShow extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    @Override
    protected Void doInBackground(String... strings) {
        try{
            /*
            Connection.Response response = Jsoup.connect(strings[0]).followRedirects(true).execute();
            url=response.url().toString();
            */
            Document doc = Jsoup.connect(strings[0])
                    .get();

            Element el=doc.selectFirst("iframe");
            url=el.attr("src");

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        if(!url.equals("")){
            SourceGetter.onComplete.goBitLY(url);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}
