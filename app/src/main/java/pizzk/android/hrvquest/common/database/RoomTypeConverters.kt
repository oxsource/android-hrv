package pizzk.android.hrvquest.common.database

import androidx.room.TypeConverter
import java.util.*

/**
 * 自定义类型装换器
 */
class RoomTypeConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        value ?: return null
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        date ?: return null
        return date.time
    }
}