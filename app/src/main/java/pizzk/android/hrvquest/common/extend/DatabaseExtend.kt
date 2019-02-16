package pizzk.android.hrvquest.common.extend

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * 清除表数据
 */
fun RoomDatabase.truncate(vararg tables: String) {
    try {
        val ssd: SupportSQLiteDatabase = this.openHelper.writableDatabase
        tables.iterator().forEach { table ->
            //清除数据
            val deleteSql = "DELETE FROM $table;"
            ssd.execSQL(deleteSql)
            //重置自增索引
            val resetSql = "UPDATE sqlite_sequence SET seq = 0 WHERE name = '$table';"
            ssd.execSQL(resetSql)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}