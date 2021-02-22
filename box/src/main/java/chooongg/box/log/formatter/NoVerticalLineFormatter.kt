package chooongg.box.log.formatter

import chooongg.box.ext.getByteGB2312Length
import chooongg.box.ext.removeLinefeed
import chooongg.box.log.LogConstant

object NoVerticalLineFormatter : Formatter {

    override fun top(childTag: String?, text: String?) = buildString {
        val textProcessed = text?.removeLinefeed(" ")
        if (textProcessed.isNullOrEmpty()) append(' ') else append(textProcessed)
        append(LogConstant.BR)

        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.D_TL)
            .append(LogConstant.D_H_LINE)
            .append(LogConstant.D_H_LINE)
            .append(LogConstant.D_H_LINE)
        maxLength -= 4

        val textLength = childTag?.removeLinefeed(" ")?.getByteGB2312Length() ?: 0
        if (textLength > 0) {
            append(childTag)
            maxLength -= textLength
        }
        while (maxLength > 0) {
            append(LogConstant.D_H_LINE)
            maxLength -= 1
        }
    }

    override fun middlePrimary(text: String?) = buildString {
        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.D_MD)
            .append(LogConstant.D_H_LINE)
            .append(LogConstant.D_H_LINE)
            .append(LogConstant.D_H_LINE)
        maxLength -= 4

        val textLength = text?.removeLinefeed(" ")?.getByteGB2312Length() ?: 0
        if (textLength > 0) {
            append(text)
            maxLength -= textLength
        }
        while (maxLength > 0) {
            append(LogConstant.D_H_LINE)
            maxLength -= 1
        }
    }

    override fun middleSecondary(text: String?) = buildString {
        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.D_ML)
            .append(LogConstant.L_H_LINE)
            .append(LogConstant.L_H_LINE)
            .append(LogConstant.L_H_LINE)
        maxLength -= 4

        val textLength = text?.removeLinefeed(" ")?.getByteGB2312Length() ?: 0
        if (textLength > 0) {
            append(text)
            maxLength -= textLength
        }
        while (maxLength > 0) {
            append(LogConstant.L_H_LINE)
            maxLength -= 1
        }
    }

    override fun middle(text: String?) = buildString {
        append(LogConstant.BLANK).append(LogConstant.BLANK).append(text ?: "")
    }

    override fun bottom() = buildString {
        var maxLength = LogConstant.LINE_MAX_LENGTH

        append(LogConstant.D_BL)
        maxLength -= 1

        while (maxLength > 0) {
            append(LogConstant.D_H_LINE)
            maxLength -= 1
        }
    }

    override fun separator() = LogConstant.BR
}