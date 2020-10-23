package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class ProStream extends AsyncTask<String, Void, Void> {

    ArrayList<XModel> xModels;
    String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            String user_agent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";
            Document doc = Jsoup.connect(strings[0])
                    .userAgent(user_agent)
                    .get();
            String html=doc.html();

            Element el=doc.select("meta[name=og:image]").first();
            String[] baseLink=el.attr("content").split("/i");

            String[] split1=html.split("poster\\|mp4");
            String[] split2=split1[1].split("sources\\|Player");
            url=baseLink[0]+"/"+split2[0].substring(1,split2[0].length()-1).substring(0,split2[0].length()-2)+"/v.mp4";

        }catch(Exception e) {

        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        xModels = new ArrayList<>(1);
        XModel xModel = new XModel();
        if (!url.equals("")){
            Log.e("URL",url);
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}

