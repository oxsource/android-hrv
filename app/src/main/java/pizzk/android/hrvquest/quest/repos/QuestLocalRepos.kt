package pizzk.android.hrvquest.quest.repos

import pizzk.android.hrvquest.common.extend.expire
import pizzk.android.hrvquest.common.extend.truncate
import pizzk.android.hrvquest.common.database.table.QuestChoiceDbEntity
import pizzk.android.hrvquest.common.database.table.QuestDbEntity
import pizzk.android.hrvquest.common.repos.LocalRepos
import pizzk.android.hrvquest.quest.entity.QuestChoiceEntity
import pizzk.android.hrvquest.quest.entity.QuestEntity
import pizzk.android.hrvquest.quest.entity.QuestParams

/**
 * 问卷本地数据仓库
 */
object QuestLocalRepos : LocalRepos() {

    /**
     * 获取问卷数据接口
     */
    fun list(params: QuestParams): List<QuestEntity> = database { db ->
        val classify: String = params.classify
        return@database db.questDao().find(classify).map { quest ->
            val entity = QuestEntity()
            entity.objectId = quest.id
            entity.updatedAt = quest.updatedAt
            entity.createdAt = quest.createdAt
            entity.type = quest.type
            entity.title = quest.title
            entity.textChoices = db.questChoiceDao().findAllByQuestId(questId = quest.id)
                .map { choice -> QuestChoiceEntity(choice.value, choice.text) }
            return@map entity
        }
    }

    /**
     * 统计数据量
     */
    fun count(): Int = database { db -> db.questDao().count() }

    /**
     * 存储更新数据
     */
    fun save(classify: String, quests: List<QuestEntity>) = database { db ->
        val combines: List<Pair<QuestDbEntity, List<QuestChoiceDbEntity>>> = quests.filter { entity ->
            //筛选本地需要更新的数据
            val id: String = entity.objectId ?: return@filter false
            val quest: QuestDbEntity = db.questDao().findById(id) ?: return@filter true
            return@filter quest.updatedAt.expire(entity.updatedAt)
        }.map { e ->
            //构建问卷记录
            val quest = QuestDbEntity()
            quest.id = e.objectId ?: ""
            quest.classify = classify
            quest.type = e.type ?: ""
            quest.title = e.title ?: ""
            quest.updatedAt = e.updatedAt
            quest.createdAt = e.createdAt
            //移除旧的选项
            val delChoices: List<QuestChoiceDbEntity> = db.questChoiceDao().findAllByQuestId(quest.id)
            db.questChoiceDao().delete(*delChoices.toTypedArray())
            //构建新的文字选项
            val choices: List<QuestChoiceDbEntity> = e.textChoices?.map choiceMap@{ c ->
                val choice = QuestChoiceDbEntity()
                choice.questId = quest.id
                choice.value = c.value
                choice.text = c.text
                return@choiceMap choice
            } ?: emptyList()
            return@map Pair(quest, choices)
        }
        //保存问卷记录
        db.questDao().save(*combines.map { it.first }.toTypedArray())
        //保存问卷选项
        val flatChoices: List<QuestChoiceDbEntity> = combines.map { it.second }.flatMap { it.asIterable() }
        db.questChoiceDao().save(*flatChoices.toTypedArray())
    }

    /**
     * 清除问卷数据
     */
    fun deleteAll() = database { db -> db.truncate("quest", "quest_choice") }
}