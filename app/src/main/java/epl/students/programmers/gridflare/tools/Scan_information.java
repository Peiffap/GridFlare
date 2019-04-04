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
    @DatabaseField(canBeNull=false, foreign = true, foreignColumnName = "idRoom", foreignAutoCreate = true)
    private Room room_idRoom;
    @DatabaseField(canBeNull=false)
    private int strength;
    @DatabaseField(canBeNull=false)
    private float ping;
    @DatabaseField(canBeNull=false)
    private float proportionOfLost;
    @DatabaseField(canBeNull=false)
    private float dl;
    @DatabaseField(canBeNull=false, foreign = true, foreignColumnName = "idData", foreignAutoCreate = true)
    private Data data;
    @DatabaseField(canBeNull = true, foreign = true, foreignColumnName = "idGlobalScan", foreignAutoCreate = true)
    private GlobalScan globalScan;

    private int numberOfScans; // Only used for the sum up

    //For ORM
    public Scan_information(){}

    public Scan_information(Room room, int strength, float ping, float proportionOfLost, float dl, Data data) {
        this.room_idRoom = room;
        this.strength = strength;
        this.ping = ping;
        this.proportionOfLost = proportionOfLost;
        this.dl = dl;
        this.data = data;
        this.numberOfScans = 999;
    }

    public Scan_information(Room room, int strength, float ping, float proportionOfLost, float dl, Data data, GlobalScan scan) {
        this.room_idRoom = room;
        this.strength = strength;
        this.ping = ping;
        this.proportionOfLost = proportionOfLost;
        this.dl = dl;
        this.data = data;
        this.numberOfScans = 999;
        this.globalScan = scan;
    }

    protected Scan_information(Parcel in) {
        id_Scan_information = in.readInt();
        room_idRoom = in.readParcelable(Room.class.getClassLoader());
        strength = in.readInt();
        ping = in.readFloat();
        proportionOfLost = in.readFloat();
        dl = in.readFloat();
        numberOfScans = in.readInt();
        data = in.readParcelable(Data.class.getClassLoader());
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_Scan_information);
        parcel.writeParcelable(room_idRoom, i);
        parcel.writeInt(strength);
        parcel.writeFloat(ping);
        parcel.writeFloat(proportionOfLost);
        parcel.writeFloat(dl);
        parcel.writeInt(numberOfScans);
        parcel.writeParcelable(data,i);
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

    public int getId_Scan_information(){
        return id_Scan_information;
    }

    public Room getRoom(){
        return room_idRoom;
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

    public Data getData(){
        return data;
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
    public String toString(){
        return getRoom().toString() + "::::" + globalScan.toString();
    }
}
