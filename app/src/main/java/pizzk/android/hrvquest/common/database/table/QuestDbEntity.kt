package pizzk.android.hrvquest.common.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * 问卷信息表
 */
@Entity(tableName = "quest")
data class QuestDbEntity(
    @PrimaryKey
    var id: String = "",
    //分类(SAS--焦虑,睡眠状况--SSS,抑郁-SDS,记忆力--SMS)
    var classify: String = "",
    var type: String = "",
    var title: String = "",
    var createdAt: Date? = null,
    var updatedAt: Date? = null
)

/**
 * 问卷选项表
 */
@Entity(tableName = "quest_choice")
data class QuestChoiceDbEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var questId: String = "",
    var value: Int = -1,
    var text: String = ""
)