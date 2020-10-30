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

public class Mp4Upload extends AsyncTask<String, Void, Void> {

    static ArrayList<XModel> xModels;
    static String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            String baseUrl=strings[0];

            if(!baseUrl.contains("embed-")) {
                baseUrl=baseUrl.replace("com/", "com/embed-");
            }
            if(!baseUrl.contains(".html")) {
                baseUrl+=".html";
            }
            Document doc=Jsoup.connect(baseUrl).get();
            Elements scripts=doc.getElementsByTag("script");
            String packed="";
            for(int i=0;i<scripts.size();i++) {
                String script=scripts.get(i).toString();
                if(script.contains("eval(function(p,a,c,k,e,d)")) {
                    packed=scripts.get(i).data();
                    break;
                }
            }
            JsUnpacker jsUnpacker = new JsUnpacker(packed);
            String result=jsUnpacker.unpack();

            System.out.println(result);

            Pattern pattern = Pattern.compile("player.src\\(\"(.*?)\"");
            Matcher matcher = pattern.matcher(result);
            if (matcher.find())
            {
                url=matcher.group(1);
            }

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
