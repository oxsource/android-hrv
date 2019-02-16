package pizzk.android.hrvquest.common.http

import pizzk.android.hrvquest.common.entity.Recycled
import java.util.*

/**
 * Http请求对象
 */
class Request(
    //基本要素
    var url: String = "",
    val headers: MutableMap<String, String> = HashMap(),
    val params: MutableMap<String, String> = HashMap(),
    var connectTimeout: Int = TMO_CONNECT,
    var readTimeout: Int = TMO_READ
) : Recycled() {

    override fun recycle() {
        super.recycle()
        url = ""
        headers.clear()
        params.clear()
        connectTimeout = TMO_CONNECT
        readTimeout = TMO_READ
    }

    companion object {
        //默认超时
        const val TMO_CONNECT = 5000
        const val TMO_READ = 5000
    }
}