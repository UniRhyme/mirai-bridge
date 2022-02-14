package red.projekt.paimon.bridge.rabbitmq;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Receiver {
    private final ConnectionFactory factory;
    private Channel channel;

    private final ArrayList<Object> subscribers = new ArrayList<>();
    private final HashMap<Object, Class<?>> subscriberClasses = new HashMap<>();

    public Receiver(String host, int port, String queue) {
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        try (Connection connection = factory.newConnection()) {
            channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            Logger.getLogger("RabbitMQ-Receiver").severe("Error while creating connection");
            Logger.getLogger("RabbitMQ-Receiver").severe(e.getMessage());
        }
    }

    public void subscribe(Object subscriber, Class<?> clazz) {
        subscribers.add(subscriber);
        subscriberClasses.put(subscriber, clazz);
    }

    public void unsubscribe(Object subscriber) {
        subscribers.remove(subscriber);
    }

    public void start() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Logger.getLogger("RabbitMQ-Receiver").info("Received message: " + message);

            Gson gson = new Gson();

            // Dispatch message to subscribers
            subscribers.stream().filter(obj -> {
                Class<?> clazz = subscriberClasses.get(obj);
                JsonElement element = JsonParser.parseString(message);
                JsonObject object = element.getAsJsonObject();

                // All fields must be present in the message to ensure we will not dispatch to a wrong subscriber
                Field[] fields = clazz.getFields();

                boolean valid = true;
                // To check if all fields are present in the JSON message
                for (Field field : fields) {
                    AtomicBoolean found = new AtomicBoolean(false);
                    String name = field.getName();
                    object.entrySet().forEach(entry -> {
                        // If the field is present in the JSON message
                        if (entry.getKey().equals(name)) {
                            found.set(true);
                        }
                    });
                    // If the field is not present in the JSON message
                    // Then the message is not valid for this subscriber
                    if (!found.get()) {
                        valid = false;
                        break;
                    }
                }

                return valid;
            }).forEach(subscriber -> {
                Class<?> clazz = subscriberClasses.get(subscriber);
                Object object = gson.fromJson(message, clazz);

                for (Method method : object.getClass().getMethods()) {
                    // Check if method is annotated with @RabbitMQ
                    if (method.isAnnotationPresent(MQSubscribed.class)) {
                        // Check if the method has only 1 parameter which is assignable to the class of the object
                        if (method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(clazz)) {
                            // Set accessibility to true to allow invocation
                            method.setAccessible(true);
                            try {
                                // Invoke the method
                                method.invoke(subscriber, object);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                // They are almost impossible to happen, if they do, the method is never accessible
                                Logger.getLogger("RabbitMQ-Receiver").severe("Error while invoking method");
                                Logger.getLogger("RabbitMQ-Receiver").severe(e.getMessage());
                            }

                        }
                    }
                }
            });
        };

        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicConsume(channel.queueDeclare().getQueue(), true, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException e) {
            Logger.getLogger("RabbitMQ-Receiver").severe("Error while consuming");
            Logger.getLogger("RabbitMQ-Receiver").severe(e.getMessage());
        }
    }
}
