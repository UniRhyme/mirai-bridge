package red.projekt.paimon.bridge.listener;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.lang.annotation.Retention;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SubscribeEvent {
    Class<? extends Event> type() default GroupMessageEvent.class;
}
