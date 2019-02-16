package pizzk.android.hrvquest.common.http

import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.context.AppContext
import pizzk.android.hrvquest.common.entity.Recycled
import pizzk.android.hrvquest.common.extend.json
import java.nio.charset.Charset

/**
 * Http返回对象
 */
class Response(
    //基本要素
    var headers: MutableMap<String, List<String>>? = null,
    var code: Int = 0,
    var error: String? = null,
    var bytes: ByteArray? = null
) : Recycled() {

    fun successful(): Boolean = code == 200

    fun error(): String = if (error?.length ?: 0 > 0) error!! else AppContext.getString(R.string.http_request_error)

    /**
     * 获取内容字节
     */
    fun bytes(): ByteArray = this.bytes ?: ByteArray(size = 0)

    /**
     * 获取内容文本
     */
    fun text(): String {
        val bytes: ByteArray = this.bytes ?: return ""
        return String(bytes, Charset.forName(HttpClient.UTF8))
    }

    /**
     * 解析获取内容json对象
     */
    inline fun <reified T> json(recycle: Boolean = true): T {
        try {
            if (!successful()) throw IllegalStateException(error())
            return text().json()
        } finally {
            if (recycle) recycle()
        }
    }

    override fun recycle() {
        super.recycle()
        headers = null
        code = 500
        error = ""
        bytes = null
    }
}