package ttw;

import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;

public class Avion extends Transport {
    private int idAvion;
    private int distanceDeVol;
    private int idAeroportIn;
    private int idAerportOut;
    
    // Getters et setters spécifiques à Avion
    public int getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(int idAvion) {
        this.idAvion = idAvion;
    }

    public int getDistanceDeVol() {
        return distanceDeVol;
    }

    public void setDistanceDeVol(int distanceDeVol) {
        this.distanceDeVol = distanceDeVol;
    }

    public int getIdAeroportIn() {
        return idAeroportIn;
    }

    public void setIdAeroportIn(int idAeroportIn) {
        this.idAeroportIn = idAeroportIn;
    }

    public int getIdAerportOut() {
        return idAerportOut;
    }

    public void setIdAerportOut(int idAerportOut) {
        this.idAerportOut = idAerportOut;
    }
}