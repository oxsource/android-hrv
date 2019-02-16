package pizzk.android.hrvquest.quest.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.view.adapter.UIListViewAdapter
import pizzk.android.hrvquest.common.view.adapter.UIListViewHolder
import pizzk.android.hrvquest.quest.entity.QuestChoiceEntity
import pizzk.android.hrvquest.quest.entity.QuestEntity

/**
 * 单选视图控件
 */
class SingleChoiceView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val adapter: ChoiceAdapter = ChoiceAdapter(context!!)
    private val tvTitle: TextView
    private val button: Button
    //视图
    private var entity: QuestEntity? = null
    private var listener: (QuestEntity, Int) -> Unit = { _, _ -> }

    init {
        View.inflate(context, R.layout.widget_single_choice, this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        tvTitle = findViewById(R.id.tvTitle)
        button = findViewById(R.id.bt)
        button.setOnClickListener {
            val e: QuestEntity = entity ?: return@setOnClickListener
            listener(e, adapter.getCheckIndex())
        }
    }

    fun setValue(commit: String, e: QuestEntity, block: (QuestEntity, Int) -> Unit) {
        entity = e
        listener = block
        tvTitle.text = e.title
        button.text = commit
        //更新选项
        adapter.setValue(e.textChoices)
    }

    class ChoiceAdapter(context: Context) : UIListViewAdapter<QuestChoiceEntity>(context) {
        private var checkIndex: Int = -1
        private var lockFlag: Boolean = false

        override fun getLayoutId(viewType: Int): Int = R.layout.widget_single_choice_item

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: UIListViewHolder, position: Int) {
            val e: QuestChoiceEntity = getList()[position]
            val text = "${position + 1}、${e.text}"
            holder.setText(R.id.tv, text)
            val checkBox: CheckBox = holder.getView(R.id.checkbox) ?: return
            checkBox.setOnCheckedChangeListener { _, checked ->
                checkIndex = if (checked) position else -1
                if (!lockFlag) notifyDataSetChanged()
            }
            lockFlag = true
            checkBox.isChecked = position == checkIndex
            lockFlag = false
        }

        fun setValue(choices: List<QuestChoiceEntity>?) {
            removeAll()
            append(choices)
            checkIndex = -1
            notifyDataSetChanged()
        }

        fun getCheckIndex(): Int = checkIndex
    }
}