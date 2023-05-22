package ttw;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttApp {

    private MqttClient client;
    private String json;
    private Object lock = new Object(); // Créer un objet pour verrouiller
    private int qos = 1;

    public MqttApp(String brokerUrl, String clientId) throws MqttException {
        client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
        client.connect();
    }
    
    public String MqttAppSelect(String brokerUrl, String clientId, String topicIn, String topicOut, String request ) throws MqttException, InterruptedException {
        // Abonnement avant de publier le message
        subscribeAndWait(topicOut);

        // Publication du message
        publish(topicIn, request);
        
        // Attendre jusqu'à ce que le message soit reçu
        synchronized (lock) {
            lock.wait();
        }

        // Le message a été reçu, retourner le contenu de la variable json
        return json;
    } 

    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        client.publish(topic, mqttMessage);
    }

    public void subscribeAndWait(String topicOut) throws MqttException, InterruptedException {
        client.subscribe(topicOut, new IMqttMessageListener() {
           
            
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                json = message.toString();
                // Notifier que le message est reçu
                synchronized (lock) {
                    lock.notify();
                }
            }
        });
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }
}


