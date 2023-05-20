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
	private Date dateDep;
	private Time timeDep;
	private Date dateArr;
	private Time timeArr;
	private String code;
	

	public int getIdAvion() {
		return idAvion;
	}


	public void setIdAvion(int idAvion) {
		this.idAvion = idAvion;
	}


	public int getDistVol() {
		return distanceDeVol;
	}


	public void setDistVol(int distVol) {
		this.distanceDeVol = distVol;
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


	public Date getDateDep() {
		return dateDep;
	}


	public void setDateDep(Date dateDep) {
		this.dateDep = dateDep;
	}


	public Time getTimeDep() {
		return timeDep;
	}


	public void setTimeDep(Time timeDep) {
		this.timeDep = timeDep;
	}


	public Date getDateArr() {
		return dateArr;
	}


	public void setDateArr(Date dateArr) {
		this.dateArr = dateArr;
	}


	public Time getTimeArr() {
		return timeArr;
	}


	public void setTimeArr(Time timeArr) {
		this.timeArr = timeArr;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public Avion(int idVilleIn, int idVilleOut) throws MqttException {
		super(idVilleIn, idVilleOut);
		
	}

}
