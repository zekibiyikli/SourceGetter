package com.zbiyikli.sgetter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.zbiyikli.sgetter.Domains.Bitly;
import com.zbiyikli.sgetter.Domains.Bittube;
import com.zbiyikli.sgetter.Domains.Blogger;
import com.zbiyikli.sgetter.Domains.ClipWatching;
import com.zbiyikli.sgetter.Domains.CloudVideo;
import com.zbiyikli.sgetter.Domains.Cloudb;
import com.zbiyikli.sgetter.Domains.Cocoscope;
import com.zbiyikli.sgetter.Domains.ComedyShow;
import com.zbiyikli.sgetter.Domains.Delivembed;
import com.zbiyikli.sgetter.Domains.Feelem;
import com.zbiyikli.sgetter.Domains.Filerio;
import com.zbiyikli.sgetter.Domains.FilmModu;
import com.zbiyikli.sgetter.Domains.Fansubs;
import com.zbiyikli.sgetter.Domains.GCloud;
import com.zbiyikli.sgetter.Domains.Imdb;
import com.zbiyikli.sgetter.Domains.M3u8;
import com.zbiyikli.sgetter.Domains.MixDrop;
import com.zbiyikli.sgetter.Domains.Movcloud;
import com.zbiyikli.sgetter.Domains.MyMailRu;
import com.zbiyikli.sgetter.Domains.Mycima;
import com.zbiyikli.sgetter.Domains.Okru;
import com.zbiyikli.sgetter.Domains.Openplay;
import com.zbiyikli.sgetter.Domains.PopcornFlix;
import com.zbiyikli.sgetter.Domains.ProStream;
import com.zbiyikli.sgetter.Domains.Puhutv;
import com.zbiyikli.sgetter.Domains.Rapidrame;
import com.zbiyikli.sgetter.Domains.Shahd;
import com.zbiyikli.sgetter.Domains.Shahidlive;
import com.zbiyikli.sgetter.Domains.Solidfile;
import com.zbiyikli.sgetter.Domains.SpeedVideo;
import com.zbiyikli.sgetter.Domains.Streamhoe;
import com.zbiyikli.sgetter.Domains.Streamtape;
import com.zbiyikli.sgetter.Domains.Streamwire;
import com.zbiyikli.sgetter.Domains.Streamz;
import com.zbiyikli.sgetter.Domains.SuperVideo;
import com.zbiyikli.sgetter.Domains.Tazvids;
import com.zbiyikli.sgetter.Domains.TheVideoBee;
import com.zbiyikli.sgetter.Domains.There;
import com.zbiyikli.sgetter.Domains.Tivi5mondeplus;
import com.zbiyikli.sgetter.Domains.UpToStream;
import com.zbiyikli.sgetter.Domains.Upstream;
import com.zbiyikli.sgetter.Domains.Uqload;
import com.zbiyikli.sgetter.Domains.VidCloud;
import com.zbiyikli.sgetter.Domains.VidMoly;
import com.zbiyikli.sgetter.Domains.VidShare;
import com.zbiyikli.sgetter.Domains.VidTodo;
import com.zbiyikli.sgetter.Domains.Vidcloud9;
import com.zbiyikli.sgetter.Domains.VideoBin;
import com.zbiyikli.sgetter.Domains.Vidfast;
import com.zbiyikli.sgetter.Domains.Vidia;
import com.zbiyikli.sgetter.Domains.Vidlox;
import com.zbiyikli.sgetter.Domains.Vidoza;
import com.zbiyikli.sgetter.Domains.Vidoza2;
import com.zbiyikli.sgetter.Domains.Vlare;
import com.zbiyikli.sgetter.Domains.VoidBoost;
import com.zbiyikli.sgetter.Domains.Vudeo;
import com.zbiyikli.sgetter.Domains.WatchVideo;
import com.zbiyikli.sgetter.Domains.tubiTv;
import com.zbiyikli.sgetter.Domains.vFilmesOnline;
import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.Domains.Twitter;
import com.zbiyikli.sgetter.Domains.DailyMotion;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import static com.zbiyikli.sgetter.Utils.FacebookUtils.check_fb_video;
import static com.zbiyikli.sgetter.Utils.FacebookUtils.getFbLink;
import static com.zbiyikli.sgetter.Utils.GPhotosUtils.getGPhotoLink;
import static com.zbiyikli.sgetter.Utils.OpenloadUtils.getKey1;
import static com.zbiyikli.sgetter.Utils.OpenloadUtils.getKey2;
import static com.zbiyikli.sgetter.Utils.OpenloadUtils.getLongEncrypt;
import static com.zbiyikli.sgetter.Utils.OpenloadUtils.getLongEncrypt2;
import static com.zbiyikli.sgetter.Utils.Utils.base64Decode;
import static com.zbiyikli.sgetter.Utils.Utils.base64Encode;
import static com.zbiyikli.sgetter.Utils.Utils.getDomainFromURL;
import static com.zbiyikli.sgetter.Utils.Utils.putModel;
import static com.zbiyikli.sgetter.Utils.Utils.sortMe;

/*
 *      sourceGetter
 *         By
 *   Zeki Bıyıklı
 *
 */

public class SourceGetter {
    private WebView webView;
    public static Context context;
    public static OnTaskCompleted onComplete;
    Handler handler = null;
    int count = 0;
    public static final String agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36";
    private final String openload = "https?:\\/\\/(www\\.)?(openload|oload)\\.[^\\/,^\\.]{2,}\\/(embed|f)\\/.+";
    private final String filerio = "https?:\\/\\/(www\\.)?(filerio)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String sendvid = "https?:\\/\\/(www\\.)?(sendvid)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String rapidvideo = "https?:\\/\\/(www\\.)?rapidvideo\\.[^\\/,^\\.]{2,}\\/(\\?v=[^&\\?]*|e\\/.+|v\\/.+|d\\/.+)";
    private final String gphoto = "https?:\\/\\/(photos.google.com)\\/(u)?\\/?(\\d)?\\/?(share)\\/.+(key=).+";
    private final String mediafire = "https?:\\/\\/(www\\.)?(mediafire)\\.[^\\/,^\\.]{2,}\\/(file)\\/.+";
    private final String vk = "https?:\\/\\/(www\\.)?vk\\.[^\\/,^\\.]{2,}\\/video\\-.+";
    private final String twitter = "http(?:s)?:\\/\\/(?:www\\.)?twitter\\.com\\/([a-zA-Z0-9_]+)";
    private final String youtube = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
    private final String vidoza = "https?:\\/\\/(www\\.)?(vidoza)\\.[^\\/,^\\.]{2,}.+";
    private final String uptostream = "https?:\\/\\/(www\\.)?(uptostream|uptobox)\\.[^\\/,^\\.]{2,}.+";
    private final String fansubs = "https?:\\/\\/(www\\.)?(fansubs\\.tv)\\/(v|watch)\\/.+";

    public SourceGetter(Context view) {
        this.context = view;
    }

    private void init() {
        webView = new WebView(context);
        webView.setWillNotDraw(true);
        webView.addJavascriptInterface(new xJavascriptInterface(), "xGetter");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        AndroidNetworking.initialize(context);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getJavaScript(view);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }
        });

        getJavaScript(webView);
    }

    class xJavascriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void fuck(final String url) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ArrayList<XModel> xModels = new ArrayList<>();
                    putModel(url,"",xModels);
                    onComplete.onTaskCompleted(xModels,false);
                }
            });
        }
    }

    public boolean find(String url) throws InterruptedException {
        Log.e("URL",url);

        boolean fb = false;
        boolean run = false;
        boolean cloudvideo=false;
        boolean vidCloud=false;
        boolean proStream=false;
        boolean myMailRu=false;
        boolean vidtodo=false;
        boolean vidMoly=false;
        boolean bitly=false;
        boolean streamz=false;
        boolean filmmodu=false;
        boolean tazvids=false;
        boolean openplay=false;
        boolean thevideobee=false;
        boolean vidshare=false;
        boolean supervideo=false;
        boolean vfilmesonline=false;
        boolean delivembed=false;
        boolean supervidos=false;
        boolean speedvideo=false;
        boolean mokru=false;
        boolean m3u8=false;
        boolean voidboost=false;
        boolean vidfast=false;
        boolean uptobox=false;
        boolean mixdrop=false;
        boolean vidcloud9=false;
        boolean vidnode=false;
        boolean mstream=false;
        boolean uqload=false;
        boolean upstream=false;
        boolean videobin=false;
        boolean streamwire=false;
        boolean cloudb=false;
        boolean vidia=false;
        boolean there=false;
        boolean shahd=false;
        boolean comedyshow=false;
        boolean feelem=false;
        boolean tivi5mondeplus=false;
        boolean blogger=false;
        boolean gcloud=false;
        boolean vidlox=false;
        boolean streamhoe=false;
        boolean rapidrame=false;
        boolean clipwatching=false;
        boolean watchvideo=false;
        boolean puhutv=false;
        boolean imdb=false;
        boolean shahidlive=false;
        boolean movcloud=false;
        boolean mycima=false;
        boolean tubitv=false;
        boolean vidoza2=false;
        boolean mfire = false;
        boolean oload = false;
        boolean isOkRu = false;
        boolean isVk=false;
        boolean isRapidVideo=false;
        boolean tw=false;
        boolean yt=false;
        boolean isvidoza=false;
        boolean isuptostream=false;
        boolean isFanSubs=false;
        boolean isSendVid = false;
        boolean isFileRio=false;
        boolean isDailyMotion=false;
        boolean popcornflix=false;
        boolean vlare=false;
        boolean bittube=false;
        boolean streamtape=false;
        boolean vudeo=false;
        boolean cocoscope=false;
        boolean filerio=false;
        boolean fansubs=false;
        boolean solidfile=false;

        if (check(openload, url)) {
            run = true;
            oload = true;
        }
        else if(url.contains("filmmodu")){
            filmmodu=true;
            run=true;
        }
        else if(url.contains("streamtape")){
            streamtape=true;
            run=true;
        }
        else if(url.contains(".m3u8") || (url.contains(".mp4")) || (url.contains(".mkv"))){
            m3u8=true;
            run=true;
        }
        else if (check(sendvid, url)) {
            run = true;
            isSendVid = true;
        }
        else if (check(rapidvideo, url)) {
            run = true;
            isRapidVideo=true;
            if (url.contains("/e/") || url.contains("/v/")){
                url = url.replace("/e/","/d/");
            }
        }
        else if (check(gphoto, url)) {
            run = true;
        }
        else if (check_fb_video(url)) {
            run = true;
            fb = true;
        }
        else if (check(mediafire, url)) {
            run = true;
            mfire = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }
        }
        else if (check(vk,url)){
            run = true;
            isVk = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }
        }
        else if (check(twitter,url)){
            run = true;
            tw = true;
        }
        else if (check(youtube,url)){
            run = true;
            yt = true;
        }
        else if(url.contains("vidoza")){
            vidoza2=true;
            run=true;
        }
        else if (check(vidoza, url)) {
            isvidoza=true;
            run = true;
        }
        else if(url.contains("uptobox")){
            uptobox=true;
            run=true;
        }
        else if (check(uptostream, url)) {
            isuptostream=true;
            run = true;
        }
        else if (url.contains("dailymotion")){
            isDailyMotion = true;
            run = true;
        }
        else if(url.contains("cloudvideo")){
            cloudvideo=true;
            run=true;
        }
        else if(url.contains("vidcloud.co")){
            vidCloud=true;
            run=false;
        }
        else if(url.contains("prostream")){
            proStream=true;
            run=true;
        }
        else if(url.contains("my.mail.ru")){
            myMailRu=true;
            run=true;
        }
        else if(url.contains("vidtodo") || url.contains("vidtodu") || url.contains("vidto-do") || url.contains("vidtobo")){
            vidtodo=true;
            run=true;
        }
        else if(url.contains("vidmoly")){
            vidMoly=true;
            run=true;
        }
        else if(url.contains("bit.ly")){
            bitly=true;
            run=true;
        }
        else if(url.contains("streamz")){
            streamz=true;
            run=true;
        }
        else if(url.contains("tazvids")){
            tazvids=true;
            run=true;
        }
        else if(url.contains("openplay")){
            openplay=true;
            run=true;
        }
        else if(url.contains("thevideobee")){
            thevideobee=true;
            run=true;
        }
        else if(url.contains("vidshare")){
            vidshare=true;
            run=true;
        }
        else if(url.contains("supervideo")){
            supervideo=true;
            run=true;
        }
        else if(url.contains("vfilmesonline")){
            vfilmesonline=true;
            run=true;
        }
        else if(url.contains("delivembed")){
            delivembed=true;
            run=true;
        }
        else if(url.contains("supervidos")){
            supervidos=true;
            run=true;
        }
        else if(url.contains("speedvideo")){
            speedvideo=true;
            run=true;
        }
        else if(url.contains("m.ok.ru") || url.contains("ok.ru")){
            mokru=true;
            run=true;
        }
        else if(url.contains("voidboost")){
            voidboost=true;
            run=true;
        }
        else if(url.contains("vidfast")){
            vidfast=true;
            run=true;
        }
        else if(url.contains("mixdrop")){
            mixdrop=true;
            run=true;
        }
        else if(url.contains("vidnode")){
            Log.e("URL-2",url);
            vidnode=true;
            run=true;
        }
        else if(url.contains("vidcloud9")){
            vidcloud9=true;
            run=true;
        }
        else if(url.contains("mstream")){
            mstream=true;
            run=true;
        }
        else if(url.contains("mystream.premiumserver.club")){
            url=url.replace("mystream.premiumserver.club","mstream.xyz");
            mstream=true;
            run=true;
        }
        else if(url.contains("embed.mystream.to")){
            url=url.replace("embed.mystream.to","mstream.space");
            mstream=true;
            run=true;
        }
        else if(url.contains("uqload")){
            uqload=true;
            run=true;
        }
        else if(url.contains("upstream")){
            upstream=true;
            run=true;
        }
        else if(url.contains("videobin")){
            videobin=true;
            run=true;
        }else if(url.contains("streamwire")){
            streamwire=true;
            run=true;
        }
        else if(url.contains("cloudb")){
            cloudb=true;
            run=true;
        }
        else if(url.contains("vidia")){
            vidia=true;
            run=true;
        }
        else if(url.contains("there.to")){
            there=true;
            run=true;
        }
        else if(url.contains("shahd")){
            shahd=true;
            run=true;
        }
        else if(url.contains("comedyshow")){
            comedyshow=true;
            run=true;
        }
        else if(url.contains("feelem")){
            feelem=true;
            run=true;
        }
        else if(url.contains("tivi5mondeplus")){
            tivi5mondeplus=true;
            run=true;
        }
        else if(url.contains("blogger")){
            blogger=true;
            run=true;
        }
        else if(url.contains("gcloud")){
            gcloud=true;
            run=true;
        }
        else if(url.contains("vidlox")){
            vidlox=true;
            run=true;
        }
        else if(url.contains("streamhoe")){
            streamhoe=true;
            run=true;
        }
        else if(url.contains("rapidrame")){
            rapidrame=true;
            run=true;
        }
        else if(url.contains("clipwatching")){
            clipwatching=true;
            run=true;
        }
        else if(url.contains("watchvideo.us")){
            watchvideo=true;
            run=true;
        }
        else if(url.contains("puhutv")){
            puhutv=true;
            run=true;
        }
        else if(url.contains("imdb")){
            imdb=true;
            run=true;
        }
        else if(url.contains("shahidlive")){
            shahidlive=true;
            run=true;
        }
        else if(url.contains("movcloud")){
            movcloud=true;
            run=true;
        }
        else if(url.contains("mycima.io")){
            mycima=true;
            run=true;
        }
        else if(url.contains("tubitv.com")){
            tubitv=true;
            run=true;
        }else if(url.contains("popcornflix")){
            popcornflix=true;
            run=true;
        }else if (url.contains("vlare")){
            vlare=true;
            run=true;
        }else if(url.contains("bittube")){
            bittube=true;
            run=true;
        }else if(url.contains("vudeo")){
            vudeo=true;
            run=true;
        }else if(url.contains("cocoscope")){
            cocoscope=true;
            run=true;
        }else if(url.contains("filerio")){
            filerio=true;
            run=true;
        }else if(url.contains("fansubs")){
            fansubs=true;
            run=true;
        }else if(url.contains("solidfile")){
            solidfile=true;
            run=true;
        }

        if (run) {
            if (check(gphoto, url)) {
                gphotoORfb(url, false);
            } else if (fb) {
                gphotoORfb(url, true);
            } else if (mfire) {
                mfire(url);
            } else if (oload) {
                openload(url);
            } else if (isOkRu){
                okru(url);
            } else if (isVk) {
                vk(url);
            } else if (isRapidVideo) {
                rapidVideo(url);
            } else if (tw) {
                twitter(url);
            } else if (yt) {
                youtube(url);
            } else if (isvidoza){
                vidozafiles(url);
            } else if (isuptostream) {
                UpToStream.fetch(context,url,onComplete);
            } else if (isFanSubs) {
                fansubs(url);
            } else if (isSendVid){
                sendvid(url);
            } else if (isFileRio){
                sendvid(url);
            } else if (isDailyMotion){
                dailyMotion(url);
            }else if(cloudvideo){
                cloudVideo(url);
            }else if(vidCloud){
                vidCloud(url);
            }else if(proStream){
                proStream(url);
            }else if(myMailRu){
                myMailRu(url);
            }else if(vidtodo){
                vidToDo(url);
            }else if(vidMoly){
                vidMoly(url);
            }else if(bitly){
                bitLy(url);
            }else if(streamz){
                StreamZ(url);
            }else if(filmmodu){
                filmModu(url);
            }else if(tazvids){
                TazVids(url);
            }else if(openplay){
                Openplay(url);
            }else if(thevideobee){
                TheVideoBee(url);
            }else if(vidshare){
                vidShare(url);
            }else if(supervideo){
                superVideo(url);
            }else if(vfilmesonline){
                vFilmesOnline(url);
            }else if(delivembed){
                deliVembed(url);
            }else if(supervidos){
                SuperVidos(url);
            }else if(speedvideo){
                speedVideo(url);
            }else if(mokru){
                Okru(url);
            }else if(m3u8){
                M3u8(url);
            }else if(voidboost){
                Voidboost(url);
            }else if(vidfast){
                url=url.replace("go.vidfast.co","vidfast.co");
                Vidfast(url);
            }else if(uptobox){
                url=url.replace("uptobox","uptostream");
                UpToStream.fetch(context,url,onComplete);
            }else if(mixdrop){
                MixDrop(url);
            }else if(vidcloud9){
                Vidcloud9(url);
            }else if(vidnode){
                url=url.replace("vidnode.net","vidcloud9.com");
                url=url.split("&")[0];
                Vidcloud9(url);
            }else if(mstream){
                Mstream(url);
            }else if(uqload){
                Uqload(url);
            }else if(upstream){
                UpStream(url);
            }else if(videobin){
                Videobin(url);
            }else if(streamwire){
                Streamwire(url);
            }else if(vidnode){
                url=url.replace("vidnode.net","vidcloud9.com");
                Vidcloud9(url);
            }else if(cloudb){
                Cloudb(url);
            }else if(vidia){
                Vidia(url);
            }else if(there){
                There(url);
            }else if(shahd){
                Shahd(url);
            }else if(comedyshow){
                Comedyshow(url);
            }else if(feelem){
                Feelem(url);
            }else if(tivi5mondeplus){
                Tivi5mondeplus(url);
            }else if(blogger){
                Blogger(url);
            }else if(gcloud){
                GCloud(url);
            }else if(vidlox){
                Vidlox(url);
            }else if(streamhoe){
                Streamhoe(url);
            }else if(rapidrame){
                Rapidrame(url);
            }else if(clipwatching){
                Clipwatching(url);
            }else if(watchvideo){
                Watchvideo(url);
            }else if(puhutv){
                puhutv(url);
            }else if(imdb){
                Imdb(url);
            }else if(shahidlive){
                shahidLive(url);
            }else if(movcloud){
                movCloud(url);
            }else if(mycima){
                myCima(url);
            }else if(tubitv){
                tubiTv(url);
            }else if(vidoza2){
                Vidoza2(url);
            }else if (popcornflix){
                Popcornflix(url);
            }else if(vlare){
                Vlare(url);
            }else if(bittube){
                Bittube(url);
            }else if(streamtape){
                Streamtape(url);
            }else if(vudeo){
                Vudeo(url);
            }else if(cocoscope){
                Cocoscope(url);
            }else if(filerio){
                Filerio(url);
            }else if(fansubs){
                Fansubs(url);
            }else if(solidfile){
                Solidfile(url);
            }
            
        }
        return run;
    }

    private void Solidfile(String url){new Solidfile().execute(url);}

    private void Fansubs(String url){new Fansubs().execute(url);}

    private void Filerio(String url){new Filerio().execute(url);}

    private void Cocoscope(String url){new Cocoscope().execute(url);}

    private void Vudeo(String url){new Vudeo().execute(url);}

    private void Streamtape(String url){new Streamtape().execute(url);}

    private void Bittube(String url){new Bittube().execute(url);}

    private void Vlare(String url){new Vlare().fetch(url,onComplete);}

    private void Popcornflix(String url){new PopcornFlix().execute(url);}

    private void Vidoza2(String url){new Vidoza2().execute(url);}

    private void tubiTv(String url){new tubiTv().execute(url);}

    private void myCima(String url){new Mycima().execute(url);}

    private void movCloud(String url){ new Movcloud().execute(url); }

    private void shahidLive(String url){ new Shahidlive().execute(url); }

    private void Feelem(String url){ new Feelem().execute(url); }

    private void Imdb(String url){ new Imdb().execute(url); }

    private void puhutv(String url){ new Puhutv().execute(url); }

    private void Watchvideo(String url){ new WatchVideo().execute(url); }

    private void Clipwatching(String url){ new ClipWatching().execute(url); }

    private void Rapidrame(String url){ new Rapidrame().execute(url); }

    private void Streamhoe(String url){ new Streamhoe().execute(url); }

    private void Vidlox(String url){ new Vidlox().execute(url); }

    private void GCloud(String url){ new GCloud().execute(url); }

    private void Blogger(String url){ new Blogger().execute(url); }

    private void Tivi5mondeplus(String url){ new Tivi5mondeplus().execute(url); }

    private void Comedyshow(String url){ new ComedyShow().execute(url); }

    private void Shahd(String url){ new Shahd().execute(url); }

    private void There(String url){ new There().execute(url); }

    private void Vidia(String url){ new Vidia().execute(url); }

    private void Cloudb(String url){ new Cloudb().execute(url); }

    private void Streamwire(String url){ new Streamwire().execute(url); }

    private void Videobin(String url){ new VideoBin().execute(url); }

    private void UpStream(String url){ new Upstream().execute(url); }

    private void Uqload(String url){ new Uqload().execute(url); }

    private void Mstream(String url){ //new MStream().execute(url);
         commonFunc(url);
        }

    private void Vidcloud9(String url){ new Vidcloud9().execute(url); }

    private void MixDrop(String url){ new MixDrop().execute(url); }

    private void Vidfast(String url){ new Vidfast().execute(url); }

    private void Voidboost(String url){ new VoidBoost().execute(url); }

    private void M3u8(String url){ new M3u8().execute(url); }

    private void Okru(String url){ new Okru().execute(url); }

    private void speedVideo(String url){ new SpeedVideo().execute(url); }

    private void SuperVidos(String url){ commonFunc(url); }

    private void deliVembed(String url){ new Delivembed().execute(url); }

    private void vFilmesOnline(String url){ new vFilmesOnline().execute(url); }

    private void superVideo(String url){ new SuperVideo().execute(url); }

    private void vidShare(String url){ new VidShare().execute(url); }

    private void TheVideoBee(String url){ new TheVideoBee().execute(url); }

    private void Openplay(String url){ new Openplay().execute(url); }

    private void TazVids(String url){ new Tazvids().execute(url); }

    private void filmModu(String url){ new FilmModu().execute(url); }

    private void StreamZ(String url){ new Streamz().execute(url); }

    private void bitLy(String url){ new Bitly().execute(url); }

    private void twitter(final String url){
        AndroidNetworking.post("https://twdown.net/download.php")
                .addBodyParameter("URL", url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        onComplete.onTaskCompleted(sortMe(Twitter.fetch(response)),true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void youtube(String url){
        if(url.contains("embed")){
            url=url.replace("embed/","watch?v=");
        }
        if (check(youtube,url)) {
            new YouTubeExtractor(context) {
                @Override
                public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                    if (ytFiles != null) {
                        ArrayList<XModel> xModels = new ArrayList<>();

                        for (int i = 0, itag; i < ytFiles.size(); i++) {
                            itag = ytFiles.keyAt(i);
                            YtFile ytFile = ytFiles.get(itag);
                            if (ytFile.getFormat().getExt().equals("mp4") && ytFile.getFormat().getAudioBitrate()!=-1){
                                putModel(ytFile.getUrl(), ytFile.getFormat().getHeight() + "p", xModels);
                            }
                        }

                        onComplete.onTaskCompleted(sortMe(xModels), true);
                    }else {
                        onComplete.onError();
                    }
                }
            }.extract(url, true, false);
        }else onComplete.onError();
    }

    private void mfire(String url) {
        AndroidNetworking.get(url)
                .addHeaders("User-agent", agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        final String regex = "aria-label=\"Download file\"\\n.+href=\"(.*)\"";
                        final Pattern pattern = Pattern.compile(regex);
                        final Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            ArrayList<XModel> xModels = new ArrayList<>();
                            putModel(matcher.group(1),"",xModels);
                            onComplete.onTaskCompleted(xModels,false);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void gphotoORfb(String url, final boolean fb) {
        if (url != null) {
            if (fb){
                AndroidNetworking.post("https://fbdown.net/download.php")
                        .addBodyParameter("URLz", "https://www.facebook.com/video.php?v="+ url)
                        .addHeaders("User-agent", agent)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                ArrayList<XModel> xModels = new ArrayList<>();
                                putModel(getFbLink(response, false),"SD",xModels);
                                putModel(getFbLink(response, true),"HD",xModels);
                                onComplete.onTaskCompleted(xModels,true);
                            }

                            @Override
                            public void onError(ANError anError) {
                                onComplete.onError();
                            }
                        });
            }else {
                AndroidNetworking.get(url)
                        .addHeaders("User-agent", agent)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                ArrayList<XModel> xModels = new ArrayList<>();
                                xModels = getGPhotoLink(response);
                                onComplete.onTaskCompleted(xModels,true);
                            }

                            @Override
                            public void onError(ANError anError) {
                                onComplete.onError();
                            }
                        });
            }
        } else onComplete.onError();
    }

    private boolean check(String regex, String string) {
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(ArrayList<XModel> vidURL,boolean multiple_quality);
        void goBitLY(String url);
        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }

    private void openload(final String url) {
        if (url != null) {
            AndroidNetworking.get(url)
                    .addHeaders("User-agent", agent)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            String longString = getLongEncrypt(response);
                            if (longString==null){
                                longString = getLongEncrypt2(response);
                            }
                            String key1 = getKey1(response);
                            String key2 = getKey2(response);
                            String js = "ZnVuY3Rpb24gZ2V0T3BlbmxvYWRVUkwoZW5jcnlwdFN0cmluZywga2V5MSwga2V5MikgewogICAgdmFyIHN0cmVhbVVybCA9ICIiOwogICAgdmFyIGhleEJ5dGVBcnIgPSBbXTsKICAgIGZvciAodmFyIGkgPSAwOyBpIDwgOSAqIDg7IGkgKz0gOCkgewogICAgICAgIGhleEJ5dGVBcnIucHVzaChwYXJzZUludChlbmNyeXB0U3RyaW5nLnN1YnN0cmluZyhpLCBpICsgOCksIDE2KSk7CiAgICB9CiAgICBlbmNyeXB0U3RyaW5nID0gZW5jcnlwdFN0cmluZy5zdWJzdHJpbmcoOSAqIDgpOwogICAgdmFyIGl0ZXJhdG9yID0gMDsKICAgIGZvciAodmFyIGFyckl0ZXJhdG9yID0gMDsgaXRlcmF0b3IgPCBlbmNyeXB0U3RyaW5nLmxlbmd0aDsgYXJySXRlcmF0b3IrKykgewogICAgICAgIHZhciBtYXhIZXggPSA2NDsKICAgICAgICB2YXIgdmFsdWUgPSAwOwogICAgICAgIHZhciBjdXJySGV4ID0gMjU1OwogICAgICAgIGZvciAodmFyIGJ5dGVJdGVyYXRvciA9IDA7IGN1cnJIZXggPj0gbWF4SGV4OyBieXRlSXRlcmF0b3IgKz0gNikgewogICAgICAgICAgICBpZiAoaXRlcmF0b3IgKyAxID49IGVuY3J5cHRTdHJpbmcubGVuZ3RoKSB7CiAgICAgICAgICAgICAgICBtYXhIZXggPSAweDhGOwogICAgICAgICAgICB9CiAgICAgICAgICAgIGN1cnJIZXggPSBwYXJzZUludChlbmNyeXB0U3RyaW5nLnN1YnN0cmluZyhpdGVyYXRvciwgaXRlcmF0b3IgKyAyKSwgMTYpOwogICAgICAgICAgICB2YWx1ZSArPSAoY3VyckhleCAmIDYzKSA8PCBieXRlSXRlcmF0b3I7CiAgICAgICAgICAgIGl0ZXJhdG9yICs9IDI7CiAgICAgICAgfQogICAgICAgIHZhciBieXRlcyA9IHZhbHVlIF4gaGV4Qnl0ZUFyclthcnJJdGVyYXRvciAlIDldIF4ga2V5MSBeIGtleTI7CiAgICAgICAgdmFyIHVzZWRCeXRlcyA9IG1heEhleCAqIDIgKyAxMjc7CiAgICAgICAgZm9yICh2YXIgaSA9IDA7IGkgPCA0OyBpKyspIHsKICAgICAgICAgICAgdmFyIHVybENoYXIgPSBTdHJpbmcuZnJvbUNoYXJDb2RlKCgoYnl0ZXMgJiB1c2VkQnl0ZXMpID4+IDggKiBpKSAtIDEpOwogICAgICAgICAgICBpZiAodXJsQ2hhciAhPSAiJCIpIHsKICAgICAgICAgICAgICAgIHN0cmVhbVVybCArPSB1cmxDaGFyOwogICAgICAgICAgICB9CiAgICAgICAgICAgIHVzZWRCeXRlcyA9IHVzZWRCeXRlcyA8PCA4OwogICAgICAgIH0KICAgIH0KICAgIC8vY29uc29sZS5sb2coc3RyZWFtVXJsKQogICAgcmV0dXJuIHN0cmVhbVVybDsKfQp2YXIgZW5jcnlwdFN0cmluZyA9ICJIdGV0ekxvbmdTdHJpbmciOwp2YXIga2V5TnVtMSA9ICJIdGV0ektleTEiOwp2YXIga2V5TnVtMiA9ICJIdGV0ektleTIiOwp2YXIga2V5UmVzdWx0MSA9IDA7CnZhciBrZXlSZXN1bHQyID0gMDsKdmFyIG9ob3N0ID0gIkh0ZXR6SG9zdCI7Ci8vY29uc29sZS5sb2coZW5jcnlwdFN0cmluZywga2V5TnVtMSwga2V5TnVtMik7CnRyeSB7CiAgICB2YXIga2V5TnVtMV9PY3QgPSBwYXJzZUludChrZXlOdW0xLm1hdGNoKC9wYXJzZUludFwoJyguKiknLDhcKS8pWzFdLCA4KTsKICAgIHZhciBrZXlOdW0xX1N1YiA9IHBhcnNlSW50KGtleU51bTEubWF0Y2goL1wpXC0oW15cK10qKVwrLylbMV0pOwogICAgdmFyIGtleU51bTFfRGl2ID0gcGFyc2VJbnQoa2V5TnVtMS5tYXRjaCgvXC9cKChbXlwtXSopXC0vKVsxXSk7CiAgICB2YXIga2V5TnVtMV9TdWIyID0gcGFyc2VJbnQoa2V5TnVtMS5tYXRjaCgvXCsweDRcLShbXlwpXSopXCkvKVsxXSk7CiAgICBrZXlSZXN1bHQxID0gKGtleU51bTFfT2N0IC0ga2V5TnVtMV9TdWIgKyA0IC0ga2V5TnVtMV9TdWIyKSAvIChrZXlOdW0xX0RpdiAtIDgpOwogICAgdmFyIGtleU51bTJfT2N0ID0gcGFyc2VJbnQoa2V5TnVtMi5tYXRjaCgvXCgnKFteJ10qKScsLylbMV0sIDgpOwogICAgdmFyIGtleU51bTJfU3ViID0gcGFyc2VJbnQoa2V5TnVtMi5zdWJzdHIoa2V5TnVtMi5pbmRleE9mKCIpLSIpICsgMikpOwogICAga2V5UmVzdWx0MiA9IGtleU51bTJfT2N0IC0ga2V5TnVtMl9TdWI7CiAgICBjb25zb2xlLmxvZyhrZXlOdW0xLCBrZXlOdW0yKTsKfSBjYXRjaCAoZSkgewogICAgLy9jb25zb2xlLmVycm9yKGUuc3RhY2spOwogICAgdGhyb3cgRXJyb3IoIktleSBOdW1iZXJzIG5vdCBwYXJzZWQhIik7Cn0KdmFyIHNyYyA9IG9ob3N0ICsgJy9zdHJlYW0vJyArIGdldE9wZW5sb2FkVVJMKGVuY3J5cHRTdHJpbmcsIGtleVJlc3VsdDEsIGtleVJlc3VsdDIpOwp4R2V0dGVyLmZ1Y2soc3JjKTs=";
                            js = base64Decode(js);
                            js = js.replace("HtetzLongString", longString);
                            js = js.replace("HtetzKey1", key1);
                            js = js.replace("HtetzKey2", key2);
                            js = js.replace("HtetzHost",getDomainFromURL(url));
                            js = base64Encode(js);
                            webView.loadUrl("javascript:(function() {" +
                                    "var parent = document.getElementsByTagName('head').item(0);" +
                                    "var script = document.createElement('script');" +
                                    "script.type = 'text/javascript';" +
                                    // Tell the browser to BASE64-decode the string into your script !!!
                                    "script.innerHTML = window.atob('" + js + "');" +
                                    "parent.appendChild(script)" +
                                    "})()");
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }
    }

    private void okru(String url) {
        if (url != null) {

            AndroidNetworking.get(url)
                    .addHeaders("User-agent", "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19")
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            String json = getJson(response);
                            if (json!=null) {
                                json = StringEscapeUtils.unescapeHtml4(json);
                                try {
                                    json = new JSONObject(json).getJSONObject("flashvars").getString("metadata");
                                    if (json!=null) {
                                        JSONArray jsonArray = new JSONObject(json).getJSONArray("videos");
                                        ArrayList<XModel> models = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String url = jsonArray.getJSONObject(i).getString("url");
                                            String name = jsonArray.getJSONObject(i).getString("name");
                                            if (name.equals("mobile")) {
                                                putModel(url, "144p", models);
                                            } else if (name.equals("lowest")) {
                                                putModel(url, "240p", models);
                                            } else if (name.equals("low")) {
                                                putModel(url, "360p", models);
                                            } else if (name.equals("sd")) {
                                                putModel(url, "480p", models);
                                            } else if (name.equals("hd")) {
                                                putModel(url, "720p", models);
                                            } else if (name.equals("full")) {
                                                putModel(url, "1080p", models);
                                            } else if (name.equals("quad")) {
                                                putModel(url, "2000p", models);
                                            } else if (name.equals("ultra")) {
                                                putModel(url, "4000p", models);
                                            } else {
                                                putModel(url, "Default", models);
                                            }
                                        }
                                        onComplete.onTaskCompleted(sortMe(models), true);
                                    }else {
                                        onComplete.onError();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onComplete.onError();
                                }
                            }else onComplete.onError();
                        }

                        private String getJson(String html){
                            final String regex = "data-options=\"(.*?)\"";
                            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                            final Matcher matcher = pattern.matcher(html);
                            if (matcher.find()) {
                                return matcher.group(1);
                            }
                            return null;
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }
    }

    private void fansubs(final String mUrl){
        AndroidNetworking.get(mUrl)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> models = new ArrayList<>();
                        Document document = Jsoup.parse(response);
                        if (document.html().contains("<source")){
                            Elements element = document.getElementsByTag("source");
                            for (int i=0;i<element.size();i++){
                                Element temp = element.get(i);
                                if (temp.hasAttr("src")) {
                                    String url = temp.attr("src");
                                    putModel(url, temp.attr("label"), models);
                                }
                            }
                        }
                        if (models.size()!=0){
                            onComplete.onTaskCompleted(sortMe(models),true);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.getErrorBody());
                        onComplete.onError();
                    }
                });
    }

    private void vk(String url) {
        if (url != null) {
            AndroidNetworking.get(url)
                    .addHeaders("User-agent", agent)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            String json = get("al_video.php', ?(\\{.*])",response);
                            json = get("\\}, ?(.*)",json);

                            try {
                                ArrayList<XModel> models = new ArrayList<>();
                                String x240="url240",x360="url360",x480="url480",x720="url720",x1080="url1080";
                                JSONObject object = new JSONArray(json).getJSONObject(4).getJSONObject("player").getJSONArray("params").getJSONObject(0);

                                if (object.has(x240)){
                                    putModel(object.getString(x240),"240p",models);
                                }

                                if (object.has(x360)){
                                    putModel(object.getString(x360),"360p",models);
                                }

                                if (object.has(x480)){
                                    putModel(object.getString(x480),"480p",models);
                                }

                                if (object.has(x720)){
                                    putModel(object.getString(x720),"720p",models);
                                }

                                if (object.has(x1080)){
                                    putModel(object.getString(x1080),"1080p",models);
                                }
                                onComplete.onTaskCompleted(sortMe(models),true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                onComplete.onError();
                            }
                        }

                        private String get(String regex,String html){
                            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                            final Matcher matcher = pattern.matcher(html);
                            if (matcher.find()) {
                                return matcher.group(1);
                            }
                            return null;
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }
    }

    private void uptoStream(final String url) {
        if (url != null) {
            AndroidNetworking.get(url)
                    .addHeaders("User-agent", agent)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            String regex = "sources.*?\\.parse.*'(.*?)'";
                            String json = get(regex,response);
                            if (json!=null){
                                try {
                                    JSONArray array = new JSONArray(json);
                                    ArrayList<XModel> xModels = new ArrayList<>();
                                    for (int i=0;i<array.length();i++){
                                        String src = array.getJSONObject(i).getString("src");
                                        String label = array.getJSONObject(i).getString("label");
                                        String lang = array.getJSONObject(i).getString("lang");

                                        if (lang!=null && !lang.isEmpty()){
                                            lang = lang.toUpperCase();
                                        }

                                        String quality=label+","+ lang;
                                        putModel(quality,src,xModels);
                                        putModel(src,quality,xModels);
                                    }

                                    if (xModels.size()!=0) {
                                        onComplete.onTaskCompleted(sortMe(xModels), true);
                                    }else onComplete.onError();
                                } catch (JSONException e) {
                                    onComplete.onError();
                                }
                            }else{
                                commonFunc(url);
                                //onComplete.onError();
                            }
                        }

                        private String get(String regex,String code){
                            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                            final Matcher matcher = pattern.matcher(code);
                            code = null;
                            while (matcher.find()) {
                                for (int i = 1; i <= matcher.groupCount(); i++) {
                                    code = matcher.group(i);
                                }
                            }

                            return code;
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }
    }

    private void rapidVideo(final String mUrl){
        AndroidNetworking.get(mUrl)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = new ArrayList<>();
                        Document document = Jsoup.parse(response);
                        if (document.html().contains("<source")){
                            Elements element = document.getElementsByTag("source");
                            for (int i=0;i<element.size();i++){
                                Element temp = element.get(i);
                                if (temp.hasAttr("src")) {
                                    String url = temp.attr("src");
                                    putModel(url, temp.attr("label"), xModels);
                                }
                            }
                        }else {
                            Elements element = document.getElementsByTag("a");
                            for (int i=0;i<element.size();i++){
                                if (element.get(i).hasAttr("href")) {
                                    String url = element.get(i).attr("href");
                                    if (url.contains(".mp4")) {
                                        String quality = element.get(i).text().replace("Download","").replace(" ","");;
                                        putModel(url, quality, xModels);
                                    }
                                }
                            }
                        }
                        if ( xModels.size()!=0){
                            onComplete.onTaskCompleted(sortMe(xModels),true);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void vidozafiles(final String url){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        onComplete.onTaskCompleted(Vidoza.fetch(response),false);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }


    private void sendvid(String url){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        String src = getSrc(response);
                        if (src!=null){
                            ArrayList<XModel> xModels = new ArrayList<>();
                            putModel(src,"Normal",xModels);
                            onComplete.onTaskCompleted(xModels,false);
                        }else onComplete.onError();
                    }

                    private String getSrc(String response){
                        final String regex = "<source ?src=\"(.*?)\"";
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(response);

                        if (matcher.find()) {
                            return matcher.group(1);
                        }
                        return null;
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void dailyMotion(String url){
        new DailyMotion().execute(url);
    }

    private void cloudVideo(String url){
        new CloudVideo().execute(url);
    }

    private void vidCloud(String url){
        new VidCloud().execute(url);
    }

    private void proStream(String url){
        new ProStream().execute(url);
    }

    private void myMailRu(final String url){new MyMailRu().execute(url); }

    private void vidMoly(final String url){new VidMoly().execute(url); }

    private void vidToDo(final String url){new VidTodo().execute(url);
    }

    private void getJavaScript(WebView view) {
        byte[] bytes = Base64.decode("aHR0cHM6Ly9yYXcuZ2l0aGFjay5jb20vS2h1bkh0ZXR6TmFpbmcvRmlsZXMvbWFzdGVyL3hnZXR0ZXIuanM=".getBytes(),Base64.DEFAULT);
        view.loadUrl("javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var script = document.createElement('script');" +
                "script.type = 'text/javascript';" +
                "script.src = '"+new String(bytes)+"';"+
                "parent.appendChild(script)" +
                "})()");
    }

    private void commonFunc(String url){
        Log.d("HTML-",url);
        webView = new WebView(context);
        handler = new Handler();
        webView.setWebChromeClient(new WebChromeClient ());

        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webViewSettings.setMediaPlaybackRequiresUserGesture(true);
        webViewSettings.setDomStorageEnabled(true);
        webViewSettings.setDatabaseEnabled(true);
        webViewSettings.setMinimumFontSize(1);
        webViewSettings.setMinimumLogicalFontSize(1);
        webViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webViewSettings.setUseWideViewPort(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setSaveFormData(true);
        webView.getSettings().setAppCachePath(context.getFilesDir().getAbsolutePath() + "/cache");
        webView.getSettings().setDatabasePath(context.getFilesDir().getAbsolutePath() + "/databases");

        webView.getSettings().setAppCacheEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        Log.d("HTML-","asdasdasdsadasd");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
            }


        });
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                findSource();
            }
        });

        webView.loadUrl(url);

    }

    private void findSource() {
        webView.loadUrl("javascript:window.HTMLOUT.showHTML" +
                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML(String html) {
            Log.d("msg", "loadFINISH");

            findUrl(html);
            handler = new Handler();
            handler.post(new Runnable() {

                public void run() {
                    long delta = 100;
                    long downTime = SystemClock.uptimeMillis();
                    float x = webView.getLeft() + (webView.getWidth() / 2);
                    float y = webView.getTop() + (webView.getHeight() / 2);

                    MotionEvent tapDownEvent = MotionEvent.obtain(downTime, downTime + delta, MotionEvent.ACTION_DOWN, x, y, 0);
                    tapDownEvent.setSource(InputDevice.SOURCE_CLASS_POINTER);
                    MotionEvent tapUpEvent = MotionEvent.obtain(downTime, downTime + delta + 2, MotionEvent.ACTION_UP, x, y, 0);
                    tapUpEvent.setSource(InputDevice.SOURCE_CLASS_POINTER);

                    webView.dispatchTouchEvent(tapDownEvent);
                    webView.dispatchTouchEvent(tapUpEvent);

                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:window.HTMLOUT.showHTML2" +
                                    "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        }
                    });

                    if (count % 10 == 0){

                    }

                    handler.postDelayed(this, 500);
                }
            });

        }


        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML2(String html) {
            findUrl(html);

        }

        private void findUrl(String html){
            String regexVideoTag = "<video(.*?)</video>";
            String regexUrl = "src=\"[^\"]+?\\.(mp4|m3u8|m3u)\"";

            Pattern patternVideoTag = Pattern.compile(regexVideoTag, Pattern.DOTALL);
            Matcher matcherVideoTag = patternVideoTag.matcher(html);
            String resultUrl="";
            while (matcherVideoTag.find()) {
                Log.d("HTML--" , matcherVideoTag.group());
                Pattern pattern = Pattern.compile(regexUrl);
                Matcher matcher = pattern.matcher(matcherVideoTag.group());
                while (matcher.find()) {
                    Log.d("HTML---", matcher.group());
                    resultUrl=matcher.group().toString().replace("src=\"","").replace("\"","");

                    handler.removeCallbacksAndMessages(null);
                }
            }
            SourceGetter.onComplete.goBitLY(resultUrl);
        }

    }
}