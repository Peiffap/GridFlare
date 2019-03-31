package epl.students.programmers.gridflare.tools;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "T_Room")
public class Room implements Parcelable {
    @DatabaseField(generatedId = true, unique = true)
    int idRoom;
    @DatabaseField(canBeNull = false)
    String room_name;
    @DatabaseField
    int floor;
    @DatabaseField
    int idPlace;

    public Room() {
    }

    public Room(String room_name, int floor) {
        this.room_name = room_name;
        this.floor = floor;
        this.idPlace = -1;
    }

    public Room(String room_name, int floor, int idPlace) {
        this.room_name = room_name;
        this.floor = floor;
        this.idPlace = idPlace;
    }

    protected Room(Parcel in) {
        idRoom = in.readInt();
        room_name = in.readString();
        floor = in.readInt();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    public String getRoom_name() {
        return room_name;
    }

    public int getFloor() {
        return floor;
    }

    public int getIdPlace(){
        return this.idPlace;
    }

    public void setIdPlace(int idPlace) {
        this.idPlace = idPlace;
    }

    @Override
    public String toString() {
        return " room : "+ getRoom_name() + " floor : " + getFloor();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idRoom);
        parcel.writeString(room_name);
        parcel.writeInt(floor);
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Room) {
            Room tmp = (Room) o;
            return tmp.getRoom_name().equals(this.getRoom_name());
        }
        return false;
    }
}
