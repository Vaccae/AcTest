package dem.vac.actest

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import dem.vac.actest.DownloadHelper.StaticFun.OnDownloadListener
import java.io.File


/**
 * 作者：Vaccae
 * 邮箱：3657447@qq.com
 * 创建时间：2019-12-09 11:34
 * 功能模块说明：
 */
class AppViewAdapter(context: Context, appinfos: List<CAppInfo>, statusChange: OnItemStatusChange) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mAppinfos: List<CAppInfo> = appinfos
    private var mContext: Context = context
    private var mStatusLister: OnItemStatusChange = statusChange


    class AppViewHolder(context: Context, view: View) : RecyclerView.ViewHolder(view) {
        var tvAppName: TextView = view.findViewById(R.id.tvAppName)
        var tvPackageName: TextView = view.findViewById(R.id.tvPackageName)
        var progressButton: ProgressButton = view.findViewById(R.id.progressbtn)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View = LayoutInflater.from(mContext).inflate(R.layout.rcl_item, parent, false)
        return AppViewHolder(mContext, v)
    }

    override fun getItemCount(): Int {
        return mAppinfos.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = holder as AppViewHolder
        var item = mAppinfos[position]
        //检测当前程序状态
        item.CheckfileStaus(mContext)

        viewHolder.tvAppName.text = item.AppName
        viewHolder.tvPackageName.text = item.PackageName
        when (item.Status) {
            0 -> {
                viewHolder.progressButton.setText("下载")
                viewHolder.progressButton.setTextColor(Color.WHITE)
            }
            1 -> {
                viewHolder.progressButton.setText("安装")
                viewHolder.progressButton.setTextColor(Color.WHITE)
            }
            2 -> {
                viewHolder.progressButton.setText("已安装")
                viewHolder.progressButton.setTextColor(Color.WHITE)
            }
            else -> {
                viewHolder.progressButton.setText("重试")
                viewHolder.progressButton.setTextColor(Color.WHITE)
            }

        }

        viewHolder.progressButton.setOnProgressButtonClickListener(object :
            ProgressButton.OnProgressButtonClickListener {
            override fun onClickListener() {
                when (item.Status) {
                    0 -> {
                        DownloadHelper.downloadasync(
                            item.DownloadUrl,
                            item.FileName,
                            object :
                                OnDownloadListener {
                                override fun onStart() {
                                    viewHolder.progressButton.setProgress(0)
                                    mStatusLister.onRefreshAll()
                                }

                                override fun onSuccess(file: File) {
                                    viewHolder.progressButton.setText("安装")
                                    item.Status = 1
                                    InstallApp(item)
                                    mStatusLister.onRefreshAll()
                                }

                                override fun onFail(file: File, failInfo: String) {
                                    viewHolder.progressButton.setText("重试")
                                    item.Status = 0
                                    mStatusLister.onRefreshAll()
                                }

                                override fun onProgress(progress: Int) {
                                    viewHolder.progressButton.setProgress(progress)
                                    viewHolder.progressButton.setText("下载中 $progress%")
                                }
                            })
                    }
                    1 -> {
                        InstallApp(item)
                    }
                }

            }

        })

    }

    //安装
    private fun InstallApp(item: CAppInfo) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            uri = FileProvider.getUriForFile(
                mContext,
                mContext.packageName + ".provider",
                File(item.FileName)
            )
        } else {
            uri = Uri.fromFile(File(item.FileName))
        }

        intent.setDataAndType(
            uri,
            "application/vnd.android.package-archive"
        )
        startActivity(mContext, intent, null)
    }
}

interface OnItemStatusChange {
    fun onRefreshAll()
}
