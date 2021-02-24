package chooongg.box.log.formatter

/**
 * @since: V2.0 格式化日志，便于 Printer 进行打印。每一个 Printer 包含一个自身的 Formatter
 */
interface Formatter {


    /**
     * 顶部
     */
    fun top(childTag: String?, text: String?, type: String?): String

    /**
     * 中间主要
     */
    fun middlePrimary(text: String?, type: String?): String

    /**
     * 中间次要
     */
    fun middleSecondary(text: String?, type: String?): String

    /**
     * 中间内容
     */
    fun middle(text: String?): String

    /**
     * 底部
     */
    fun bottom(): String

    /**
     * 分隔单位
     */
    fun separator(): String
}