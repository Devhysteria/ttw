package ttw;

import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.*;

import com.google.gson.Gson;

public class ParcDisney {
	
	private int idParc;
	private int idVilleGare;
	private int idVilleAeroport;
	private int idVille;
	private String nom;
	private String siteWeb;
	private String adresse;
//	private Gare gare1;
//	private Aeroport aeroport1;
	private Ville villeParc;

	
	public int getIdParc() {
		return idParc;
	}

	public void setIdParc(int idParc) {
		this.idParc = idParc;
	}


	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public int getIdVilleGare() {
		return idVilleGare;
	}

	public void setIdVilleGare(int idVilleGare) {
		this.idVilleGare = idVilleGare;

	}

	public int getIdVilleAeroport() {
		return idVilleAeroport;
	}

	public void setIdVilleAeroport(int idVilleAeroport) {
		this.idVilleAeroport = idVilleAeroport;
	}

	public String getSiteWeb() {
		return siteWeb;
	}

	public void setSiteWeb(String siteWeb) {
		this.siteWeb = siteWeb;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public int getIdVille() {
		return idVille;
	}

	public void setIdVille(int idVille) {
		this.idVille = idVille;
	}

//	public void newGare() throws MqttException, InterruptedException {
//		MqttApp mqtt = new MqttApp("tcp://localhost:1884", "newGare");
//		Gson gson = new Gson();
//		String request = "select * from gare where idVille = " + this.idVilleGare;
//		String json = mqtt.MqttAppSelect("tcp://localhost:1884", "app1", "select", "response", request
//                );
//        Gare gare[] = gson.fromJson(json, Gare[].class);
//        gare1 = gare[0];
//        }
//            
//	public Gare getGare1() {
//		return gare1;
//	}
//
//	public void setGare1(Gare gare1) {
//		this.gare1 = gare1;
//	}
//
//	public Aeroport getAeroport1() {
//		return aeroport1;
//	}
//
//	public void setAeroport1(Aeroport aeroport1) {
//		this.aeroport1 = aeroport1;
//	}

//	public void newAeroport() throws MqttException, InterruptedException {
//		MqttApp mqtt = new MqttApp("tcp://localhost:1884", "newAeroport");
//		Gson gson = new Gson();
//		String request = "select * from aeroport where idVille = " + this.idVilleAeroport;
//		String json = mqtt.MqttAppSelect("tcp://localhost:1884", "app2", "select", "response", request
//                );
//        Aeroport aeroport[] = gson.fromJson(json, Aeroport[].class);
//        aeroport1 = aeroport[0];
//        }

	public void newVille() throws MqttException, InterruptedException {
		MqttApp mqtt = new MqttApp("tcp://localhost:1884", "newVille");
		Gson gson = new Gson();
		String request = "select * from ville where idVille = " + this.idVille;
		String json = mqtt.MqttAppSelect("tcp://localhost:1884", "newVille", "select", "response", request
                );
        Ville ville[] = gson.fromJson(json, Ville[].class);
        villeParc = ville[0];
        villeParc.newGare();
        villeParc.newAeroport();
        }
	
	public Ville getVilleParc() {
		return villeParc;
	}

	public void setVilleParc(Ville villeParc) {
		this.villeParc = villeParc;
	}

	public ParcDisney() throws Exception {
		

		
	        
	    }}
