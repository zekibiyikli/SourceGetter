package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Feelem extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    boolean isOther=false;
    static ArrayList<String> urls;
    static ArrayList<String> qualities;

    @Override
    protected Void doInBackground(String... strings) {
        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        try{
            Document doc=Jsoup.connect(strings[0]).get();

            String videoSrc=doc.getElementById("video").attr("src");

            if(videoSrc.equals("")) {
                isOther=false;
                Elements els=doc.getElementsByTag("source");
                for(int i=0;i<els.size();i++) {
                    urls.add(els.get(i).attr("src"));
                    qualities.add(els.get(i).attr("label"));
                }
            }else {
                isOther=true;
                url=videoSrc;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        if(isOther){
            if(!url.equals("")){
                SourceGetter.onComplete.goBitLY(url);
            }else{
                SourceGetter.onComplete.onError();
            }
        }else{
            if(!url.equals("") || !urls.isEmpty()){
                xModels = new ArrayList<>(1);
                for(int i=0;i<urls.size();i++){
                    final XModel xModel = new XModel();
                    xModel.setUrl(urls.get(i));
                    xModel.setQuality(qualities.get(i));
                    xModels.add(xModel);
                }
                SourceGetter.onComplete.onTaskCompleted(xModels, true);
            }else{
                SourceGetter.onComplete.onError();
            }
        }
        super.onPostExecute(aVoid);
    }
}

