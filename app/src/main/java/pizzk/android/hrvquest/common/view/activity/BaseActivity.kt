package pizzk.android.hrvquest.common.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.view.widget.HintView
import pizzk.android.hrvquest.common.view.widget.HintViewImpl
import pizzk.android.hrvquest.common.vm.VMTaskManager
import pizzk.android.hrvquest.common.vm.ViewModel
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    private val vms: MutableList<ViewModel> = LinkedList()
    private val hView: HintView by lazy { HintViewImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vmList: List<ViewModel> = initViewModels()
        vms.addAll(vmList)
    }

    protected open fun initViewModels(): List<ViewModel> = emptyList()

    //获取提示视图接口
    fun getHintView(): HintView = hView

    protected abstract fun getToolBar(): Toolbar

    protected open fun initToolBar() {
        setSupportActionBar(getToolBar())
        getToolBar().setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite))
    }

    override fun onResume() {
        super.onResume()
        getHintView().onResume()
    }

    override fun onPause() {
        super.onPause()
        getHintView().onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        //Activity销毁时自动释放取消Action
        vms.forEach { vm -> VMTaskManager.cancel(vm.getName()) }
        vms.clear()
    }
}