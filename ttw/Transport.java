package ttw;

import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;

public abstract class Transport {
    private Date dateDep;
    private Time timeDep;
    private Date dateArr;
    private Time timeArr;
    private String code;

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
}
