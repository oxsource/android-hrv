package pizzk.android.hrvquest.common.repos

import android.content.Context
import androidx.room.Room
import pizzk.android.hrvquest.common.context.AppContext
import pizzk.android.hrvquest.common.database.AppDatabase

/**
 * 本地数据仓库
 */
open class LocalRepos {

    //执行SQL
    fun <T> database(block: (AppDatabase) -> T): T {
        val context: Context = AppContext.app().applicationContext
        val dbName = "${context.applicationInfo.packageName}.app.db"
        val db: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, dbName).build()
        try {
            return block(db)
        } finally {
            db.close()
        }
    }
}