package pizzk.android.hrvquest.common.repos

import pizzk.android.hrvquest.common.http.HttpClient
import pizzk.android.hrvquest.common.http.Interceptor
import pizzk.android.hrvquest.common.http.Request
import pizzk.android.hrvquest.common.http.Response

/**
 * 云接口数据仓库
 */
open class CloudRepos {

    fun http(block: (HttpClient) -> Response): Response = block(http)

    companion object {
        private const val HOST_URL = "https://api.larghetto.me"
        //JWT口令注入拦截器
        private val jwtInterceptor = object : Interceptor() {
            override fun onRequest(request: Request) {
                request.headers["Authorization"] = "Bearer 4X7r8mdcQwhOEa+kQUm86g=="
            }
        }
        private val http = HttpClient.Builder()
            .setHost(HOST_URL)
            .addInterceptor(jwtInterceptor)
            .build()
    }
}