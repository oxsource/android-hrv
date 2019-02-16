package pizzk.android.hrvquest.common.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class UIListViewAdapter<T>(val context: Context) : RecyclerView.Adapter<UIListViewHolder>() {
    private val data: MutableList<T> = ArrayList()
    protected abstract fun getLayoutId(viewType: Int): Int
    //holder,index
    private var tapNormal: (UIListViewHolder, Int) -> Unit = { _, _ -> }
    //holder,index
    private var tapLong: (UIListViewHolder, Int) -> Unit = { _, _ -> }
    //holder,index,what
    protected open var tapChild: (UIListViewHolder, View, Int, Int) -> Unit = { _, _, _, _ -> }
    //EditText event
    private var childChanged: (type: Int, s: Any) -> Unit = { _, _ -> }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UIListViewHolder {
        val lyId: Int = getLayoutId(viewType)
        val view: View = LayoutInflater.from(context).inflate(lyId, parent, false)
        val holder = UIListViewHolder(view)
        view.setOnClickListener {
            val index: Int = holder.adapterPosition
            if (index in 0..getList().size) {
                tapNormal(holder, index)
            }
        }
        view.setOnLongClickListener {
            val index: Int = holder.adapterPosition
            if (index in 0..getList().size) {
                tapLong(holder, index)
            }
            return@setOnLongClickListener true
        }
        return holder
    }

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

    override fun getItemCount(): Int = data.size

    fun getList(): MutableList<T> = data

    fun setTapBlock(tap: (holder: UIListViewHolder, index: Int) -> Unit) {
        tapNormal = tap
    }

    fun setTapLongBlock(tap: (holder: UIListViewHolder, index: Int) -> Unit) {
        tapLong = tap
    }

    fun setTapChildBlock(tap: (holder: UIListViewHolder, view: View, index: Int, what: Int) -> Unit) {
        tapChild = tap
    }

    fun setChildChanged(block: (type: Int, value: Any) -> Unit) {
        childChanged = block
    }

    protected fun onChildChanged(type: Int, value: Any) {
        childChanged(type, value)
    }


    companion object {
        const val WHAT0: Int = 100
        const val WHAT1: Int = 101
        const val WHAT2: Int = 102
    }
}