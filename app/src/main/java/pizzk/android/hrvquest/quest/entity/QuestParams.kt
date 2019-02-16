package pizzk.android.hrvquest.quest.entity

/**
 * 问卷查询参数
 */
class QuestParams(
    //是否使用缓存
    var useCache: Boolean = true,
    //执行意图
    val action: String = ACTION_QUERY,
    //查询分类
    val classify: String = QuestEntity.SAS
) {
    companion object {
        const val ACTION_QUERY = "query"
        const val ACTION_CLEAN = "clean"
    }
}
