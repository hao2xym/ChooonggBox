package chooongg.box.logger.formatter

/**
 * @since: V2.0 格式化日志，便于 Printer 进行打印。每一个 Printer 包含一个自身的 Formatter
 */
abstract class Formatter {

    companion object {
        const val LINE_MAX_LENGTH = 120

        val BR = System.getProperty("line.separator")!! // 换行符

        protected fun String?.removeWrap() = this?.replace("\r|\n", "")

        protected fun String?.getBytesLength() = this?.toByteArray()?.size ?: 0
    }


    /**
     * 顶部
     */
    abstract fun top(text: String?): String

    /**
     * 中间分割
     */
    abstract fun middle(text: String?): String

    /**
     * 分隔单位
     */
    abstract fun separator(): String

    /**
     * 底部
     */
    abstract fun bottom(): String
}