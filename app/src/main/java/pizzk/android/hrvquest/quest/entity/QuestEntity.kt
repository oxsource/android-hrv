package pizzk.android.hrvquest.quest.entity

import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.context.AppContext
import java.util.*

/**
 * 文字选项
 */
data class QuestChoiceEntity(
    var value: Int = -1,
    var text: String = ""
)

/**
 * 问卷其中某项的实体
 */
data class QuestEntity(
    var objectId: String? = null,
    var updatedAt: Date? = null,
    var createdAt: Date? = null,
    var type: String? = null,
    var title: String? = null,
    var textChoices: List<QuestChoiceEntity>? = null
) {
    companion object {
        //SAS--焦虑,睡眠状况--SSS,抑郁-SDS,记忆力--SMS
        const val SAS = "SAS"
        const val SSS = "SSS"
        const val SDS = "SDS"
        const val SMS = "SMS"
        //题目类型(接口数据返回暂时仅有单选)
        const val TYPE_SINGLE_CHOICE = "singleChoice"

        fun classifyExplain(classify: String): String {
            return when (classify) {
                SAS -> AppContext.getString(R.string.quest_classify_sas)
                SSS -> AppContext.getString(R.string.quest_classify_sss)
                SDS -> AppContext.getString(R.string.quest_classify_sds)
                SMS -> AppContext.getString(R.string.quest_classify_sms)
                else -> AppContext.getString(R.string.unknown)
            }
        }
    }
}