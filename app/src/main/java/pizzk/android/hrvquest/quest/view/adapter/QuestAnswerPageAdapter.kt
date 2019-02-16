package pizzk.android.hrvquest.quest.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.context.AppContext
import pizzk.android.hrvquest.common.extend.cast
import pizzk.android.hrvquest.common.view.adapter.UIViewPageAdapter
import pizzk.android.hrvquest.quest.entity.AnswerEntity
import pizzk.android.hrvquest.quest.entity.QuestChoiceEntity
import pizzk.android.hrvquest.quest.entity.QuestEntity
import pizzk.android.hrvquest.quest.view.widget.SingleChoiceView

/**
 * 问卷答题ViewPager适配器
 */
class QuestAnswerPageAdapter : UIViewPageAdapter<QuestEntity>() {
    private var onResult: (Int, AnswerEntity?) -> Unit = { _, _ -> }

    override fun onCreateView(container: ViewGroup, position: Int): View {
        val e: QuestEntity = getList()[position]
        val layoutId: Int = when (e.type) {
            QuestEntity.TYPE_SINGLE_CHOICE -> R.layout.quest_single_choice
            else -> R.layout.quest_unknow_type
        }
        return LayoutInflater.from(container.context).inflate(layoutId, null)
    }

    override fun onInitView(view: View, position: Int) {
        val e: QuestEntity = getList()[position]
        when (e.type) {
            QuestEntity.TYPE_SINGLE_CHOICE -> initSingleChoice(view, e, position)
            else -> {
            }
        }
    }

    private fun initSingleChoice(view: View, e: QuestEntity, position: Int) {
        val vChoice: SingleChoiceView = view.cast() ?: return
        val commitTextId: Int = if (position == getList().size - 1) R.string.finish else R.string.next_step
        vChoice.setValue(AppContext.getString(commitTextId), e) { entity, index ->
            val choices: List<QuestChoiceEntity> = entity.textChoices ?: emptyList()
            if (index < 0 || choices.size <= index) return@setValue onResult(position, null)
            val obj: String = entity.objectId ?: ""
            val value: String = choices[index].value.toString()
            onResult(position, AnswerEntity(obj, value))
        }
    }

    fun setOnItemResult(block: (Int, AnswerEntity?) -> Unit) {
        onResult = block
    }
}