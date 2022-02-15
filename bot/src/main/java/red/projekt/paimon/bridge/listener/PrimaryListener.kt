package red.projekt.paimon.bridge.listener

import net.mamoe.mirai.event.events.MessageEvent

object PrimaryListener {
    @SubscribeEvent(type = MessageEvent::class)
    fun onMessageEvent(event: MessageEvent) {

    }
}