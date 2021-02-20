package chooongg.box.ext

/**
 * 判断 Any 是否为基本类型
 */
fun isPrimitiveType(value: Any?) = when (value) {
    is Int -> true
    is Byte -> true
    is Char -> true
    is Long -> true
    is Float -> true
    is Short -> true
    is Double -> true
    is String -> true
    is Boolean -> true
    is CharSequence -> true
    else -> false
}