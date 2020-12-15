package com.company;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.Closeable;
import java.io.IOException;

public class TemperatureController implements Closeable {
    private final MqttClient client;
    private final String temperatureTopic;
    private final String controlTopic;
    private final int qos;

    public TemperatureController(String broker, String clientId, String temperatureTopic, String controlTopic, int qos) throws MqttException {
        this.client = new MqttClient(broker, clientId, new MemoryPersistence());
        this.temperatureTopic = temperatureTopic;
        this.controlTopic = controlTopic;
        this.qos = qos;
    }


    public void begin() throws MqttException {
        var options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);
        client.subscribe(temperatureTopic, (tt, receivedMessage) -> {
            int value = Integer.parseInt(receivedMessage.toString());
            MqttMessage transmitMessage = new MqttMessage((value > 22 ? "-" : "+").getBytes());
            transmitMessage.setQos(qos);
            client.publish(controlTopic, transmitMessage);
        });
    }

    @Override
    public void close() throws IOException {
        try {
            client.disconnect();
            client.close();
        } catch (MqttException e) {
            throw new IOException("Failed to disconnect or close client", e);
        }
    }
}
