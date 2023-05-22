package ttw;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;

public class Ville {
	private int idVille;
	private String nom;
	private String pays;
	private Gare gareIN;
	private Aeroport aeroportIN;
	
	public String getPays() {
		return pays;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Gare getGareIN() {
		return gareIN;
	}

	public void setGareIN(Gare gareIN) {
		this.gareIN = gareIN;
	}

	public Aeroport getAeroportIN() {
		return aeroportIN;
	}

	public void setAeroportIN(Aeroport aeroportIN) {
		this.aeroportIN = aeroportIN;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public int getIdVille() {
		return idVille;
	}
	
	public void setIdVille(int id) {
		this.idVille = id;
	}
	
	public String getName() {
		return nom;
	}
	
	public void setName(String name) {
		this.nom = name;
	}
	
	public void newGare() throws MqttException, InterruptedException {
		MqttApp mqtt = new MqttApp("tcp://localhost:1884", "gareIN");
		Gson gson = new Gson();
		String request = "select * from gare where idVille = " + this.idVille;
		String json = mqtt.MqttAppSelect("tcp://localhost:1884", "app1", "select", "response", request
                );
        Gare gare[] = gson.fromJson(json, Gare[].class);
        gareIN = gare[0];
        }
	
	public void newAeroport() throws MqttException, InterruptedException {
		MqttApp mqtt = new MqttApp("tcp://localhost:1884", "aeroportIN");
		Gson gson = new Gson();
		String request = "select * from aeroport where idVille = " + this.idVille;
		String json = mqtt.MqttAppSelect("tcp://localhost:1884", "app2", "select", "response", request
                );
        Aeroport aeroport[] = gson.fromJson(json, Aeroport[].class);
        aeroportIN = aeroport[0];
        }
}
