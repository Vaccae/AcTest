package dem.vac.actest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    //程序列表
    lateinit var mListAppinfo: MutableList<CAppInfo>

    //RecyclerView设置
    lateinit var rcl_item: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var AppAdapter: AppViewAdapter

    //安装包路径
    val filepath: String =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + "SUM" + File.separator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InitList()


        rcl_item = findViewById(R.id.rcl_app)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        AppAdapter = AppViewAdapter(this, mListAppinfo, object : OnItemStatusChange {
            override fun onRefreshAll() {
                AppAdapter.notifyDataSetChanged()
            }
        })
        rcl_item.layoutManager = layoutManager
        rcl_item.adapter = AppAdapter


    }

    private fun InitList() {
        mListAppinfo = ArrayList()

        //加入TTS讯飞语音合成
        var itemInfo = CAppInfo()
        itemInfo.AppName = "讯飞语音合成"
        itemInfo.DownloadUrl = "http://www.sumsoft.cn/apk/TTSChina.apk"
        itemInfo.FileName = filepath + "TTSChina.apk"
        itemInfo.PackageName = "com.iflytek.tts"
        itemInfo.CheckfileStaus(this)
        mListAppinfo.add(itemInfo)

        //加入讯飞输入法
        itemInfo = CAppInfo()
        itemInfo.AppName = "讯飞输入法"
        itemInfo.DownloadUrl = "http://www.sumsoft.cn/apk/ifly.apk"
        itemInfo.FileName = filepath + "ifly.apk"
        itemInfo.PackageName = "com.iflytek.inputmethod"
        itemInfo.CheckfileStaus(this)
        mListAppinfo.add(itemInfo)

        //加入商盘通
        itemInfo = CAppInfo()
        itemInfo.AppName = "商盘通"
        itemInfo.DownloadUrl = "http://www.sumsoft.cn/apk/sumbizscan50.apk"
        itemInfo.FileName = filepath + "sumbizscan.apk"
        itemInfo.PackageName = "sum.biz.sumscan"
        itemInfo.CheckfileStaus(this)
        mListAppinfo.add(itemInfo)

        //加入慧溯通
        itemInfo = CAppInfo()
        itemInfo.AppName = "慧溯通"
        itemInfo.DownloadUrl = "http://www.sumsoft.cn/apk/sumindscan27.apk"
        itemInfo.FileName = filepath + "sumindscan.apk"
        itemInfo.PackageName = "sum.ind.sumscan"
        itemInfo.CheckfileStaus(this)
        mListAppinfo.add(itemInfo)

    }
}
