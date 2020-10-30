package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filerio extends AsyncTask<String, Void, Void> {

    static ArrayList<XModel> xModels;
    static String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            Document doc=Jsoup.connect(strings[0]).get();
            Elements elements=doc.getElementsByTag("input");
            ArrayList list=new ArrayList<String>();
            for(int i=0;i<elements.size();i++) {
                list.add(elements.get(i).attr("value"));
            }
            Document doc2=Jsoup.connect(strings[0])
                    .data("op", list.get(0).toString())
                    .data("usr_login", list.get(1).toString())
                    .data("id", list.get(2).toString())
                    .data("fname", list.get(3).toString())
                    .data("referer", list.get(4).toString())
                    .data("method_free", list.get(5).toString())
                    .post();

            url=doc2.getElementsByTag("source").get(0).attr("src");

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
