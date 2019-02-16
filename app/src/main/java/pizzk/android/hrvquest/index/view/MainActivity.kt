package pizzk.android.hrvquest.index.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.view.activity.ListViewActivity
import pizzk.android.hrvquest.common.view.adapter.UIListViewAdapter
import pizzk.android.hrvquest.common.vm.VMTaskManager
import pizzk.android.hrvquest.common.vm.ViewModel
import pizzk.android.hrvquest.index.adapter.IndexMenuAdapter
import pizzk.android.hrvquest.index.entity.MenuAction
import pizzk.android.hrvquest.quest.entity.QuestParams
import pizzk.android.hrvquest.quest.view.QuestAnswerActivity
import pizzk.android.hrvquest.quest.vm.QuestViewModel

class MainActivity : ListViewActivity<MenuAction>() {
    private var vm: QuestViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolBar()
        //内容
        adapter.removeAll()
        adapter.append(MenuAction.getActions())
        adapter.notifyDataSetChanged()
        //初始请求数据
        initViewModels()
    }

    override fun initViewModels(): List<ViewModel> {
        //创建清理ViewModel
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
                getHintView().showToast(content = getString(R.string.clean_cache_finished))
            }
        }
        vm = questVm
        return listOf(questVm)
    }

    override fun initToolBar() {
        super.initToolBar()
        getToolBar().title = getString(R.string.app_name)
        getToolBar().navigationIcon = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.index_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id: Int = item?.itemId ?: -1
        when (id) {
            //清理本地缓存
            R.id.action_clean_cache -> VMTaskManager.call(vm, QuestParams(action = QuestParams.ACTION_CLEAN))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBuildAdapter(): UIListViewAdapter<MenuAction> {
        val adapter = IndexMenuAdapter(baseContext)
        adapter.setTapBlock { _, index ->
            val action: MenuAction = adapter.getList()[index]
            QuestAnswerActivity.show(activity = this, classify = action.key)
        }
        return adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        vm = null
    }
}