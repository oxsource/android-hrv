package pizzk.android.hrvquest.common.view.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.view.adapter.UIListViewAdapter

/**
 *列表Activity
 */
abstract class ListViewActivity<T> : BaseActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    //适配器
    val adapter: UIListViewAdapter<T> by lazy { onBuildAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        initRecyclerView(recyclerView)
        recyclerView.adapter = adapter
        initToolBar()
    }

    protected open fun initRecyclerView(view: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    abstract fun onBuildAdapter(): UIListViewAdapter<T>

    protected fun getRecyclerView() = recyclerView

    override fun getToolBar() = toolbar
}