package pizzk.android.hrvquest.common.view.adapter

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import pizzk.android.hrvquest.common.extend.cast

@Suppress("UNCHECKED_CAST")
class UIListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val views: SparseArray<View> = SparseArray()

    fun <T : View> getView(@IdRes id: Int): T? {
        var view: View? = views.get(id)
        if (null == view) {
            view = itemView.findViewById<View>(id)?.apply { views.put(id, this) }
        }
        return view as? T
    }

    fun setMarginTop(@DimenRes size: Int) {
        val mp: ViewGroup.MarginLayoutParams = itemView.layoutParams.cast() ?: return
        mp.topMargin = itemView.context.resources.getDimensionPixelSize(size)
    }

    fun setMarginBottom(@DimenRes size: Int) {
        val mp: ViewGroup.MarginLayoutParams = itemView.layoutParams.cast() ?: return
        mp.bottomMargin = itemView.context.resources.getDimensionPixelSize(size)
    }

    fun setHeight(@IdRes id: Int, @DimenRes size: Int) {
        val view: View = getView(id) ?: return
        val mp: ViewGroup.LayoutParams = view.layoutParams.cast() ?: return
        mp.height = itemView.context.resources.getDimensionPixelSize(size)
    }

    fun setText(@IdRes id: Int, text: CharSequence?) {
        if (null == text) return
        val view: View = getView(id) ?: return
        when (view) {
            is TextView -> view.text = text
            is EditText -> view.setText(text)
            is Button -> view.text = text
        }
    }

    fun setBackgroundResource(@IdRes id: Int, @DrawableRes color: Int) {
        val view: View = getView(id) ?: return
        view.setBackgroundResource(color)
    }

    fun setTextColor(@IdRes id: Int, color: Int) {
        val view: View = getView(id) ?: return
        when (view) {
            is TextView -> view.setTextColor(color)
            is EditText -> view.setTextColor(color)
            is Button -> view.setTextColor(color)
        }
    }

    fun setHint(@IdRes id: Int, text: CharSequence?) {
        if (null == text) return
        val view: View = getView(id) ?: return
        when (view) {
            is EditText -> view.hint = text
        }
    }

    fun setClickListen(@IdRes id: Int, block: (View) -> Unit) {
        val view: View = getView(id) ?: return
        view.setOnClickListener(block)
    }

    fun setVisibility(@IdRes id: Int, v: Int) {
        val view: View = getView(id) ?: return
        view.visibility = v
    }
}