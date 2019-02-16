package pizzk.android.hrvquest.common.context

import android.app.Application

/**
 * App上下文
 */
object AppContext {

    private lateinit var app: Application

    fun attach(instance: Application) {
        app = instance
    }

    fun app() = app

    fun getString(resId: Int): String = app.getString(resId)
}