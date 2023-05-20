import org.eclipse.paho.client.mqttv3.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



public class MainSql {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ttw";
    private static final String DB_USER = "ttw";
    private static final String DB_PASSWORD = "root";
    private static final String MQTT_TOPIC = "response";
    private static final String MQTT_BROKER = "tcp://localhost:1884";
    private static final String MQTT_CLIENT_ID = "myclientid";
    private static final String MQTT_CLIENT_ID1 = "myclientid1";
    

    public static void main(String[] args) throws MqttException {
        MqttSql mqttSql = new MqttSql(MQTT_BROKER, MQTT_CLIENT_ID);
        MqttSql mqttSql1 = new MqttSql(MQTT_BROKER, MQTT_CLIENT_ID1);
        String[] topics = {"select", "insertParcours"};
        final int idParcours;
        String response;

        mqttSql.subscribe(topics, new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {}

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                Statement statement = connection.createStatement();

                if(topic.equals("select") ) {
                    String request = message.toString();
                    System.out.println("ok" + request);
                    ResultSet resultSet = statement.executeQuery(request);
                    JsonArray jsonArray = new JsonArray();
                    while (resultSet.next()) {
                        JsonObject jsonObject = new JsonObject();
                        int total_columns = resultSet.getMetaData().getColumnCount();
                        for (int i = 1; i <= total_columns; i++) {
                            jsonObject.addProperty(resultSet.getMetaData().getColumnLabel(i), resultSet.getString(i));
                        }
                        jsonArray.add(jsonObject);
                    }
                    String json = jsonArray.toString();

                    mqttSql.publish(MQTT_TOPIC, json);
                }
                if(topic.equals("insertParcours") ) {
                    String request = message.toString();
                    String json;
                    System.out.println("insert    " + request);
                    String result ;
                    int rowsAffected = statement.executeUpdate(request);
                    if (rowsAffected > 0) {
                    	request = "SELECT * FROM parcours ORDER BY idParcours DESC LIMIT 1 ";
                    	ResultSet resultSet = statement.executeQuery(request);
                        JsonArray jsonArray = new JsonArray();
                        while (resultSet.next()) {
                            JsonObject jsonObject = new JsonObject();
                            int total_columns = resultSet.getMetaData().getColumnCount();
                            for (int i = 1; i <= total_columns; i++) {
                                jsonObject.addProperty(resultSet.getMetaData().getColumnLabel(i), resultSet.getString(i));
                            }
                            jsonArray.add(jsonObject);
                        }
                        json = jsonArray.toString();
                    } else {
                        json = "Null1";
                    }
                    mqttSql.publish(MQTT_TOPIC, json);
                    }                  
                

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });}

}
