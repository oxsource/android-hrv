package pizzk.android.hrvquest.common.view.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.view.adapter.UIViewPageAdapter
import pizzk.android.hrvquest.common.view.widget.UIViewPager

abstract class ViewPagerActivity<T> : BaseActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var viewPager: UIViewPager
    val adapter: UIViewPageAdapter<T> by lazy { onBuildAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)
        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = adapter
        initToolBar()
    }

    override fun getToolBar(): Toolbar = toolbar

    fun getViewPager(): UIViewPager = viewPager

    abstract fun onBuildAdapter(): UIViewPageAdapter<T>
}