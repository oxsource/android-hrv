package pizzk.android.hrvquest.common.entity

/**
 * 通用负载参数类
 */
data class Payload(
    //是否为过程量
    var process: Boolean = false,
    var success: Boolean = false,
    var msg: String = "",
    var value: Any? = null
) : Recycled(maxPoolSize = 10) {

    companion object {

        fun obtain(
            process: Boolean = false, success: Boolean = false,
            msg: String = "", value: Any? = null
        ): Payload {
            val payload: Payload =
                obtain(Payload::class.java)
            payload.process = process
            payload.success = success
            payload.msg = msg
            payload.value = value
            return payload
        }
    }
}