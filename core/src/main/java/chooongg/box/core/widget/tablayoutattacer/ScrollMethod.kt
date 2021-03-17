package chooongg.box.core.widget.tablayoutattacer

internal sealed class ScrollMethod {
    object Direct : ScrollMethod()
    object Smooth : ScrollMethod()
    class LimitedSmooth(val limit: Int) : ScrollMethod()
}