package red.projekt.paimon.bridge

import kotlinx.coroutines.CompletableJob
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.utils.info
import red.projekt.paimon.bridge.listener.EventHandler


object Wrapper : KotlinPlugin(
    JvmPluginDescription(
        id = "red.projekt.paimon-bridge",
        name = "PaimonBridge",
        version = "0.1.0"
    )
) {
    private lateinit var disableCallback: () -> Unit

    override fun onEnable() {
        logger.info { "Plugin loaded" }

        // Listen all types of events and pass them to the EventHandler
        val listener: CompletableJob = GlobalEventChannel.subscribeAlways<Event> { event->
            EventHandler.dispatchEvent(event)
        }

        disableCallback = {
            listener.complete()
        }
    }

    override fun onDisable() {
        disableCallback()
    }
}