package pizzk.android.hrvquest.common.vm

import android.os.AsyncTask
import pizzk.android.hrvquest.common.entity.Payload
import java.util.*

/**
 * 视图模型
 */
open class ViewModel {
    //回调函数
    private var start: () -> Unit = {}
    private var async: (p: Any?) -> Payload = { Payload.obtain() }
    private var progress: (p: Payload?) -> Unit = {}
    private var result: (p: Payload?) -> Unit = {}

    /**调用*/

    fun doStart() = start()

    fun doAsync(p: Any?) = async(p)

    fun doProgress(p: Payload?) = progress(p)

    fun doResult(p: Payload?) = result(p)

    /**赋值*/

    fun onStart(call: () -> Unit): ViewModel {
        start = call
        return this
    }

    fun onAsync(call: (p: Any?) -> Payload): ViewModel {
        async = call
        return this
    }

    fun onProgress(call: (p: Payload?) -> Unit): ViewModel {
        progress = call
        return this
    }

    fun onResult(call: (result: Payload?) -> Unit): ViewModel {
        result = call
        return this
    }

    open fun getName(): String = javaClass.name
}

/**
 * 视图模型任务
 */
class ViewModelTask(val vm: ViewModel) : AsyncTask<Any, Payload, Payload>() {

    override fun onPreExecute() {
        super.onPreExecute()
        vm.doStart()
    }

    override fun doInBackground(vararg p: Any): Payload {
        return try {
            vm.doAsync(if (p.isEmpty()) null else p[0])
        } catch (e: Exception) {
            Payload.obtain(msg = e.message.toString())
        }
    }

    override fun onProgressUpdate(vararg values: Payload?) {
        super.onProgressUpdate(*values)
        vm.doProgress(if (values.isEmpty()) null else values[0])
    }

    override fun onPostExecute(result: Payload?) {
        super.onPostExecute(result)
        vm.doResult(result)
        VMTaskManager.doNext(previous = this)
    }

    override fun onCancelled() {
        super.onCancelled()
        VMTaskManager.doNext(previous = this)
    }

    override fun onCancelled(result: Payload?) {
        super.onCancelled(result)
        VMTaskManager.doNext(previous = this)
    }
}


/**
 * 视图模型任务管理器
 */
object VMTaskManager {
    //最大并行任务数量
    private const val MAX_PARALLEL_TASK = 6
    //
    private val waits: MutableList<Triple<Int, ViewModel, Any?>> = LinkedList()
    private val tasks: MutableList<ViewModelTask> = LinkedList()

    /**
     * 放入等待队列，空闲时执行
     */
    fun queue(vm: ViewModel, payload: Any? = null, priority: Int = 100) {
        waits.find { it.second.getName() == vm.getName() }?.let { waits.remove(it) }
        waits.add(Triple(priority, vm, payload))
    }

    /**
     * 立即调用执行
     */
    fun call(vm: ViewModel?, payload: Any? = null, refuseIfRunning: Boolean = true): Boolean {
        vm ?: return false
        //判断是否已有正在进行的相同任务
        val exist: Boolean = tasks.find { it.vm.getName() == vm.getName() } != null
        if (exist && refuseIfRunning) return false
        //任务量过载时加入等待
        if (tasks.size >= MAX_PARALLEL_TASK) {
            queue(vm, priority = 1)
            return false
        }
        val task = ViewModelTask(vm)
        tasks.add(task)
        task.execute(payload)
        return true
    }

    /**
     * 从等待队列中取出优先级最高的一个并执行
     */
    fun doNext(previous: ViewModelTask): Boolean {
        tasks.remove(previous)
        waits.sortBy { it.first }
        val e: Triple<Int, ViewModel, Any?> = waits.firstOrNull() ?: return false
        waits.remove(e)
        call(e.second, e.third)
        return true
    }

    /**
     * 取消执行
     */
    fun cancel(name: String?, force: Boolean = true) {
        name ?: return
        waits.filter { it.second.getName() == name }.forEach { e -> waits.remove(e) }
        tasks.filter { it.vm.getName() == name }.forEach { it.cancel(force) }
    }
}