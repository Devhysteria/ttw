package ttw;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Voyage {

	private int idVoyage;
	private int idParcours;
	private int idParc;
	private String nom;
	private int idAvionIn;
	private int idAvionOut;
	private int idTrainIn;
	private int idTrainOut;


	private ParcDisney parc;
    private Avion avionIn;
    private Avion avionOut;
    private Train trainIn;
    private Train trainOut;
    private Ville villeIn;


	public Voyage(Ville firstVille, int index, Voyage lastVoyage) throws Exception {


        MqttApp mqtt = new MqttApp("tcp://localhost:1884", "app");
        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);
        String json;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
    	LocalDateTime dateTime = LocalDateTime.parse(formattedDate + "T00:00:00"); 
        json = mqtt.MqttAppSelect("tcp://localhost:1884", "app", "select", "response",
                "select idParc, idVilleGare, idVilleAeroport, idVille, nom, siteWeb, adresse from parcDisney");
        ParcDisney[] parcdisney = gson.fromJson(json, ParcDisney[].class);
        for (ParcDisney parcDisney : parcdisney) {
        	if(parcDisney.getIdVilleAeroport() != firstVille.getIdVille()) {
        		System.out.println("ID: " + parcDisney.getIdParc() + " | Nom: " + parcDisney.getNom());
        	}
            
        }
        System.out.println("Sélectionnez le parc que vous désirez visiter (ID): ");
        int selectedId = scanner.nextInt();
        for (ParcDisney parcDisney : parcdisney) {
            if (parcDisney.getIdParc() == selectedId) {
                System.out.println("Vous avez sélectionné: " + parcDisney.getNom());
                parc = parcDisney;
                firstVille.newGare();
                firstVille.newAeroport();
                parc.newVille();
                villeIn = firstVille  ;
                nom = villeIn.getAeroportIN().getNom() + "-" + parcDisney.getNom() + "(" + parcDisney.getAdresse() + ")";
                break;
            }
        }
        if (parc != null) {
        	String request;
        	if(index == 1 || lastVoyage == null) {
        		request = "SELECT avion.idAvion, avion.distanceDeVol, aeroport_in.idville AS idAeroportIn, aeroport_out.idville AS idAeroportOut, avion.dateDep, avion.timeDep, avion.dateArr, avion.timeArr,avion.code "
                		+ "FROM avion "
                		+ "INNER JOIN aeroport AS aeroport_in ON avion.idaeroportin = aeroport_in.idAeroport "
                		+ "INNER JOIN aeroport AS aeroport_out ON avion.idaeroportout = aeroport_out.idAeroport "
                		+ "INNER JOIN ville AS ville_in ON aeroport_in.idville = ville_in.idVille "
                		+ "INNER JOIN ville AS ville_out ON aeroport_out.idville = ville_out.idVille "
                		+ "WHERE dateDep >=" + "\"" + formattedDate + "\" and idAeroportIN ="
                        + firstVille.getIdVille() + " and idAeroportOut =" + parc.getIdVille();
        	}else {
            	String dateString = lastVoyage.getTrainOut().getDateArr().toString();
            	DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            	DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            	Date date = inputFormat.parse(dateString);
            	formattedDate = outputFormat.format(date);
        		request = "SELECT avion.idAvion, avion.distanceDeVol, aeroport_in.idville AS idAeroportIn, aeroport_out.idville AS idAeroportOut, avion.dateDep, avion.timeDep, avion.dateArr, avion.timeArr,avion.code "
                		+ "FROM avion "
                		+ "INNER JOIN aeroport AS aeroport_in ON avion.idaeroportin = aeroport_in.idAeroport "
                		+ "INNER JOIN aeroport AS aeroport_out ON avion.idaeroportout = aeroport_out.idAeroport "
                		+ "INNER JOIN ville AS ville_in ON aeroport_in.idville = ville_in.idVille "
                		+ "INNER JOIN ville AS ville_out ON aeroport_out.idville = ville_out.idVille "
                		+ "WHERE dateDep >=" + "\"" + formattedDate + "\" and idAeroportIN ="
                        + firstVille.getIdVille() + " and idAeroportOut =" + parc.getIdVille();
        	}

            json = mqtt.MqttAppSelect("tcp://localhost:1884", "app", "select", "response", request);
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
            Avion[] avions = gson.fromJson(json, Avion[].class);
            for (Avion avion : avions) {
                System.out.println("ID: " + avion.getIdAvion() + " | Date: " + avion.getDateDep()+ " | heure: " + avion.getTimeDep());
            }
            System.out.println("Sélectionnez un vol de départ(ID): ");
            selectedId = scanner.nextInt();
            for (Avion avion : avions) {
                if (avion.getIdAvion() == selectedId) {
                    System.out.println("Vous avez sélectionné: " + avion.getCode() + " du : " + avion.getDateDep() + " à :" + avion.getTimeDep() );
                    avionIn = avion;                   
                    break;
                }
            }
        }

        if(avionIn != null) {
        	String dateString = avionIn.getDateArr().toString();
        	DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        	DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        	Date date = inputFormat.parse(dateString);
        	formattedDate = outputFormat.format(date);
        	
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(outputFormat.parse(formattedDate));
        	calendar.add(Calendar.DATE, 2);
        	Date newDate = calendar.getTime();
        	String formattedDate2 = outputFormat.format(newDate);

        	String request = "SELECT train.idTrain, train.distance, gare_in.idville AS idGareIn, gare_out.idville AS idGareOut, train.dateDep, train.timeDep, train.dateArr, train.timeArr,train.code \n"
        			+ "            		FROM train \n"
        			+ "            		INNER JOIN gare AS gare_in ON train.idGareIn = gare_in.idGare \n"
        			+ "            		INNER JOIN gare AS gare_out ON train.idGareOut = gare_out.idGare \n"
        			+ "            		INNER JOIN ville AS ville_in ON gare_in.idville = ville_in.idVille \n"
        			+ "            		INNER JOIN ville AS ville_out ON gare_out.idville = ville_out.idVille  WHERE dateDep >=" + "\"" + formattedDate + "\" and timeDep >="  + "\"" +
        			avionIn.getTimeArr() + " \"and idGareIn =" + parc.getIdVilleGare() + " and dateDep <= \"" + formattedDate2 + "\"";           
            json = mqtt.MqttAppSelect("tcp://localhost:1884", "app", "select", "response", request);
            Train[] trains = gson.fromJson(json, Train[].class);
            for (Train train : trains) {
                System.out.println("ID: " + train.getIdTrain() + " | Date: " + train.getDateDep()+ " | heure: " + train.getTimeDep());
            }
            System.out.println("Sélectionnez un train de départ(ID): ");
            selectedId = scanner.nextInt();
            for (Train train : trains) {
                if (train.getIdTrain() == selectedId) {
                    System.out.println("Vous avez sélectionné: " + train.getCode() + " du : " + train.getDateDep() + " à :" + train.getTimeDep() + ", vous arriverez à " + parc.getAdresse() + " le " + train.getDateArr() + " à " + train.getTimeArr() );
                    trainIn = train;                   
                    break;
                }
            }
        }
        if(trainIn != null ) {


        	String dateString = trainIn.getDateArr().toString();
        	DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        	DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        	Date date = inputFormat.parse(dateString);
        	formattedDate = outputFormat.format(date);
        	
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(outputFormat.parse(formattedDate));
        	calendar.add(Calendar.DATE, 5);
        	Date newDate = calendar.getTime();
        	String formattedDate2 = outputFormat.format(newDate);     	        	

        	String request = "SELECT train.idTrain, train.distance, gare_in.idville AS idGareIn, gare_out.idville AS idGareOut, train.dateDep, train.timeDep, train.dateArr, train.timeArr,train.code \n"
        			+ "            		FROM train \n"
        			+ "            		INNER JOIN gare AS gare_in ON train.idGareIn = gare_in.idGare \n"
        			+ "            		INNER JOIN gare AS gare_out ON train.idGareOut = gare_out.idGare \n"
        			+ "            		INNER JOIN ville AS ville_in ON gare_in.idville = ville_in.idVille \n"
        			+ "            		INNER JOIN ville AS ville_out ON gare_out.idville = ville_out.idVille WHERE dateDep >=" + "\"" + formattedDate + " \"and ville_in.idVille =" + parc.getIdVilleGare() + " and dateDep <= \"" + formattedDate2 + "\"" + " and ville_out.idVille =" + parc.getIdVilleAeroport() ;

            json = mqtt.MqttAppSelect("tcp://localhost:1883", "app", "select", "response", request);

            Train[] trains = gson.fromJson(json, Train[].class);
            for (Train train : trains) {
                System.out.println("ID: " + train.getIdTrain() + " | Date: " + train.getDateDep()+ " | heure: " + train.getTimeDep());
            }

            System.out.println("Sélectionnez un train de retour(ID): ");
            selectedId = scanner.nextInt();
            for (Train train : trains) {
                if (train.getIdTrain() == selectedId) {
                    System.out.println("Vous avez sélectionné: " + train.getCode() + " du : " + train.getDateDep() + " à :" + train.getTimeDep() );
                    trainOut = train;                   
                    break;
                }
            }
        }
        if (trainOut != null && index == 1) {

        	String dateString = trainOut.getDateArr().toString();
        	DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        	DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        	Date date = inputFormat.parse(dateString);
        	formattedDate = outputFormat.format(date);
        	
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
        			+ "INNER JOIN ville AS ville_out ON aeroport_out.idville = ville_out.idVille WHERE dateDep >=" + "\"" + formattedDate + " \"and idaeroportIn =" + parc.getIdVilleAeroport() + " and dateDep <= \"" + formattedDate2 + "\"" + " and idaeroportOut =" + firstVille.getIdVille() ;

            json = mqtt.MqttAppSelect("tcp://localhost:1884", "app", "select", "response", request);
            Avion[] avions = gson.fromJson(json, Avion[].class);
            for (Avion avion : avions) {
                System.out.println("ID: " + avion.getIdAvion() + " | Date: " + avion.getDateDep()+ " | heure: " + avion.getTimeDep());
            }
            System.out.println("Sélectionnez un vol de départ(ID): ");
            selectedId = scanner.nextInt();
            for (Avion avion : avions) {
                if (avion.getIdAvion() == selectedId) {
                    System.out.println("Vous avez sélectionné: " + avion.getCode() + " du : " + avion.getDateDep() + " à :" + avion.getTimeDep());
                    avionOut = avion;                   
                    break;
                }
            }
        }

	}


	public Ville getVilleIn() {
		return villeIn;
	}


	public void setVilleIn(Ville villeIn) {
		this.villeIn = villeIn;
	}


	public ParcDisney getParc() {
		return parc;
	}


	public void setParc(ParcDisney parc) {
		this.parc = parc;
	}


	public Avion getAvionIn() {
		return avionIn;
	}


	public void setAvionIn(Avion avionIn) {
		this.avionIn = avionIn;
	}


	public Avion getAvionOut() {
		return avionOut;
	}


	public void setAvionOut(Avion avionOut) {
		this.avionOut = avionOut;
	}


	public Train getTrainIn() {
		return trainIn;
	}


	public void setTrainIn(Train trainIn) {
		this.trainIn = trainIn;
	}


	public Train getTrainOut() {
		return trainOut;
	}


	public void setTrainOut(Train trainOut) {
		this.trainOut = trainOut;
	}


	public String getNomVoyage() {
		return nom;
	}


	public void setNomVoyage(String nomVoyage) {
		this.nom = nomVoyage;
	}
	
    public int getIdVoyage() {
		return idVoyage;
	}


	public void setIdVoyage(int idVoyage) {
		this.idVoyage = idVoyage;
	}


	public int getIdParcours() {
		return idParcours;
	}


	public void setIdParcours(int idParcours) {
		this.idParcours = idParcours;
	}
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


		public int getIdAvionIn() {
			return idAvionIn;
		}


		public void setIdAvionIn(int idAvionIn) {
			this.idAvionIn = idAvionIn;
		}


		public int getIdAvionOut() {
			return idAvionOut;
		}


		public void setIdAvionOut(int idAvionOut) {
			this.idAvionOut = idAvionOut;
		}


		public int getIdTrainIn() {
			return idTrainIn;
		}


		public void setIdTrainIn(int idTrainIn) {
			this.idTrainIn = idTrainIn;
		}


		public int getIdTrainOut() {
			return idTrainOut;
		}


		public void setIdTrainOut(int idTrainOut) {
			this.idTrainOut = idTrainOut;
		}
}
