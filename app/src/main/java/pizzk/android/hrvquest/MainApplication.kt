package pizzk.android.hrvquest

import android.app.Application
import pizzk.android.hrvquest.common.context.AppContext

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.attach(instance = this)
    }
}