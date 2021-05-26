package chooongg.box.core.permission.request

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
     * 将任务添加到任务链
     * @param task 要添加的任务
     */
    fun addTaskToChain(task: BaseTask) {
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
     * 从第一个任务运行此任务链
     */
    fun runTask() {
        headTask!!.request()
    }
}