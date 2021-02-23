package chooongg.box.log.formatter

import chooongg.box.ext.getByteGB2312Length
import chooongg.box.ext.removeLinefeed
import chooongg.box.log.LogConstant

object DefaultFormatter : Formatter {

    override fun top(childTag: String?, text: String?) = buildString {
        val textProcessed = text?.removeLinefeed(" ")
        if (textProcessed.isNullOrEmpty()) append(' ') else append(textProcessed)
        append(LogConstant.BR)

        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.TABS_DL_DT)
        maxLength -= 1
        for (i in 0 until LogConstant.CHILD_TAG_OFFSET) {
            append(LogConstant.TABS_DM_NN)
            maxLength -= 1
        }

        val textLength = childTag?.removeLinefeed(" ")?.getByteGB2312Length() ?: 0
        if (textLength > 0) {
            if (LogConstant.TAG_HIGHLIGHT) {
                append(LogConstant.TABS_DR_DB).append(LogConstant.BLANK)
                maxLength -= 2
            }

            append(childTag)
            maxLength -= textLength

            if (LogConstant.TAG_HIGHLIGHT) {
                append(LogConstant.BLANK).append(LogConstant.TABS_DL_DB)
                maxLength -= 2
            }
        }
        while (maxLength > 0) {
            append(LogConstant.TABS_DM_NN)
            maxLength -= 1
        }
    }

    override fun middlePrimary(text: String?) = buildString {
        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.TABS_DL_DM)
        maxLength -= 1
        for (i in 0 until LogConstant.CONTENT_TAG_OFFSET) {
            append(LogConstant.TABS_DM_NN)
            maxLength -= 1
        }

        val textLength = text?.removeLinefeed(" ")?.getByteGB2312Length() ?: 0
        if (textLength > 0) {
            if (LogConstant.TAG_HIGHLIGHT) {
                append(LogConstant.TABS_DR_SB).append(LogConstant.BLANK)
                maxLength -= 2
            }

            append(text)
            maxLength -= textLength

            if (LogConstant.TAG_HIGHLIGHT) {
                append(LogConstant.BLANK).append(LogConstant.TABS_DL_SB)
                maxLength -= 2
            }
        }
        while (maxLength > 0) {
            append(LogConstant.TABS_DM_NN)
            maxLength -= 1
        }
    }

    override fun middleSecondary(text: String?) = buildString {
        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.TABS_SL_DM)
        maxLength -= 1
        for (i in 0 until LogConstant.CONTENT_TAG_OFFSET) {
            append(LogConstant.TABS_SM_NN)
            maxLength -= 1
        }

        val textLength = text?.removeLinefeed(" ")?.getByteGB2312Length() ?: 0
        if (textLength > 0) {
            if (LogConstant.TAG_HIGHLIGHT) {
                append(LogConstant.TABS_SR_SB).append(LogConstant.BLANK)
                maxLength -= 2
            }

            append(text)
            maxLength -= textLength

            if (LogConstant.TAG_HIGHLIGHT) {
                append(LogConstant.BLANK).append(LogConstant.TABS_SL_SB)
                maxLength -= 2
            }
        }
        while (maxLength > 0) {
            append(LogConstant.TABS_SM_NN)
            maxLength -= 1
        }
    }

    override fun middle(text: String?) = buildString {
        append(LogConstant.TABS_NN_DM).append(LogConstant.BLANK).append(text ?: "")
    }

    override fun bottom() = buildString {
        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.TABS_DL_DB)
        maxLength -= 1

        while (maxLength > 0) {
            append(LogConstant.TABS_DM_NN)
            maxLength -= 1
        }
    }

    override fun separator() = LogConstant.BR
}