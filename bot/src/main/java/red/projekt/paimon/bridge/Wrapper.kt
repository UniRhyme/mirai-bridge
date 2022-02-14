package red.projekt.paimon.bridge

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info


object Wrapper : KotlinPlugin(
    JvmPluginDescription(
        id = "red.projekt.paimon-bridge",
        name = "PaimonBridge",
        version = "0.1.0"
    )
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}