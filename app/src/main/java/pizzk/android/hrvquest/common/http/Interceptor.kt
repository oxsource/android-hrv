package pizzk.android.hrvquest.common.http

abstract class Interceptor {

    open fun onRequest(request: Request) {}

    open fun onResponse(response: Response) {}
}