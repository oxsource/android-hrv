package pizzk.android.hrvquest.common.http

import pizzk.android.hrvquest.common.entity.Recycled
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*

/**
 * 基于HttpURLConnection封装的Http请求客户端
 * 相关文档：https://developer.android.google.cn/reference/java/net/HttpURLConnection
 */
class HttpClient private constructor(
    private val host: String,
    private val interceptors: List<Interceptor>
) {

    /**
     * HttpClient构架器
     */
    class Builder {
        private var host: String = ""
        private val interceptors: MutableList<Interceptor> = ArrayList(5)

        fun setHost(host: String): Builder {
            this.host = host
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            interceptors.add(interceptor)
            return this
        }

        fun build(): HttpClient {
            return HttpClient(host, interceptors)
        }
    }

    /**
     * 获取最终URL请求地址
     */
    private fun getHttpUrl(request: Request): String {
        //参数拼接
        var tail: String = request.params.map {
            String.format("%s=%s", it.key, URLEncoder.encode(it.value, UTF8))
        }.joinToString(separator = "&")
        tail = if (tail.isEmpty()) tail else "?$tail"
        return (if (request.url.startsWith(prefix = "http")) "" else host) + request.url + tail
    }

    fun get(request: Request): Response {
        var conn: HttpURLConnection? = null
        val response: Response = Recycled.obtain(Response::class.java)
        //拦截器请求处理
        for (interceptor: Interceptor in interceptors) {
            interceptor.onRequest(request)
        }
        try {
            //创建连接
            val url: String = getHttpUrl(request)
            conn = URL(url).openConnection() as HttpURLConnection
            val connection: HttpURLConnection = conn
            connection.requestMethod = "GET"
            connection.useCaches = false
            //头部设置
            connection.addRequestProperty("Charset", UTF8)
            for ((key: String, value: String) in request.headers) {
                connection.setRequestProperty(key, value)
            }
            //超时设置
            connection.connectTimeout = request.connectTimeout
            connection.readTimeout = request.readTimeout
            //开始请求
            connection.connect()
            response.headers = connection.headerFields
            if (connection.errorStream != null) {
                response.error = String(connection.errorStream.readBytes(), Charset.forName(UTF8))
            }
            response.code = connection.responseCode
            if (response.successful() && connection.inputStream != null) {
                response.bytes = connection.inputStream.readBytes()
            }
        } catch (e: Exception) {
            response.error = e.message.toString()
        } finally {
            request.recycle()
            conn?.disconnect()
        }
        //拦截器返回结果处理
        for (interceptor: Interceptor in interceptors) {
            interceptor.onResponse(response)
        }
        return response
    }

    companion object {
        const val UTF8 = "UTF-8"
    }
}