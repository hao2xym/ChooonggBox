package chooongg.box.log

class LogBean {
    internal val contentTag: String?
    internal val any: Any?
    internal val isShowType: Boolean

    constructor(any: Any?) {
        this.contentTag = null
        this.any = any
        this.isShowType = true
    }

    constructor(contentTag: String?, any: Any?) {
        this.contentTag = contentTag
        this.any = any
        this.isShowType = true
    }

    constructor(contentTag: String?, any: Any?, isShowType: Boolean) {
        this.contentTag = contentTag
        this.any = any
        this.isShowType = isShowType
    }
}
