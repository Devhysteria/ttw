package ttw;

import java.sql.Time;
import java.util.Date;

public class Train extends Transport {
    private int idTrain;
    private int distance;
    private int idGareIn;
    private int idGareOut;
    
    // Getters et setters spécifiques à Train
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
}