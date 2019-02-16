package pizzk.android.hrvquest.quest.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.view.activity.ListViewActivity
import pizzk.android.hrvquest.common.view.adapter.UIListViewAdapter
import pizzk.android.hrvquest.quest.entity.AnswerEntity
import pizzk.android.hrvquest.quest.view.adapter.AnswerResultAdapter
import kotlin.collections.ArrayList

/**
 * 问卷答题结果界面
 */
class AnswerResultActivity : ListViewActivity<String>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getToolBar().title = getString(R.string.quest_answer_result)
        getToolBar().setNavigationOnClickListener { finish() }
        //准别数据
        val classify: String = intent.getStringExtra(TAG_ANSWERS_CLASSIFY)
        val answers: ArrayList<AnswerEntity> = intent.getParcelableArrayListExtra(TAG_ANSWERS)
        val score: Int = answers.map { it.value.toIntOrNull() ?: 0 }.reduce { acc, lst -> acc + lst }
        val contents: List<String> = answers.map { "${it.objectId}:${it.value}" }
        val list: MutableList<String> = ArrayList(contents.size + 1)
        list.add("$classify--分数：$score")
        list.addAll(contents)
        //更新显示
        adapter.removeAll()
        adapter.append(list)
        adapter.notifyDataSetChanged()
    }

    override fun onBuildAdapter(): UIListViewAdapter<String> = AnswerResultAdapter(this)

    companion object {
        private const val TAG_ANSWERS = "quest_answers"
        private const val TAG_ANSWERS_CLASSIFY = "quest_answers_classify"

        fun show(activity: Activity, classify: String, answer: ArrayList<AnswerEntity>) {
            val intent = Intent(activity, AnswerResultActivity::class.java)
            intent.putExtra(TAG_ANSWERS_CLASSIFY, classify)
            intent.putParcelableArrayListExtra(TAG_ANSWERS, answer)
            activity.startActivity(intent)
        }
    }
}