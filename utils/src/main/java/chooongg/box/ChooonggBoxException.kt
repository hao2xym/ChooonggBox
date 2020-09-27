package chooongg.box

/**
 * 框架异常
 */
class ChooonggBoxException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}