package chooongg.box.log

import chooongg.box.log.handler.AnyLogHandler
import chooongg.box.log.handler.BundleLogHandler
import chooongg.box.log.handler.JsonLogHandler

object LogConstant {

    fun getDefaultHandlers() = linkedSetOf(
        AnyLogHandler,
        JsonLogHandler,
        BundleLogHandler
    )

    const val DEFAULT_STACK_OFFSET = 3

    const val LINE_MAX_LENGTH = 120
    const val CHILD_TAG_OFFSET = 5
    const val CHILD_TYPE_OFFSET = 5

    const val TABS_DM_NN = '═'
    const val TABS_NN_DM = '║'
    const val TABS_SM_NN = '─'
    const val TABS_NN_SM = '│'


    /**
     * ╔ ╦ ╗
     * ╠ ╬ ╣
     * ╚ ╩ ╝
     */
    const val TABS_DL_DT = '╔'
    const val TABS_DL_DM = '╠'
    const val TABS_DL_DB = '╚'
    const val TABS_DM_DT = '╦'
    const val TABS_DM_DM = '╬'
    const val TABS_DM_DB = '╩'
    const val TABS_DR_DT = '╗'
    const val TABS_DR_DM = '╣'
    const val TABS_DR_DB = '╝'

    /**
     * ┌ ┬ ┐
     * ├ ┼ ┤
     * └ ┴ ┘
     */
    const val TABS_SL_ST = '┌'
    const val TABS_SL_SM = '├'
    const val TABS_SL_SB = '└'
    const val TABS_SM_ST = '┬'
    const val TABS_SM_SM = '┼'
    const val TABS_SM_SB = '┴'
    const val TABS_SR_ST = '┐'
    const val TABS_SR_SM = '┤'
    const val TABS_SR_SB = '┘'

    /**
     * ╓ ╥ ╖
     * ╟ ╫ ╢
     * ╙ ╨ ╜
     */
    const val TABS_SL_DT = '╓'
    const val TABS_SL_DM = '╟'
    const val TABS_SL_DB = '╙'
    const val TABS_SM_DT = '╥'
    const val TABS_SM_DM = '╫'
    const val TABS_SM_DB = '╨'
    const val TABS_SR_DT = '╖'
    const val TABS_SR_DM = '╢'
    const val TABS_SR_DB = '╜'

    /**
     * ╒ ╤ ╕
     * ╞ ╪ ╡
     * ╘ ╧ ╛
     */
    const val TABS_DL_ST = '╒'
    const val TABS_DL_SM = '╞'
    const val TABS_DL_SB = '╘'
    const val TABS_DM_ST = '╤'
    const val TABS_DM_SM = '╪'
    const val TABS_DM_SB = '╧'
    const val TABS_DR_ST = '╕'
    const val TABS_DR_SM = '╡'
    const val TABS_DR_SB = '╛'

    val BR = System.getProperty("line.separator")!! // 换行符

    const val BLANK = ' '

    const val NONE = "[Params is Null!]"

    const val FORMAT_STEP = "    "
    const val FORMAT_STEP_COUNT = 4
}