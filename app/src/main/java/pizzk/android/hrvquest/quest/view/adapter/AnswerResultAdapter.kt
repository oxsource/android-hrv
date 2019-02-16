package pizzk.android.hrvquest.quest.view.adapter

import android.content.Context
import android.view.View
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.view.adapter.UIListViewAdapter
import pizzk.android.hrvquest.common.view.adapter.UIListViewHolder

class AnswerResultAdapter(context: Context) : UIListViewAdapter<String>(context) {

    override fun getLayoutId(viewType: Int): Int = R.layout.answer_result_list_item

    override fun onBindViewHolder(holder: UIListViewHolder, position: Int) {
        if (0 == position) {
            holder.setVisibility(R.id.tvTitle, View.VISIBLE)
            holder.setVisibility(R.id.tvContent, View.GONE)
            holder.setVisibility(R.id.vLine, View.GONE)
            holder.setText(R.id.tvTitle, getList()[position])
        } else {
            holder.setVisibility(R.id.tvTitle, View.GONE)
            holder.setVisibility(R.id.tvContent, View.VISIBLE)
            holder.setVisibility(R.id.vLine, View.VISIBLE)
            holder.setText(R.id.tvContent, getList()[position])
        }
    }
}