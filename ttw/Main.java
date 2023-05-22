package ttw;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class Main {

	public static void main(String[] args) throws Exception {

		boolean end;
		int idParcours = 0;
		Train trainOut = null;
		Train trainIn = null;
		Avion avionIn = null;
		Aeroport aeroportIn = null;
		ParcDisney parcDisney = null;
		Aeroport aeroportOut = null;

		while (end = true) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Que souhaitez-vous faire ?");
			System.out.println("1. Créer un nouveau voyage");
			System.out.println("2. Créer un nouveau parcours");
			System.out.println("3. Consulter parcours enregistré");
			int choix = scanner.nextInt();

			switch (choix) {
			case 1:
				System.out.println("Vous avez choisi de créer un nouveau voyage");
				Parcours parcours = new Parcours(1);
				break;
			case 2:
				System.out.println("Vous avez choisi de créer un nouveau parcours");
				parcours = new Parcours(2);
				break;
			case 3:
				System.out.println("Vous avez choisi de consulter un parcours enregistré");
				MqttApp mqtt = new MqttApp("tcp://localhost:1884", "main");
				String json = mqtt.MqttAppSelect("tcp://localhost:1884", "main", "select", "response",
						"SELECT * from parcours");

				if (json != null) {
					Gson gson = new Gson();
					Parcours[] parcoursselect = gson.fromJson(json, Parcours[].class);

					for (Parcours parcours1 : parcoursselect) {
						System.out.println("ID: " + parcours1.getIdParcours() + " | Nom: " + parcours1.getNom());
					}

					scanner = new Scanner(System.in);
					System.out.println("Sélectionnez un parcours (ID): ");
					int selectedId = scanner.nextInt();

					for (Parcours parcours2 : parcoursselect) {
						if (parcours2.getIdParcours() == selectedId) {
							System.out.println("Vous avez sélectionné: " + parcours2.getNom());
							idParcours = parcours2.getIdParcours();
							break;
						}

					}

					json = mqtt.MqttAppSelect("tcp://localhost:1884", "main", "select", "response",
							"SELECT * from voyage where idParcours = " + idParcours);
					if (json != null) {
						Gson gson2 = new GsonBuilder().registerTypeAdapter(Time.class, new JsonDeserializer<Time>() {
							DateFormat df = new SimpleDateFormat("HH:mm:ss");

							@Override
							public Time deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
									JsonDeserializationContext context) throws JsonParseException {
								try {
									java.util.Date date = df.parse(json.getAsString());
									return new Time(date.getTime());
								} catch (ParseException e) {
									throw new JsonParseException("Unable to parse time", e);
								}
							}
						}).create();
						Voyage[] voyages = gson.fromJson(json, Voyage[].class);
						for (int i = 0; i < voyages.length; i++) {
							json = mqtt.MqttAppSelect("tcp://localhost:1884", "main", "select", "response",
									"SELECT * from avion where idAvion =" + voyages[i].getIdAvionIn());
							if (json != null) {

								Avion[] avions = gson2.fromJson(json, Avion[].class);
								if (avions.length > 0) {
									avionIn = avions[0];
								}
							}
							json = mqtt.MqttAppSelect("tcp://localhost:1884", "main", "select", "response",
									"SELECT * from train where idTrain =" + voyages[i].getIdTrainIn());
							if (json != null) {
								gson = new Gson();
								Train[] trains = gson2.fromJson(json, Train[].class);
								if (trains.length > 0) {
									trainIn = trains[0];
								}
							}
							json = mqtt.MqttAppSelect("tcp://localhost:1884", "main", "select", "response",
									"SELECT * from train where idTrain =" + voyages[i].getIdTrainOut());
							if (json != null) {
								gson = new Gson();
								Train[] trains = gson2.fromJson(json, Train[].class);
								if (trains.length > 0) {
									trainOut = trains[0];
								}
							}
							json = mqtt.MqttAppSelect("tcp://localhost:1884", "main", "select", "response",
									"SELECT * from aeroport where idAeroport =" + avionIn.getIdAeroportIn());
							if (json != null) {
								gson = new Gson();
								Aeroport[] aeroports = gson2.fromJson(json, Aeroport[].class);
								if (aeroports.length > 0) {
									aeroportIn = aeroports[0];
								}
							}
							json = mqtt.MqttAppSelect("tcp://localhost:1884", "main", "select", "response",
									"SELECT * from parcDisney where idParc =" + voyages[i].getIdParc());
							if (json != null) {
								gson = new Gson();
								ParcDisney[] parcdisneys = gson2.fromJson(json, ParcDisney[].class);
								if (parcdisneys.length > 0) {
									parcDisney = parcdisneys[0];
								}
							}
							json = mqtt.MqttAppSelect("tcp://localhost:1884", "main", "select", "response",
									"SELECT * FROM aeroport a inner join parcDisney pd on a.idVille = pd.idVilleAeroport where idParc ="
											+ voyages[i].getIdParc());
							if (json != null) {
								gson = new Gson();
								Aeroport[] aeroports = gson2.fromJson(json, Aeroport[].class);
								if (aeroports.length > 0) {
									aeroportOut = aeroports[0];
								}
							}
							if (i == voyages.length-1) {

							} else {
								System.out.println("Etape :" + (i + 1));
								System.out.println("Départ aéroport de :" + aeroportIn.getNom() + " à "
										+ avionIn.getTimeDep() + " le " + avionIn.getDateDep() + " avion numéro : "
										+ avionIn.getCode());
								System.out.println("Arrivée aeroport de : " + aeroportOut.getNom() + " à "
										+ avionIn.getTimeArr() + " le " + avionIn.getDateArr());
								System.out.println("Navettte vers le parc de : " + parcDisney.getNom() + " à "
										+ trainIn.getTimeDep() + " le " + trainIn.getDateDep() + " train numéro : "
										+ trainIn.getCode());
								System.out.println("Navette de retour vers l'aeroport de : " + aeroportOut.getNom()
										+ " à " + trainOut.getTimeDep() + " le " + trainOut.getDateDep()
										+ " train numéro : " + trainOut.getCode());
							}

						}
					}
				}

				break;
			default:
				System.out.println("Choix invalide");
			}
			System.out.println("Voulez-vous retourner au menu principal ? (oui/non)");
			Scanner scanner2 = new Scanner(System.in);
			String reponse = scanner2.nextLine().toLowerCase();
			if (reponse.equals("oui")) {
				end = false;
			} else {
				end = true;
			}
		}

	}
}
