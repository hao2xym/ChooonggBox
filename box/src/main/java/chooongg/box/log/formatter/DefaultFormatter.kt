package chooongg.box.log.formatter

import chooongg.box.ext.getByteGB2312Length
import chooongg.box.ext.removeLinefeed
import chooongg.box.log.LogConstant

object DefaultFormatter : Formatter {

    override fun top(childTag: String?, text: String?, type: String?) = buildString {
        val textProcessed = childTag?.removeLinefeed(" ")
        if (textProcessed.isNullOrEmpty()) append(' ') else append(textProcessed)
        append(LogConstant.BR)

        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.TABS_DL_DT)
        maxLength -= 1
        for (i in 0 until LogConstant.CHILD_TAG_OFFSET) {
            append(LogConstant.TABS_DM_NN)
            maxLength -= 1
        }

        val textLength = text?.removeLinefeed(" ")?.getByteGB2312Length() ?: 0
        if (textLength > 0) {
            append(LogConstant.BLANK)
            maxLength -= 1

            append(text)
            maxLength -= textLength

            append(LogConstant.BLANK)
            maxLength -= 1
        }
        val typeLength = type.getByteGB2312Length()
        while (maxLength > 0) {
            append(LogConstant.TABS_DM_NN)
            maxLength -= 1

            if (type != null && LogConstant.CHILD_TYPE_OFFSET + typeLength == maxLength) {
                append('<')
                maxLength -= 1

                append(type)
                maxLength -= typeLength

                append('>')
                maxLength -= 1
            }
        }
    }

    override fun middlePrimary(text: String?, type: String?) = buildString {
        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.TABS_DL_DM)
        maxLength -= 1
        for (i in 0 until LogConstant.CHILD_TAG_OFFSET) {
            append(LogConstant.TABS_DM_NN)
            maxLength -= 1
        }

        val textLength = text?.removeLinefeed(" ")?.getByteGB2312Length() ?: 0
        if (textLength > 0) {
            append(LogConstant.BLANK)
            maxLength -= 1

            append(text)
            maxLength -= textLength

            append(LogConstant.BLANK)
            maxLength -= 1
        }
        val typeLength = type.getByteGB2312Length()
        while (maxLength > 0) {
            append(LogConstant.TABS_DM_NN)
            maxLength -= 1

            if (type != null && LogConstant.CHILD_TYPE_OFFSET + typeLength == maxLength) {
                append('<')
                maxLength -= 1

                append(type)
                maxLength -= typeLength

                append('>')
                maxLength -= 1
            }
        }
    }

    override fun middleSecondary(text: String?, type: String?) = buildString {
        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.TABS_SL_DM)
        maxLength -= 1
        for (i in 0 until LogConstant.CHILD_TAG_OFFSET) {
            append(LogConstant.TABS_SM_NN)
            maxLength -= 1
        }

        val textLength = text?.removeLinefeed(" ")?.getByteGB2312Length() ?: 0
        if (textLength > 0) {
            append(LogConstant.BLANK)
            maxLength -= 1

            append(text)
            maxLength -= textLength

            append(LogConstant.BLANK)
            maxLength -= 1
        }
        val typeLength = type.getByteGB2312Length()
        while (maxLength > 0) {
            append(LogConstant.TABS_SM_NN)
            maxLength -= 1

            if (type != null && LogConstant.CHILD_TYPE_OFFSET + typeLength == maxLength) {
                append('<')
                maxLength -= 1

                append(type)
                maxLength -= typeLength

                append('>')
                maxLength -= 1
            }
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