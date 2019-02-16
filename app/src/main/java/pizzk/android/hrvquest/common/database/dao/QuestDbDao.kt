package pizzk.android.hrvquest.common.database.dao

import androidx.room.*
import pizzk.android.hrvquest.common.database.table.QuestChoiceDbEntity
import pizzk.android.hrvquest.common.database.table.QuestDbEntity

@Dao
interface QuestDbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg quests: QuestDbEntity)

    @Query(value = "SELECT * FROM quest WHERE classify =:classify")
    fun find(classify: String): List<QuestDbEntity>

    @Query(value = "SELECT * FROM quest WHERE id= :id")
    fun findById(id: String): QuestDbEntity?

    @Query(value = "SELECT count(1) FROM quest")
    fun count(): Int
}

@Dao
interface QuestChoiceDbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg choices: QuestChoiceDbEntity)

    @Delete
    fun delete(vararg choices: QuestChoiceDbEntity)

    @Query(value = "SELECT * FROM quest_choice WHERE questId = :questId")
    fun findAllByQuestId(questId: String): List<QuestChoiceDbEntity>

    @Query(value = "SELECT count(1) FROM quest_choice")
    fun count(): Int
}