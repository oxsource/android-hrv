package pizzk.android.hrvquest.quest.repos

import pizzk.android.hrvquest.common.entity.Recycled
import pizzk.android.hrvquest.common.http.Request
import pizzk.android.hrvquest.common.http.Response
import pizzk.android.hrvquest.common.entity.PagedQuery
import pizzk.android.hrvquest.common.repos.CloudRepos

/**
 * 问卷云端数据仓库
 */
object QuestCloudRepos : CloudRepos() {

    /**
     * 获取问卷数据接口(暂未使用)
     */
    fun list(p: PagedQuery): Response = http { client ->
        val request: Request = Recycled.obtain(Request::class.java)
        request.url = "/v1/HIS/Evaluation"
        request.params["count"] = if (p.page == 0) "1" else "0"
        request.params["limit"] = "$p.size"
        request.params["skip"] = "${p.page * p.size}"
        request.params["order"] = "-updatedAt"
        return@http client.get(request)
    }

    /**
     * 通过分类获取问卷数据接口
     */
    fun listByClassify(classify: String): Response = http { client ->
        val request: Request = Recycled.obtain(Request::class.java)
        request.url = "/v1/HIS/Evaluation/$classify"
        return@http client.get(request)
    }

}