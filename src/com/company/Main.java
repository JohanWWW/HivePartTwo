package com.company;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

    public static void main(String[] args) {
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "clientId-JJS2";
        String temperatureTopic = "temperature-topic/1";
        String controlTopic = "control-topic/2";
        int qos = 2;

        try {
            var controller = new TemperatureController(broker, clientId, temperatureTopic, controlTopic, qos);
            controller.begin();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
