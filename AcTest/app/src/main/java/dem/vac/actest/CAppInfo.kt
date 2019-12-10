package dem.vac.actest

import android.content.Context
import java.io.File

/**
 * 作者：Vaccae
 * 邮箱：3657447@qq.com
 * 创建时间：2019-12-06 15:32
 * 功能模块说明：
 */
class CAppInfo{
    //程序名称
    lateinit var AppName: String

    //程序包名
    lateinit var PackageName: String

    //下载文件名称
    lateinit var FileName: String

    //下载地址
    lateinit var DownloadUrl: String

    //状态 0-下载  1-已下载  2-已安装
    var Status: Int = 0


    fun CheckfileStaus(context: Context) {
        Status = 0
        if (DownloadUrl == "" || FileName == "") {
            return
        }
        if (CheckAppInstall.isAppInstalled(context, PackageName)) {
            Status = 2
        } else {
            var file = File(FileName)
            //存在的话说明已下载
            if (file.exists()) {
                Status = 1
            } else {
                Status = 0
            }
        }
    }
}