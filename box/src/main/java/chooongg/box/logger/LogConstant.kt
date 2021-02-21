package chooongg.box.logger

object LogConstant {

    const val DEFAULT_STACK_OFFSET = 3

    const val LINE_MAX_LENGTH = 120

    const val D_TL = '╔'
    const val D_MD = '╠'
    const val D_ML = '╟'
    const val D_BL = '╚'

    const val L_TL = '┌'
    const val L_MD = '╞'
    const val L_ML = '├'
    const val L_BL = '└'

    const val D_H_LINE = '═'
    const val D_V_LINE = '║'
    const val L_H_LINE = '─'
    const val L_V_LINE = '│'

    const val BLANK = ' '

    val BR = System.getProperty("line.separator")!! // 换行符

}