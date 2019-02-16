package pizzk.android.hrvquest.quest.vm

import android.util.Log
import pizzk.android.hrvquest.R
import pizzk.android.hrvquest.common.context.AppContext
import pizzk.android.hrvquest.common.entity.Payload
import pizzk.android.hrvquest.common.http.Response
import pizzk.android.hrvquest.common.vm.ViewModel
import pizzk.android.hrvquest.common.entity.HttpPacket
import pizzk.android.hrvquest.quest.entity.QuestEntity
import pizzk.android.hrvquest.quest.entity.QuestParams
import pizzk.android.hrvquest.quest.repos.QuestCloudRepos
import pizzk.android.hrvquest.quest.repos.QuestLocalRepos
import java.lang.Exception

/**
 * 问卷信息视图模型
 */
class QuestViewModel(block: QuestViewModel.() -> Unit = {}) : ViewModel() {

    init {
        block(this)
        onAsync { e ->
            val params: QuestParams = e as? QuestParams ?: QuestParams()
            return@onAsync when (params.action) {
                QuestParams.ACTION_CLEAN -> clean()
                else -> query(params)
            }
        }
    }

    //数据查询
    private fun query(params: QuestParams): Payload {
        //参数校验
        val classify: String = params.classify
        if (classify.isEmpty()) return Payload.obtain(msg = AppContext.getString(R.string.please_special_classify))
        //开始执行
        if (params.useCache) {
            //本地获取数据
            val result: Payload? = loadFromDatabase(params)
            if (null !== result) return result
        }
        //云端获取数据：不使用缓存或者从缓存获取数据失败
        val response: Response = QuestCloudRepos.listByClassify(classify)
        val packet: HttpPacket<List<QuestEntity>> = response.json()
        if (!packet.successful()) return packet.result()
        //缓存至本地
        QuestLocalRepos.save(classify, quests = packet.resultBody ?: emptyList())
        return packet.result()
    }

    private fun loadFromDatabase(params: QuestParams): Payload? {
        try {
            val entities: List<QuestEntity> = QuestLocalRepos.list(params)
            if (entities.isNotEmpty()) return Payload.obtain(success = true, value = entities)
        } catch (e: Exception) {
            Log.e(QuestViewModel::class.java.name, e.message)
        }
        return null
    }


    //清理本地所有数据(对外提供接口，以防云端某些数据不存在，本地无法感知等情况)
    private fun clean(): Payload {
        QuestLocalRepos.deleteAll()
        return Payload.obtain(success = true)
    }
}