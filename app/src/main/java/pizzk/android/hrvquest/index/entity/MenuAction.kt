package pizzk.android.hrvquest.index.entity

import pizzk.android.hrvquest.quest.entity.QuestEntity

data class MenuAction(val key: String, val value: String) {
    companion object {
        fun getActions(): List<MenuAction> = listOf(
            MenuAction(QuestEntity.SAS, "焦虑"),
            MenuAction(QuestEntity.SSS, "睡眠状况"),
            MenuAction(QuestEntity.SDS, "抑郁"),
            MenuAction(QuestEntity.SMS, "记忆力")
        )
    }
}