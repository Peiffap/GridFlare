package epl.students.programmers.gridflare.tools;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "T_Scan_information")
public class Scan_information implements Parcelable {

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

    public Scan_information(int strength, float ping, float proportionOfLost, float dl, Date date) {
        this.strength = strength;
        this.ping = ping;
        this.proportionOfLost = proportionOfLost;
        this.dl = dl;
        this.date = date;
    }

    public String getRoom(){
        return room;
    }
    public void setRoom(String room){
        this.room = room;
    }

    public int getStrength() {
        return strength;
    }
    public void setStrength(int Strength){
        this.strength = strength;
    }

    public float getPing() {
        return ping;
    }
    public void setPing(float ping){
        this.ping = ping;
    }

    public float getProportionOfLost() {
        return proportionOfLost;
    }
    public void setProportionOfLost(float proportionOfLost){
        this.proportionOfLost = proportionOfLost;
    }

    public float getDl() {
        return dl;
    }
    public void setDl(float dl){
        this.dl = dl;
    }

    public Date getDate(){
        return date;
    }
    public void setDate(Date date){
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
