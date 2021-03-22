package chooongg.box.ext

import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * @sample formatNumber("#.00")
 * @param pattern 格式
 *  0 = 数字
 *  # = 数字，零则不显示
 *  . = 小数分隔符
 *  - = 减号
 *  , = 分组分隔符
 *  E = 科学计数法
 *  % = 乘100显示百分比
 *  \u2030 = 乘1000显示千分比
 *  ' = 以上字符前缀，使其为字符显示
 */
fun Long.formatNumber(pattern: String): String {
    return formatNumberByAny(pattern)
}

fun Int.formatNumber(pattern: String): String {
    return formatNumberByAny(pattern)
}

fun Short.formatNumber(pattern: String): String {
    return formatNumberByAny(pattern)
}

fun Byte.formatNumber(pattern: String): String {
    return formatNumberByAny(pattern)
}

fun AtomicInteger.formatNumber(pattern: String): String {
    return formatNumberByAny(pattern)
}

fun AtomicLong.formatNumber(pattern: String): String {
    return formatNumberByAny(pattern)
}

fun BigInteger.formatNumber(pattern: String): String {
    return formatNumberByAny(pattern)
}

fun BigDecimal.formatNumber(pattern: String): String {
    return formatNumberByAny(pattern)
}

fun Number.formatNumber(pattern: String): String {
    return formatNumberByAny(pattern)
}

private fun Any.formatNumberByAny(pattern: String): String {
    return try {
        val decimalFormat = DecimalFormat(pattern)
        decimalFormat.format(this)
    } catch (e: Exception) {
        this.toString()
    }
}