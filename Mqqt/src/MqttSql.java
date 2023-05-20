
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSql {

    private MqttClient client;

    public MqttSql(String brokerUrl, String clientId) throws MqttException {
        client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
        client.connect();
    }

    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        client.publish(topic, mqttMessage);
    }

    public void subscribe(String[] topics, MqttCallback callback) throws MqttException {
        client.setCallback(callback);
        client.subscribe(topics);
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }
}
