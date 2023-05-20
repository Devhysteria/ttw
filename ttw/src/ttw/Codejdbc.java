package ttw;
import java.sql.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Codejdbc {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ttw";
    private static final String DB_USER = "ttw";
    private static final String DB_PASSWORD = "root";
    private static final String MQTT_TOPIC = "mytopic";
    private static final String MQTT_BROKER = "tcp://localhost:1884";
    private static final String MQTT_CLIENT_ID = "myclientid";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select nom from ville");

            MqttClient mqttClient = new MqttClient(MQTT_BROKER, MQTT_CLIENT_ID, new MemoryPersistence());
           mqttClient.connect();

            while (resultSet.next()) {
                String data = resultSet.getString("idAvion");
                MqttMessage message = new MqttMessage(data.getBytes());
                mqttClient.publish(MQTT_TOPIC, message);
            }

            mqttClient.disconnect();
            statement.close();
            connection.close();

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }
}
