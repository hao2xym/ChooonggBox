package chooongg.box.core.statePage

object StatePage {

    internal val config = StatePageConfig()

    public fun setDefaultConfig(block: (StatePageConfig) -> Unit) {
        block.invoke(config)
    }

}