package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class MixDrop extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        Document doc;
        try {
            doc = Jsoup.connect(strings[0])
                    .get();

//            String[] split1=doc.html().split("return p");
//            String[] split2=split1[1].split("\"");
//            String[] split3=split1[1].split("'");
//            String[] baseSplit=split3[3].split("\\|");
//
//            String baseMid="";
//            if(split2[5].contains("-g")){
//                baseMid="-"+baseSplit[16];
//                url="https://"+baseSplit[2]+"-"+baseSplit[3]+"."+baseSplit[6]+"."+baseSplit[7]+"/"+baseSplit[9]+"/"+baseSplit[4]+"."+baseSplit[5]+"?"+baseSplit[2]+"="+baseMid+"&e="+baseSplit[15];
//            }else if(split2[5].contains("g-h")){
//                baseMid=baseSplit[16]+"-"+baseSplit[17];
//                url="https://"+baseSplit[2]+"-"+baseSplit[3]+"."+baseSplit[6]+"."+baseSplit[7]+"/"+baseSplit[9]+"/"+baseSplit[4]+"."+baseSplit[5]+"?"+baseSplit[2]+"="+baseMid+"&e="+baseSplit[15];
//            }else{
//                if(baseSplit[15].equals("v")) {
//                    url="https://a-"+baseSplit[2]+"."+baseSplit[4]+"."+baseSplit[6]+"/"+baseSplit[15]+"/"+baseSplit[3]+"."+baseSplit[5]+"?"+baseSplit[8]+"="+baseSplit[17]+"&e="+baseSplit[16];
//                }else{
//                    url="https://"+baseSplit[2]+"-"+baseSplit[3]+"."+baseSplit[6]+"."+baseSplit[7]+"/"+baseSplit[9]+"/"+baseSplit[4]+"."+baseSplit[5]+"?"+baseSplit[2]+"="+baseSplit[16]+"&e="+baseSplit[15];
//                }
//            }
            doc = Jsoup.connect(strings[0])
                    .get();
            String packed=doc.html().split("\"\\};")[1].split("</script>")[0].trim();
            JsUnpacker jsUnpacker = new JsUnpacker(packed);

            url="https:"+jsUnpacker.unpack().split("MDCore.wurl=\"")[1].split("\";")[0];
            System.out.println(url);


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
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
