package pizzk.android.hrvquest.index.adapter

import android.content.Context
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.view.adapter.UIListViewAdapter
import pizzk.android.hrvquest.common.view.adapter.UIListViewHolder
import pizzk.android.hrvquest.index.entity.MenuAction

/**
 * 首页适配器
 */
class IndexMenuAdapter(context: Context) : UIListViewAdapter<MenuAction>(context) {

    override fun getLayoutId(viewType: Int): Int = R.layout.index_menu_list_item

    override fun onBindViewHolder(holder: UIListViewHolder, position: Int) {
        val action: MenuAction = getList()[position]
        holder.setText(R.id.tv, action.value)
    }

}