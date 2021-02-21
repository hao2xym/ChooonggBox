package chooongg.box.ext

import java.nio.charset.Charset

fun String?.getByteUTF8Length() = this?.toByteArray(Charsets.UTF_8)?.size ?: 0
fun String?.getByteGB2312Length() = this?.toByteArray(Charset.forName("GB2312"))?.size ?: 0

/**
 * 删除换行符
 */
fun String.removeLinefeed(replace: String) = replace("\r|\n", replace)