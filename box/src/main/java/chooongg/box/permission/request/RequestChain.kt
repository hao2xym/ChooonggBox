package chooongg.box.permission.request

/**
 * 维护权限请求过程的任务链
 */
class RequestChain {
    /**
     * 承担请求过程的第一项任务。权限请求从此处开始
     */
    private var headTask: BaseTask? = null

    /**
     * 保留请求过程的最后一个任务。权限请求到此结束
     */
    private var tailTask: BaseTask? = null

    /**
     * Add a task into task chain.
     * @param task  task to add.
     */
    fun addTaskToChain(task: BaseTask?) {
        if (headTask == null) {
            headTask = task
        }
        // add task to the tail
        if (tailTask != null) {
            tailTask!!.next = task
        }
        tailTask = task
    }

    /**
     * Run this task chain from the first task.
     */
    fun runTask() {
        headTask!!.request()
    }
}