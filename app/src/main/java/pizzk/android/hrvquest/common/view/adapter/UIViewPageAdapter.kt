package pizzk.android.hrvquest.common.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import pizzk.android.hrvquest.common.extend.cast
import java.util.*

/**
 * ViewPager适配器
 */
abstract class UIViewPageAdapter<T> : PagerAdapter() {
    //视图复用
    private val views: LinkedList<View> = LinkedList()
    //数据集合
    private val data: MutableList<T> = ArrayList()

    //创建视图回调
    abstract fun onCreateView(container: ViewGroup, position: Int): View

    //视图复用条件
    open fun recyclable(view: View, position: Int): Boolean = true

    //初始化(数据、事件等)
    abstract fun onInitView(view: View, position: Int)

    fun append(d: List<T>?, clean: Boolean = false) {
        if (clean) data.clear()
        if (null == d || d.isEmpty()) return
        data.addAll(d)
    }

    fun remove(index: Int): Boolean {
        if (index !in 0..data.size) return false
        data.removeAt(index)
        return true
    }

    fun removeAll() {
        data.clear()
    }

    fun getList(): List<T> = data

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = data.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val recycle: View? = views.firstOrNull { recyclable(it, position) }
        recycle?.let { views.remove(it) }
        val view: View = recycle ?: onCreateView(container, position)
        container.addView(view)
        onInitView(view, position)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view: View = `object`.cast() ?: return
        container.removeView(view)
        views.addLast(view)
    }
}