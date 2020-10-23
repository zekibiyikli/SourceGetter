package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mycima extends AsyncTask<String, Void, Void> {

    ArrayList<XModel> xModels;
    String url = "";
    public static ArrayList<String> movieUrls;
    public static ArrayList<String> movieQualities;
    public static String referer="";
    @Override
    protected Void doInBackground(String... strings) {

//        String urls,quality;
        movieUrls=new ArrayList<>();
        movieQualities=new ArrayList<>();

        try{
//            Document doc = Jsoup.connect(strings[0]).get();
//            Elements elements=doc.getElementsByTag("a");
//            for(int i=0;i<elements.size();i=i+2) {
//                String url=elements.get(i).attr("href");
//                if(url.contains(".mp4")) {
//                    urls = elements.get(i).attr("href");
//                    Pattern p = Pattern.compile("\\d\\d\\dp",Pattern.CASE_INSENSITIVE);
//                    Matcher m = p.matcher(urls);
//                    while (m.find()) {
//                        quality = m.group();
//                        urls=Jsoup.connect("http://uppom.live/ms9fntx0c36i/Ezbet.Adam.2009.1080p.HD.MyCima.ME.mp4.html?Key=zZBfUYwiL0_EejDCazQ4nQ&Expires=1602173874").followRedirects(true).ignoreContentType(true).execute().url().toString();
//                        System.out.println(urls);
//                        System.out.println(quality);
//                        movieQualities.add(quality);
//                        movieUrls.add(urls);
//                    }
//                }
//            }

//                String urls,quality;
//                Document doc = Jsoup.connect(strings[0]).get();
//                Elements elements=doc.getElementsByTag("a");
//                for(int i=1;i<elements.size();i=i+2) {
//                    String url=elements.get(i).attr("href");
//                    if(url.contains(".mp4")) {
//                        urls = elements.get(i).attr("href");
//                        Pattern p = Pattern.compile("\\d\\d\\d\\d?p",Pattern.CASE_INSENSITIVE);
//                        Matcher m = p.matcher(urls);
//                        Boolean control = m.find();
//                        if (control)
//                        {
//                            while (control)
//                            {
//                                quality = m.group();
////                        System.out.println(urls);
////                        System.out.println(quality);
//                                movieQualities.add(quality);
//                                urls=Jsoup.connect(urls).followRedirects(true).ignoreContentType(true).execute().url().toString();
//                                movieUrls.add(urls);
//                                break;
//                            }
//                        }
//                        else
//                        {
//                            movieQualities.add("Low Quality");
//                            urls=Jsoup.connect(urls).followRedirects(true).ignoreContentType(true).execute().url().toString();
//                            movieUrls.add(urls);
//                        }
//                    }
//                }
////        for(String q : qualityList)
////            System.out.println(q);
////        for(String u : urlsList)
////            System.out.println(u);

            Document doc = Jsoup.connect(strings[0])
                    .get();

            Elements elements=doc.getElementsByClass("TableBody ForceDownload");
            Elements elements2=elements.get(0).getElementsByTag("a");
            for(int i=0;i<elements2.size();i=i+2) {
                String urls=elements2.get(i).attr("href");
                Pattern p = Pattern.compile("\\d\\d\\d\\d?p",Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(urls);
                Boolean control = m.find();
                if (control)
                {
                    while (control)
                    {
                        String quality = m.group();
//                System.out.println(urls);
//                System.out.println(quality);
                        urls=Jsoup.connect(urls).followRedirects(true).ignoreContentType(true).execute().url().toString();
                        movieUrls.add(urls);
                        movieQualities.add(quality);
                        break;
                    }
                }
                else
                {
                    urls=Jsoup.connect(urls).followRedirects(true).ignoreContentType(true).execute().url().toString();
                    movieQualities.add("Low Quality");
                    movieUrls.add(urls);
                }


            }



        }catch (Exception e){

        }

        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        xModels = new ArrayList<>();
        if (!movieUrls.isEmpty()){
            for(int i=0;i<movieUrls.size();i++){
                XModel xModel = new XModel();
                xModel.setUrl(movieUrls.get(i));
                xModel.setQuality(movieQualities.get(i));
                Log.e("Link",movieUrls.get(i));
                Log.e("Quality",movieQualities.get(i));
                xModels.add(xModel);
            }
            SourceGetter.onComplete.onTaskCompleted(xModels, true);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }

}
