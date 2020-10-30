package com.zbiyikli.sourcegetter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException


class MainActivity : AppCompatActivity() {

    var urlText:EditText?=null
    var urlButton:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlText=findViewById(R.id.urlText)
        urlButton=findViewById(R.id.urlBtn)

        urlButton!!.setOnClickListener {
            var url=urlText!!.text.toString()
            if (url.equals("")){
                urlText!!.error="Necessarly"
            }else{
                goPlayer(url)
            }
        }

        try {
            YoutubeDL.getInstance().init(getApplication());
            val streamInfo =YoutubeDL.getInstance().getInfo("https://vimeo.com/22439234")
            println(streamInfo.title)
            println(streamInfo.url)
        } catch (e: YoutubeDLException) {
            Log.e("Zeki", "failed to initialize youtubedl-android", e)
        }


    }

    fun goPlayer(url:String){
//        initXPlayer(url)
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("url",url)
        startActivity(intent)

    }

    fun btn_bitly(view: View){
        goPlayer("")
    }
    fun btn_bittube(view: View){
        goPlayer("https://bittube.video/videos/embed/8e02be96-fa09-4309-a469-3c74c945182b")
    }
    fun btn_blogger(view: View){
        goPlayer("")
    }
    fun btn_clipwatching(view: View){
        goPlayer("https://clipwatching.com/qts479e0dgq4/Manchester_United_1-6_Tottenham.mp4.html")
    }
    fun btn_cloudb(view: View){
        goPlayer("http://cloudb.me/embed-ro14ewb64rif.html")
    }
    fun btn_cloudvideo(view: View){
        goPlayer("https://cloudvideo.tv/embed-6ofaor62msls.html")
    }
    fun btn_comedyshow(view: View){
        goPlayer("https://play.comedyshow.to/embedplay/ffd68b7497daa0ca15026a2fe3237257")
    }
    fun btn_cocoscope(view: View){
        goPlayer("https://www.cocoscope.com/watch?v=57072")
    }
    fun btn_dailymotion(view: View){
        goPlayer("https://www.dailymotion.com/embed/video/x3giopp")
    }
    fun btn_delivembed(view: View){
        goPlayer("https://api1571795485.delivembed.cc/embed/kp/1045457")
    }
    fun btn_feelem(view: View){
        goPlayer("https://feelem.org/95240")
    }
    fun btn_filerio(view: View){
        goPlayer("https://filerio.in/9sufkwnytiwp")
    }
    fun btn_filmmodu(view: View){
        goPlayer("https://www.filmmodu.org/hvitur-hvitur-dagur-altyazili-izle")
    }
    fun btn_gcloud(view: View){
        goPlayer("https://gcloud.live/v/7r3dgbgn0w7l67y")
    }
    fun btn_imdb(view: View){
        goPlayer("https://www.imdb.com/videoplayer/vi2750790937?ref_=embed")
    }
    fun btn_m3u8_mp4(view: View){
        goPlayer("")
    }
    fun btn_mixdrop(view: View){
        goPlayer("https://mixdrop.co/e/4dmyw551bmi")
    }
    fun btn_movcloud(view: View){
        goPlayer("https://movcloud.net/embed/sv-6H4ObIaqQ")
    }
    fun btn_mstream(view: View){
        goPlayer("https://mstream.xyz/otx38euaqx75")
    }
    fun btn_mycima(view: View){
        goPlayer("https://mycima.io/%d9%85%d8%b4%d8%a7%d9%87%d8%af%d8%a9-%d9%81%d9%8a%d9%84%d9%85-this-was-america-2020-%d9%85%d8%aa%d8%b1%d8%ac%d9%85/")
    }
    fun btn_mymailru(view: View){
        goPlayer("https://my.mail.ru/video/embed/9134206772033094269")
    }
    fun btn_okru(view: View){
        goPlayer("https://m.ok.ru/video/2192920939091")
    }
    fun btn_popcornflix(view: View){
        goPlayer("https://www.popcornflix.com/watch/channel/new-releases/movie/30-w2baat4ywdv1-grand-isle")
    }
    fun btn_puhutv(view: View){
        goPlayer("https://puhutv.com/insaat-izle-22270")
    }
    fun btn_rapidrame(view: View){
        goPlayer("https://rapidrame.com/pt6dgx580lkv.html")
    }
    fun btn_shahd(view: View){
        goPlayer("https://shahd.online/96445?content=1")
    }
    fun btn_shahidlive(view: View){
        goPlayer("https://shahidlive.co/Play/1177516-728-408.9887640449438")
    }
    fun btn_speedvideo(view: View){
        goPlayer("")
    }
    fun btn_streamhoe(view: View){
        goPlayer("https://streamhoe.online/v/m0z8ra54zrpk28z")
    }
    fun btn_streamtape(view: View){
        goPlayer("https://streamtape.net/v/GbmzAG9ZaVHlzK/%5BAsahi%5D_Fugou_Keiji_-_Balance_-_UNLIMITED_-_01_%5B1080p%5D.mp4")
    }
    fun btn_streamvid(view: View){
        goPlayer("https://streamvid.co/player/nJa8zwoJB73cSLV/")
    }
    fun btn_streamwire(view: View){
        goPlayer("")
    }
    fun btn_supervideo(view: View){
        goPlayer("https://supervideo.tv/e/xxdoezc1shb7")
    }
    fun btn_thereto(view: View){
        goPlayer("https://there.to/v/npmy2t2-q7n1j66")
    }
    fun btn_thevidebee(view: View){
        goPlayer("http://thevideobee.to/embed-cup20llhnany.html")
    }
    fun btn_tivi5mondeplus(view: View){
        goPlayer("https://www.tivi5mondeplus.com/danslatoile/episode-20")
    }
    fun btn_tubitv(view: View){
        goPlayer("https://tubitv.com/movies/506704/rio_lobo?start=true")
    }
    fun btn_twitter(view: View){
        goPlayer("")
    }
    fun btn_upstream(view: View){
        goPlayer("https://upstream.to/embed-6utvu0rd7mis.html")
    }
    fun btn_uptostream(view: View){
        goPlayer("https://uptostream.com/iframe/ie1a1143ijaa")
    }
    fun btn_uqload(view: View){
        goPlayer("https://uqload.com/embed-r9i2sro7trsr.html?Key=0_iu3Qzj5JPZK-medjJjHg&Expires=1591289110")
    }
    fun btn_vfilmesonline(view: View){
        goPlayer("https://vfilmesonline.net/v/plrn4um421y344w/")
    }
    fun btn_vidcloud9(view: View){
        goPlayer("https://vidcloud9.com/load.php?id=MzI3NzU1")
    }
    fun btn_videobin(view: View){
        goPlayer("https://videobin.co/embed-4tzce1sbqj64.html")
    }
    fun btn_vidfast(view: View){
        goPlayer("https://vidfast.co/embed-2t98rcgo3hbw.html")
    }
    fun btn_vidia(view: View){
        goPlayer("https://www.vidia.tv/embed-xjowg4qgrosn.html?Key=eYVQQFla6MYMJ2_ftae4ZA&Expires=1591289258")
    }
    fun btn_vidlox(view: View){
        goPlayer("https://vidlox.me/embed-3lbr9r4nfbag.html")
    }
    fun btn_vidmoly(view: View){
        goPlayer("https://vidmoly.to/embed-l1dy6podbaaz.html")
    }
    fun btn_vidoza(view: View){
        goPlayer("https://vidoza.net/embed-k6u2ytv8bvmu-720x410.html")
    }
    fun btn_vidshare(view: View){
        goPlayer("https://vidshare.tv/embed-dx8u64xt53hv.html?Key=IychfYLk8RMww5-nuCFS_g&Expires=1591289284")
    }
    fun btn_vidtobo(view: View){
        goPlayer("https://vidtobo.com/qhcm9gipwgbl.html")
    }
    fun btn_vlare(view: View){
        goPlayer("https://vlare.tv/v/V0egtPxz")
    }
    fun btn_voidboost(view: View){
        goPlayer("https://voidboost.net/embed/1261612")
    }
    fun btn_vudeo(view: View){
        goPlayer("https://vudeo.net/azhfxfpzq6yq.html")
    }
    fun btn_watchvideo(view: View){
        goPlayer("https://watchvideo.us/306wmjp8spay.html")
    }
    fun btn_openload(view:View){
        goPlayer("")
    }
    fun btn_fileru(view:View){
        goPlayer("")
    }
    fun btn_sendvid(view:View){
        goPlayer("")
    }
    fun btn_rapidvideo(view:View){
        goPlayer("")
    }
    fun btn_gphoto(view:View){
        goPlayer("")
    }
    fun btn_mediafire(view:View){
        goPlayer("")
    }
    fun btn_youtube(view:View){
        goPlayer("https://www.youtube.com/watch?v=LXb3EKWsInQ")
    }
    fun btn_vk(view:View){
        goPlayer("")
    }

}