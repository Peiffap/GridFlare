package epl.students.programmers.gridflare.tools;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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

    private int numberOfScans; // Only used for the sum up

    //For ORM
    public Scan_information(){}

    public Scan_information(String room, int strength, float ping, float proportionOfLost, float dl, Date date) {
        this.room = room;
        this.strength = strength;
        this.ping = ping;
        this.proportionOfLost = proportionOfLost;
        this.dl = dl;
        this.date = date;
        this.numberOfScans = 999;
    }

    protected Scan_information(Parcel in) {
        id_Scan_information = in.readInt();
        room = in.readString();
        strength = in.readInt();
        ping = in.readFloat();
        proportionOfLost = in.readFloat();
        dl = in.readFloat();

    }

    public static final Creator<Scan_information> CREATOR = new Creator<Scan_information>() {
        @Override
        public Scan_information createFromParcel(Parcel in) {
            return new Scan_information(in);
        }

        @Override
        public Scan_information[] newArray(int size) {
            return new Scan_information[size];
        }
    };

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

    public int getNumberOfScans(){
        return this.numberOfScans;
    }

    public void setNumberOfScans(int nos){
        this.numberOfScans = nos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_Scan_information);
        parcel.writeString(room);
        parcel.writeInt(strength);
        parcel.writeFloat(ping);
        parcel.writeFloat(proportionOfLost);
        parcel.writeFloat(dl);
        parcel.writeInt(numberOfScans);
    }
}
