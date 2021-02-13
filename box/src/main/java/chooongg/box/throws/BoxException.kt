package chooongg.box.throws

/**
 * Box Frame Exception
 */
class BoxException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}