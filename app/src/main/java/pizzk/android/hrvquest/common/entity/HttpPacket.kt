package pizzk.android.hrvquest.common.entity

/**
 * HRV请求返回结果
 */

data class HttpPacket<T>(
    var resultCode: Int = -1,
    var reason: String? = null,
    var resultBody: T? = null,
    var error: Boolean? = null,
    var msg: String? = null,
    var count: Int? = null
) {

    fun successful() = 200 == resultCode

    fun result(): Payload {
        val result: Payload = Payload.obtain()
        result.success = successful()
        result.value = resultBody
        result.msg = (if (result.success) msg else reason) ?: ""
        return result
    }
}