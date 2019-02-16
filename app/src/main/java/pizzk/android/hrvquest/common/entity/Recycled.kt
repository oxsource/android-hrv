package pizzk.android.hrvquest.common.entity

import pizzk.android.hrvquest.common.extend.cast
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * 可回收对象基类(减少子类实现复用池的复杂度)
 */
abstract class Recycled(
    private var recycled: Boolean = false,
    val maxPoolSize: Int = 5
) {

    /**回收对象*/
    open fun recycle() {
        if (isRecycled()) return
        recycled = true
        val pool: MutableList<Recycled> =
            getPool(javaClass)
        if (pool.size >= maxPoolSize) return
        pool.add(this)
    }

    /**判断是否回收*/
    open fun isRecycled() = recycled

    companion object {
        private val pools: MutableMap<String, MutableList<Recycled>> = LinkedHashMap()

        /**
         * 获取回收池
         */
        fun <T : Recycled> getPool(clazz: Class<T>): MutableList<Recycled> {
            val key: String = clazz.name
            if (pools.containsKey(key)) return pools[key]!!
            val pool: MutableList<Recycled> = LinkedList()
            pools[key] = pool
            return pool
        }

        /**
         * 获取回收池中的对象，不存在则新建
         */
        fun <T : Recycled> obtain(clazz: Class<T>): T {
            val pool: MutableList<Recycled> =
                getPool(clazz)
            val element: Recycled = pool.firstOrNull { it.javaClass == clazz } ?: clazz.newInstance()
            element.recycled = false
            return element.cast<T>()!!
        }
    }
}