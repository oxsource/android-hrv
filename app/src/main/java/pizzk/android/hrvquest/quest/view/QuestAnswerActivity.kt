package pizzk.android.hrvquest.quest.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.extend.cast
import pizzk.android.hrvquest.common.view.activity.ViewPagerActivity
import pizzk.android.hrvquest.common.view.adapter.UIViewPageAdapter
import pizzk.android.hrvquest.common.vm.VMTaskManager
import pizzk.android.hrvquest.common.vm.ViewModel
import pizzk.android.hrvquest.quest.entity.AnswerEntity
import pizzk.android.hrvquest.quest.entity.QuestEntity
import pizzk.android.hrvquest.quest.entity.QuestParams
import pizzk.android.hrvquest.quest.view.adapter.QuestAnswerPageAdapter
import pizzk.android.hrvquest.quest.vm.QuestViewModel
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList

/**
 * 问卷答题界面
 */
class QuestAnswerActivity : ViewPagerActivity<QuestEntity>() {
    private val answers: ArrayList<AnswerEntity> = ArrayList()
    private var vm: QuestViewModel? = null
    private var questTitle: String = ""
    //page监听器
    private val pageChangeListener: ViewPager.OnPageChangeListener by lazy {
        object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) = updateTitle(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewPager().forbidScroll()
        getViewPager().addOnPageChangeListener(pageChangeListener)
        val classify: String = intent.getStringExtra(TAG_CLASSIFY)
        questTitle = QuestEntity.classifyExplain(classify)
        initToolBar()
        //初始请求数据
        initViewModels()
        VMTaskManager.call(vm, QuestParams(classify = classify))
    }

    override fun initToolBar() {
        super.initToolBar()
        getToolBar().setNavigationOnClickListener { askCancel() }
        updateTitle()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.quest_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id: Int = item?.itemId ?: -1
        when (id) {
            R.id.action_cancel -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //询问是否取消
    private fun askCancel() = getHintView().showAlert(
        title = getString(R.string.hint),
        content = getString(R.string.whether_continue_answer),
        positiveText = getString(R.string.continuation),
        negativeText = getString(R.string.cancel),
        negativeClick = this::finish
    )

    override fun initViewModels(): List<ViewModel> {
        //创建查询ViewModel
        val questVm = QuestViewModel {
            onStart {
                getHintView().showLoading()
            }
            onResult { result ->
                getHintView().hideLoading()
                if (isFinishing) return@onResult
                result ?: return@onResult
                if (!result.success) {
                    getHintView().showToast(content = result.msg)
                    return@onResult
                }
                val quests: List<QuestEntity> = result.value.cast() ?: emptyList()
                if (quests.isEmpty()) {
                    getHintView().showToast(content = getString(R.string.fetch_empty_data))
                    return@onResult
                }
                adapter.removeAll()
                adapter.append(quests)
                adapter.notifyDataSetChanged()
                updateTitle()
            }
        }
        vm = questVm
        return listOf(questVm)
    }

    //更新标题
    private fun updateTitle(index: Int = 0) {
        val progress: String = if (adapter.count > 0 && index >= 0) "(${index + 1}/${adapter.count})" else ""
        getToolBar().title = "$questTitle$progress"
    }

    override fun onBuildAdapter(): UIViewPageAdapter<QuestEntity> {
        val adapter = QuestAnswerPageAdapter()
        adapter.setOnItemResult { index, answer ->
            if (null == answer) {
                getHintView().showToast(content = getString(R.string.please_solve_this))
                return@setOnItemResult
            }
            answers.add(answer)
            if (index == adapter.count - 1) {
                //完成问卷
                AnswerResultActivity.show(this, questTitle, answers)
                finish()
            } else {
                //进行下一步
                getViewPager().currentItem = index + 1
            }
        }
        return adapter
    }

    override fun onBackPressed() = askCancel()

    override fun onDestroy() {
        super.onDestroy()
        getViewPager().removeOnPageChangeListener(pageChangeListener)
        vm = null
    }

    companion object {
        private const val TAG_CLASSIFY = "quest_classify"

        fun show(activity: Activity, classify: String) {
            val intent = Intent(activity, QuestAnswerActivity::class.java)
            intent.putExtra(TAG_CLASSIFY, classify)
            activity.startActivity(intent)
        }
    }
}