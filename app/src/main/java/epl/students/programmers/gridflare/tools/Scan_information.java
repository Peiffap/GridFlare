package epl.students.programmers.gridflare.tools;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "T_Scan_information")
public class Scan_information {

    @DatabaseField(generatedId = true, unique = true)
    private int id_Scan_information;
    @DatabaseField(canBeNull=false)
    private String room;
    @DatabaseField(canBeNull=false)
    private int strength;
    @DatabaseField(canBeNull=false)
    private float ping;
    @DatabaseField(canBeNull=false)
    private float proportionOfLost;
    @DatabaseField(canBeNull=false)
    private float dl;
    @DatabaseField(canBeNull=false)
    private Date date;

    //For ORM
    public Scan_information(){}

    public Scan_information(String room, int strength, float ping, float proportionOfLost, float dl, Date date) {
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
