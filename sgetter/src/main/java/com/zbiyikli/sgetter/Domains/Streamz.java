package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

public class Streamz extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    String[] split;
    String url2;

    @Override
    protected Void doInBackground(String... strings) {
        if(strings[0].contains("/x")){
            split=strings[0].split("/x");
            url2="https://streamz.cc/getlink-"+split[1]+".dll";
            try {
                Connection.Response response = Jsoup.connect(url2).followRedirects(true).ignoreContentType(true).execute();
                url=response.url().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(strings[0].contains("/y")){
            split=strings[0].split("/y");
            url2="https://streamz.cc/getlink-"+split[1]+".dll";
            try {
                Connection.Response response = Jsoup.connect(url2).followRedirects(true).ignoreContentType(true).execute();
                url=response.url().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Connection.Response response = Jsoup.connect(strings[0]).followRedirects(true).ignoreContentType(true).execute();
                String redirectUrl=response.url().toString();
                url=redirectUrl;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(url.contains(".mp4")){
            xModels = new ArrayList<>(1);
            XModel xModel = new XModel();
            if (!url.equals("")){
                xModel.setUrl(url);
                xModels.add(xModel);
                SourceGetter.onComplete.onTaskCompleted(xModels, false);
            }else{
                SourceGetter.onComplete.onError();
            }
        }else{
            SourceGetter.onComplete.goBitLY(url);
        }
        super.onPostExecute(aVoid);
    }
}

