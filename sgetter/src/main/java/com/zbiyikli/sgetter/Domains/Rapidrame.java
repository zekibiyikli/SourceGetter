package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rapidrame extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";

    static ArrayList<String> urls;
    static ArrayList<String> qualities;
    static Document doc;

    @Override
    protected Void doInBackground(String... strings) {

        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        Document doc;
        try {
            /*
            String videoUrl=strings[0];
            String[] split=videoUrl.split("\\+\\+\\+");
            String server=split[0];
            String referer=split[1];

            URL url = new URL(server);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Referer", referer);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();

            List<String> allMatches = new ArrayList<String>();
            Matcher m = Pattern.compile("https.*v.mp4")
                    .matcher(content.toString());
            while (m.find()) {
                allMatches.add(m.group());
            }

            String master=allMatches.get(0).split("\\}")[0];
            String resultMaster=master.substring(0, master.length()-1);

            URL url2 = new URL(resultMaster);
            HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
            con2.setRequestMethod("GET");
            BufferedReader in2 = new BufferedReader(new InputStreamReader(con2.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine2;
            StringBuffer content2 = new StringBuffer();
            while ((inputLine2 = in2.readLine()) != null) {
                content2.append(inputLine2);
                content2.append("\n");
            }
            in2.close();

             */
            /*
            String url=strings[0];
            url=url.replace("embed-", "");

            doc = Jsoup.connect(url)
                    .get();

            String[] split1=doc.html().split("sources: ");
            String[] split2=split1[1].split("\\],");
            String resultJson=split2[0]+"]";
            resultJson=resultJson.replace("file", "\"file\"").replace("label", "\"label\"");

            JSONArray sources=new JSONArray(resultJson);
            for(int i=0;i<sources.length();i++) {
                JSONObject object=new JSONObject(sources.get(i).toString());
                String file=object.getString("file").toString();
                if(file.contains("master.m3u8")) {
                    getRequest(file);
                }else {
                    String label=object.getString("label").toString();
                    System.out.println(file);
                    System.out.println(label);
                }
            }

            //linkParse(content2.toString());
            */
            doc = Jsoup.connect(strings[0]).get();
            Elements script = doc.select("#container > script:nth-child(6)");
            String script_value = String.valueOf(script);
            Pattern p = Pattern.compile("urlset/master.m3u8");
            Matcher m = p.matcher(script_value);
            m.find();
            int start = m.start();
            String firstproccess = script_value.substring(0,start) + "urlset/master.m3u8";
            String m3u8 = firstproccess.replace("<script type=\"text/javascript\">  jwplayer(\"vplayer\").setup({","").replace("sources: [{file:\"","");
            m3u8 = m3u8.trim();
            getRequest(m3u8);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
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
        super.onPostExecute(aVoid);
    }

    public static void getRequest(String baseurl){
        try {
            URL url = new URL(baseurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();
            System.out.println(content.toString());
            linkParse(content.toString());
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
    }

    public static void linkParse(String text) {
        String[] t= text.split("\n");
        for(int i=1;i<t.length-1;i=i+2) {
            if(!t[i].equals("")) {
                String[] split1=t[i].split("RESOLUTION=");
                if(split1.length>1) {
                    String[] split2=split1[1].split(",FRAME-RATE");
                    urls.add(t[i+1]);
                    qualities.add(split2[0]);
                }
            }
        }
    }

}
