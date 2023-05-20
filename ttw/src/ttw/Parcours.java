package ttw;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;

public class Parcours {
	@SerializedName("IdParcours")
	private int idParcours;
	@SerializedName("nom")
	private String nom;

	public Parcours(int index) throws Exception {

		Ville firstVille = null;
		Voyage voyage;
		ArrayList<Voyage> voyages = new ArrayList<>();
		MqttApp mqtt = new MqttApp("tcp://localhost:1884", "app");
		Voyage lastVoyage = null;
		Avion avionOut = null;
		Scanner scanner = new Scanner(System.in);

		System.out.print("Veuillez taper le nom de votre parcours : ");
		nom = scanner.nextLine();

		System.out.println("Le nom saisi est : " + nom);

		String json = mqtt.MqttAppSelect("tcp://localhost:1884", "app", "select", "response",
				"SELECT ville.idVille, ville.nom, ville.pays from ville inner join aeroport on aeroport.idVille = ville.idVille");

		if (json != null) {
			Gson gson = new Gson();
			Ville[] villes = gson.fromJson(json, Ville[].class);

			for (Ville ville : villes) {
				System.out.println(
						"ID: " + ville.getIdVille() + " | Nom: " + ville.getName() + "| Pays: " + ville.getPays());
			}

			scanner = new Scanner(System.in);
			System.out.println("Sélectionnez une ville de départ (ID): ");
			int selectedId = scanner.nextInt();

			for (Ville ville : villes) {
				if (ville.getIdVille() == selectedId) {
					System.out.println("Vous avez sélectionné: " + ville.getName());

					firstVille = ville;
					break;
				}
			}
		}

		if (firstVille != null) {
			if (index == 1) {
				voyage = new Voyage(firstVille, 1, lastVoyage);

				boolean enregistrer = false;
				while (!enregistrer) {
					System.out.println("Voici le récapitulatif de votre voyage : ");
					System.out.println("- Jour de départ à l'aéroport : " + firstVille.getAeroportIN().getNom() + " à "
							+ voyage.getAvionIn().getTimeDep() + " le " + voyage.getAvionIn().getDateDep());
					System.out.println(
							", navette à l'aéroport de " + voyage.getParc().getVilleParc().getAeroportIN().getNom()
									+ " vers " + voyage.getParc().getNom() + " le " + voyage.getTrainIn().getDateArr()
									+ ". Arrivée au parc à " + voyage.getTrainIn().getTimeArr() + ".");
					System.out.println("- Jour du retour à la gare de "
							+ voyage.getParc().getVilleParc().getGareIN().getNom() + " à "
							+ voyage.getTrainOut().getTimeDep() + " le " + voyage.getAvionOut().getDateDep());
					System.out.println(", retour vers l'aéroport de "
							+ voyage.getParc().getVilleParc().getAeroportIN().getNom() + ", vol retour à "
							+ voyage.getAvionOut().getTimeDep() + " le " + voyage.getAvionOut().getDateDep()
							+ ". Vous arriverez à la ville à " + voyage.getAvionOut().getTimeArr() + ".");

					System.out.println("Que voulez-vous faire ?");
					System.out.println("1. Info aéroport In");
					System.out.println("2. Info aéroport Out");
					System.out.println("3. Info gare In");
					System.out.println("4. Info gare Out");
					System.out.println("5. Info parc");
					System.out.println("6. Enregistrer");

					scanner = new Scanner(System.in);
					int choix = scanner.nextInt();
					switch (choix) {
					case 1:
						System.out.println("Vous avez choisi l'option 1 : Info aéroport Parc Disney");
						System.out.println(
								"Nom de l'aéroport : " + voyage.getParc().getVilleParc().getAeroportIN().getNom());
						System.out.println("Pays : " + voyage.getParc().getVilleParc().getAeroportIN().getPays());
						System.out.println(
								"Site Internet : " + voyage.getParc().getVilleParc().getAeroportIN().getSiteWeb());
						System.out.println("Appuyez sur Enter pour continuer...");
						scanner = new Scanner(System.in);
						scanner.nextLine();
						break;

					case 2:
						System.out.println("Vous avez choisi l'option 2 : Info aéroport Out");
						System.out.println("Nom de l'aéroport : " + firstVille.getAeroportIN().getNom());
						System.out.println("Pays : " + firstVille.getAeroportIN().getPays());
						System.out.println("Site Internet : " + firstVille.getAeroportIN().getSiteWeb());
						System.out.println("Appuyez sur Enter pour continuer...");
						scanner = new Scanner(System.in);
						scanner.nextLine();
						break;
					case 3:
						System.out.println("Vous avez choisi l'option 3 : Info gare Parc Disney");
						System.out.println("Nom de la gare : " + voyage.getParc().getVilleParc().getGareIN().getNom());
						System.out.println("Pays : " + voyage.getParc().getVilleParc().getGareIN().getPays());
						System.out
								.println("Site internet : " + voyage.getParc().getVilleParc().getGareIN().getSiteWeb());
						System.out.println("Appuyez sur Enter pour continuer...");
						scanner = new Scanner(System.in);
						scanner.nextLine();
						break;
					case 4:
						System.out.println("Vous avez choisi l'option 4 : Info gare Out");
						System.out.println("Nom de la gare : " + firstVille.getGareIN().getNom());
						System.out.println("Pays : " + firstVille.getGareIN().getPays());
						System.out.println("Site internet : " + firstVille.getGareIN().getSiteWeb());
						System.out.println("Appuyez sur Enter pour continuer...");
						scanner = new Scanner(System.in);
						scanner.nextLine();
						break;
					case 5:
						System.out.println("Vous avez choisi l'option 5 : Info parc");
						System.out.println("Nom du Parc Disney : " + voyage.getParc().getNom());
						System.out.println("Site internet : " + voyage.getParc().getSiteWeb());
						System.out.println("Adresse : " + voyage.getParc().getAdresse());
						System.out.println("Appuyez sur Enter pour continuer...");
						scanner = new Scanner(System.in);
						scanner.nextLine();
						break;
					case 6:
						System.out.println("Vous avez choisi l'option 6 : Enregistrer");
						mqtt = new MqttApp("tcp://localhost:1884", "newInsert");
						String json2;
						json2 = mqtt.MqttAppSelect("tcp://localhost:1884", "newInsert", "insertParcours", "response",
								"INSERT INTO `parcours`( `nom`) VALUES (\"" + nom + "\")");

						if (json2 != null) {
							JsonArray jsonArray = JsonParser.parseString(json2).getAsJsonArray();
							if (jsonArray.size() > 0) {
								JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
								idParcours = jsonObject.get("IdParcours").getAsInt();
							}

							// json = idParcours;
							enregistrer = true;
							json = mqtt.MqttAppSelect("tcp://localhost:1884", "newInsert", "insertParcours", "response",
									"INSERT INTO `voyage`( `idParcours`, `idParc`, `nom`, `idAvionIn`, `idavionOut`, `idTrainIn`, `idTrainOut`) VALUES ('"
											+ idParcours + "','" + voyage.getParc().getIdParc() + "','"
											+ voyage.getNomVoyage() + "','" + voyage.getAvionIn().getIdAvion() + "','"
											+ voyage.getAvionOut().getIdAvion() + "','"
											+ voyage.getTrainIn().getIdTrain() + "','"
											+ voyage.getTrainOut().getIdTrain() + "')");
						}

						break;
					default:
						System.out.println("Le choix que vous avez entré n'est pas valide.");
						break;
					}
				}
			}
			if (index == 2) {
				boolean enregistrer = false;
				Ville lastVille = firstVille;
				while (!enregistrer) {

					int indexVoyage = 0;
					voyage = new Voyage(lastVille, 2, lastVoyage);
					voyages.add(voyage);
					lastVoyage = voyage;
					System.out.println("Voulez-vous ajouter une étape ? (oui/non)");
					Scanner scanner2 = new Scanner(System.in);
					String reponse = scanner2.nextLine().toLowerCase();

					if (reponse.equals("oui")) {
						System.out.println("Vous avez choisi de continuer.");
						lastVille = voyage.getParc().getVilleParc();
						boolean next = false;
						while (!next) {
							System.out.println("Voici le récapitulatif de votre voyage : ");
							System.out.println("- Jour de départ à l'aéroport : " + firstVille.getAeroportIN().getNom()
									+ " à " + voyage.getAvionIn().getTimeDep() + " le "
									+ voyage.getAvionIn().getDateDep());
							System.out.println(", navette à l'aéroport de "
									+ voyage.getParc().getVilleParc().getAeroportIN().getNom() + " vers "
									+ voyage.getParc().getNom() + " le " + voyage.getTrainIn().getDateArr()
									+ ". Arrivée au parc à " + voyage.getTrainIn().getTimeArr() + ".");
							System.out.println("- Jour du retour à la gare de "
									+ voyage.getParc().getVilleParc().getGareIN().getNom() + " à "
									+ voyage.getTrainOut().getTimeDep() + " le " + voyage.getTrainOut().getDateDep());
							System.out.println(", retour vers l'aéroport de "
									+ voyage.getParc().getVilleParc().getAeroportIN().getNom());

							System.out.println("Que voulez-vous faire ?");
							System.out.println("1. Info aéroport In");
							System.out.println("2. Info aéroport Out");
							System.out.println("3. Info gare In");
							System.out.println("4. Info gare Out");
							System.out.println("5. Info parc");
							System.out.println("6. Suivant");

							scanner = new Scanner(System.in);
							int choix = scanner.nextInt();
							switch (choix) {
							case 1:
								System.out.println("Vous avez choisi l'option 1 : Info aéroport Parc Disney");
								System.out.println("Nom de l'aéroport : " + firstVille.getAeroportIN().getNom());
								System.out.println("Pays : " + firstVille.getAeroportIN().getPays());
								System.out.println("Site Internet : " + firstVille.getAeroportIN().getSiteWeb());
								System.out.println("Appuyez sur Enter pour continuer...");
								scanner = new Scanner(System.in);
								scanner.nextLine();
								break;

							case 2:
								System.out.println("Vous avez choisi l'option 2 : Info aéroport Out");
								System.out.println("Nom de l'aéroport : "
										+ voyage.getParc().getVilleParc().getAeroportIN().getNom());
								System.out
										.println("Pays : " + voyage.getParc().getVilleParc().getAeroportIN().getPays());
								System.out.println("Site Internet : "
										+ voyage.getParc().getVilleParc().getAeroportIN().getSiteWeb());
								System.out.println("Appuyez sur Enter pour continuer...");
								scanner = new Scanner(System.in);
								scanner.nextLine();
								break;
							case 3:
								System.out.println("Vous avez choisi l'option 3 : Info gare Parc Disney");
								System.out.println(
										"Nom de la gare : " + voyage.getParc().getVilleParc().getGareIN().getNom());
								System.out.println("Pays : " + voyage.getParc().getVilleParc().getGareIN().getPays());
								System.out.println(
										"Site internet : " + voyage.getParc().getVilleParc().getGareIN().getSiteWeb());
								System.out.println("Appuyez sur Enter pour continuer...");
								scanner = new Scanner(System.in);
								scanner.nextLine();
								break;
							case 4:
								System.out.println("Vous avez choisi l'option 4 : Info gare Out");
								System.out.println("Nom de la gare : " + firstVille.getGareIN().getNom());
								System.out.println("Pays : " + firstVille.getGareIN().getPays());
								System.out.println("Site internet : " + firstVille.getGareIN().getSiteWeb());
								System.out.println("Appuyez sur Enter pour continuer...");
								scanner = new Scanner(System.in);
								scanner.nextLine();
								break;
							case 5:
								System.out.println("Vous avez choisi l'option 5 : Info parc");
								System.out.println("Nom du Parc Disney : " + voyage.getParc().getNom());
								System.out.println("Site internet : " + voyage.getParc().getSiteWeb());
								System.out.println("Adresse : " + voyage.getParc().getAdresse());
								System.out.println("Appuyez sur Enter pour continuer...");
								scanner = new Scanner(System.in);
								scanner.nextLine();
								break;
							case 6:
								System.out.println("Vous avez choisi l'option 6 : Suivant");
								mqtt = new MqttApp("tcp://localhost:1884", "newInsert");
								next = true;

								break;
							default:
								System.out.println("Le choix que vous avez entré n'est pas valide.");
								break;
							}
						}
					} else if (reponse.equals("non")) {
						System.out.println("Vous avez choisi de ne pas continuer.");
						if (!firstVille.getName().equals(voyage.getParc().getVilleParc().getNom())) {
							Gson gson = new Gson();
							String dateString = voyages.get(voyages.size() - 1).getTrainOut().getDateArr().toString();
							DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
									Locale.ENGLISH);
							DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date date = inputFormat.parse(dateString);
							String formattedDate = outputFormat.format(date);
							gson = new GsonBuilder().registerTypeAdapter(Time.class, new JsonDeserializer<Time>() {
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
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(outputFormat.parse(formattedDate));
							calendar.add(Calendar.DATE, 3);
							Date newDate = calendar.getTime();
							String formattedDate2 = outputFormat.format(newDate);

							String request = "SELECT avion.idAvion, avion.distanceDeVol, aeroport_in.idville AS idAeroportIn, aeroport_out.idville AS idAeroportOut, avion.dateDep, avion.timeDep, avion.dateArr, avion.timeArr,avion.code \n"
									+ "FROM avion \n"
									+ "INNER JOIN aeroport AS aeroport_in ON avion.idaeroportin = aeroport_in.idAeroport \n"
									+ "INNER JOIN aeroport AS aeroport_out ON avion.idaeroportout = aeroport_out.idAeroport \n"
									+ "INNER JOIN ville AS ville_in ON aeroport_in.idville = ville_in.idVille \n"
									+ "INNER JOIN ville AS ville_out ON aeroport_out.idville = ville_out.idVille WHERE dateDep >="
									+ "\"" + formattedDate + " \"and idaeroportIn ="
									+ voyage.getParc().getIdVilleAeroport() + " and dateDep <= \"" + formattedDate2
									+ "\"" + " and idaeroportOut =" + firstVille.getIdVille();
							mqtt = new MqttApp("tcp://localhost:1884", "LastTravel");
							json = mqtt.MqttAppSelect("tcp://localhost:1884", "LastTravel", "select", "response",
									request);
							Avion[] avions = gson.fromJson(json, Avion[].class);
							for (Avion avion : avions) {
								System.out.println("ID: " + avion.getIdAvion() + " | Date: " + avion.getDateDep()
										+ " | heure: " + avion.getTimeDep());
							}
							System.out.println("Sélectionnez un vol de départ(ID): ");
							int selectedId = scanner.nextInt();
							for (Avion avion : avions) {
								if (avion.getIdAvion() == selectedId) {
									System.out.println("Vous avez sélectionné: " + avion.getCode() + " du : "
											+ avion.getDateDep() + " à :" + avion.getTimeDep());
									avionOut = avion;
									break;
								}
							}

							break;
						}
						break;
					} else {
						System.out.println("Réponse invalide.");
					}
					indexVoyage++;
				}

				System.out.println("Récapitulatif du parcours :");
				for (int i = 0; i < voyages.size(); i++) {
					System.out.println("Etape :" + (i + 1));
					System.out.println("Départ aéroport de :" + voyages.get(i).getVilleIn().getAeroportIN().getNom()
							+ " à " + voyages.get(i).getAvionIn().getTimeDep() + " le "
							+ voyages.get(i).getAvionIn().getDateDep() + " avion numéro : "
							+ voyages.get(i).getAvionIn().getCode());
					System.out.println(
							"Arrivée aeroport de : " + voyages.get(i).getParc().getVilleParc().getAeroportIN().getNom()
									+ " à " + voyages.get(i).getAvionIn().getTimeArr() + " le "
									+ voyages.get(i).getAvionIn().getDateArr());
					System.out.println("Navettte vers le parc de : " + voyages.get(i).getParc().getNom() + " à "
							+ voyages.get(i).getTrainIn().getTimeDep() + " le "
							+ voyages.get(i).getTrainIn().getDateDep() + " train numéro : "
							+ voyages.get(i).getTrainIn().getCode());
					System.out.println("Navette de retour vers l'aeroport de : "
							+ voyages.get(i).getParc().getVilleParc().getAeroportIN().getNom() + " à "
							+ voyages.get(i).getTrainOut().getTimeDep() + " le "
							+ voyages.get(i).getTrainOut().getDateDep() + " train numéro : "
							+ voyages.get(i).getTrainOut().getCode());
				}
				System.out.println("Voulez vous enregistrer votre parcours ? (oui/non)");
				Scanner scanner2 = new Scanner(System.in);
				String reponse = scanner2.nextLine().toLowerCase();
				if (reponse.equals("oui")) {
					mqtt = new MqttApp("tcp://localhost:1884", "newInsert");
					String json2;
					json2 = mqtt.MqttAppSelect("tcp://localhost:1884", "newInsert", "insertParcours", "response",
							"INSERT INTO `parcours`( `nom`) VALUES (\"" + nom + "\")");

					if (json2 != null) {
						JsonArray jsonArray = JsonParser.parseString(json2).getAsJsonArray();
						if (jsonArray.size() > 0) {
							JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
							idParcours = jsonObject.get("IdParcours").getAsInt();
						}

						// json = idParcours;
						enregistrer = true;
						for (int i = 0; i < voyages.size(); i++) {
							json = mqtt.MqttAppSelect("tcp://localhost:1884", "newInsert", "insertParcours", "response",
									"INSERT INTO `voyage`( `idParcours`, `idParc`, `nom`, `idAvionIn`, `idavionOut`, `idTrainIn`, `idTrainOut`) VALUES ('"
											+ idParcours + "','" + voyages.get(i).getParc().getIdParc() + "','"
											+ voyages.get(i).getNomVoyage() + "','"
											+ voyages.get(i).getAvionIn().getIdAvion() + "','"
											+ voyages.get(i).getAvionIn().getIdAvion() + "','"
											+ voyages.get(i).getTrainIn().getIdTrain() + "','"
											+ voyages.get(i).getTrainOut().getIdTrain() + "')");
							if ((i + 1) == voyages.size()) {
								json = mqtt.MqttAppSelect("tcp://localhost:1884", "newInsert", "insertParcours",
										"response",
										"INSERT INTO `voyage`( `idParcours`, `idParc`, `nom`, `idAvionIn`, `idavionOut`, `idTrainIn`, `idTrainOut`) VALUES ('"
												+ idParcours + "','" + voyages.get(i).getParc().getIdParc() + "','"
												+ voyages.get(i).getNomVoyage() + "','"
												+ voyages.get(i).getAvionIn().getIdAvion() + "','"
												+ avionOut.getIdAvion() + "','"
												+ voyages.get(i).getTrainIn().getIdTrain() + "','"
												+ voyages.get(i).getTrainOut().getIdTrain() + "')");
							}

						}

					}
				}
			}
		}
	}

	public int getIdParcours() {
		return idParcours;
	}

	public void setIdParcours(int idParcours) {
		this.idParcours = idParcours;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}
