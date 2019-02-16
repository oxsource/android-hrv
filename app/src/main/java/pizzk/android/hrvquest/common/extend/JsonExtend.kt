package pizzk.android.hrvquest.common.extend

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference

/**
 * Json解析及序列化功能扩展
 */
fun Any.jsonTextIf(defaultValue: String = ""): String {
    return try {
        JSONObject.toJSONString(this)
    } catch (e: Exception) {
        defaultValue
    }
}

fun <T> String.json(clazz: Class<T>): T = JSONObject.parseObject(this, clazz)

inline fun <reified T> String.json(): T = JSONObject.parseObject(this, object : TypeReference<T>() {})

@Suppress("UNCHECKED_CAST")
fun <T> Any?.cast(): T? {
    this ?: return null
    return this as? T
}