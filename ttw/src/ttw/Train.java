package ttw;

import java.sql.Time;
import java.util.Date;

public class Train extends Transport {
	private int idTrain;
	private int distance;
	private int idGareIn;
	private int idGareOut;
	private Date dateDep;
	private Time timeDep;
	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getIdTrain() {
		return idTrain;
	}

	public void setIdTrain(int idTrain) {
		this.idTrain = idTrain;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getIdGareIn() {
		return idGareIn;
	}

	public void setIdGareIn(int idGareIn) {
		this.idGareIn = idGareIn;
	}

	public int getIdGareOut() {
		return idGareOut;
	}

	public void setIdGareOut(int idGareOut) {
		this.idGareOut = idGareOut;
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

	private Date dateArr;
	private Time timeArr;

	public Train(int idVilleIn, int idVilleOut) {
		super(idVilleIn, idVilleOut);
		// TODO Auto-generated constructor stub
	}

}
