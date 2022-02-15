package red.projekt.paimon.bridge.listener;

import com.sun.jdi.InvocationException;
import net.mamoe.mirai.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class EventHandler {
    private static final InnerCompanion companion = new InnerCompanion();

    public static void registerHandler(Object handler) {
        companion.handlers.add(handler);
    }

    public static void unregisterHandler(Object handler) {
        companion.handlers.remove(handler);
    }

    public static void dispatchEvent(Event event) {
        // Check null
        if (event != null) {
            for (Object handler : companion.handlers) {
                Class<?> clazz = handler.getClass();
                Method[] methods = clazz.getMethods();

                for (Method method : methods) {
                    // We care only about methods with a certain annotation
                    if (method.isAnnotationPresent(SubscribeEvent.class) && method.getAnnotation(SubscribeEvent.class).type().isAssignableFrom(event.getClass())) {
                        if (method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                            method.setAccessible(true);
                            try {
                                method.invoke(handler, event);
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    static class InnerCompanion {
        public ArrayList<Object> handlers = new ArrayList<>();
    }
}
