package epl.students.programmers.gridflare.tools;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Scanne_information {
    private String room;
    private int strength;
    private float ping;
    private float proportionOfLost;
    private float dl;
    private Date date;

    public Scanne_information(String room,int strength, float ping, float proportionOfLost, float dl, Date date) {
        this.room = room;
        this.strength = strength;
        this.ping = ping;
        this.proportionOfLost = proportionOfLost;
        this.dl = dl;
        this.date = date;
    }

    public String getRoom(){
        return room;
    }

    public int getStrength() {
        return strength;
    }

    public float getPing() {
        return ping;
    }

    public float getProportionOfLost() {
        return proportionOfLost;
    }

    public float getDl() {
        return dl;
    }

    public Date getDate(){
        return date;
    }
}
