package epl.students.programmers.gridflare.tools;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

public class Place implements Parcelable {
    @DatabaseField(canBeNull = false, generatedId = true)
    int idPlace;
    @DatabaseField(canBeNull = false)
    String place_name;
    @DatabaseField
    int number_of_floor;

    public Place(String place, int nof){
        this.place_name = place;
        this.number_of_floor = nof;
    }

    public Place(){
    }

    protected Place(Parcel in) {
        idPlace = in.readInt();
        place_name = in.readString();
        number_of_floor = in.readInt();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public int getNumber_of_floor() {
        return number_of_floor;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setNumber_of_floor(int number_of_floor) {
        this.number_of_floor = number_of_floor;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String toString(){
        return this.place_name + ": " + this.number_of_floor;
    }

    public int getIdPlace(){
        return this.idPlace;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idPlace);
        parcel.writeString(place_name);
        parcel.writeInt(number_of_floor);
    }
}
