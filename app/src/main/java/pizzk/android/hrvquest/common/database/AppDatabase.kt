package pizzk.android.hrvquest.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pizzk.android.hrvquest.common.database.dao.QuestChoiceDbDao
import pizzk.android.hrvquest.common.database.dao.QuestDbDao
import pizzk.android.hrvquest.common.database.table.QuestChoiceDbEntity
import pizzk.android.hrvquest.common.database.table.QuestDbEntity

/**
 * 基于Room的App数据库
 * 相关文档：https://developer.android.google.cn/training/data-storage/room
 */
@Database(
    entities = [
        QuestDbEntity::class,
        QuestChoiceDbEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [RoomTypeConverters::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun questDao(): QuestDbDao

    abstract fun questChoiceDao(): QuestChoiceDbDao
}