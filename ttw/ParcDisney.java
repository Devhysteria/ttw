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
