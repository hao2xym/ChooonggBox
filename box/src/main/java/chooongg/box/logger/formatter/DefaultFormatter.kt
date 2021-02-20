package chooongg.box.logger.formatter

object DefaultFormatter : Formatter() {

    const val TOP_LEFT_CORNER = '╔'
    const val BOTTOM_LEFT_CORNER = '╚'
    const val MIDDLE_CORNER = '╟'
    const val HORIZONTAL_DOUBLE_LINE = "║"

    const val DOUBLE_DIVIDER = "═"
    const val SINGLE_DIVIDER = "─"

    override fun top(text: String?): String {
        return ""
    }

    override fun middle(text: String?): String {
        return ""
    }

    override fun separator(): String {
        return ""
    }

    override fun bottom(): String {
        return ""
    }
}