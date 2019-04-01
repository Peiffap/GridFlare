package epl.students.programmers.gridflare.tools;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class GlobalScan implements Parcelable {

    @DatabaseField(canBeNull = false, generatedId = true, unique = true)
    private int idGlobalScan;
    @DatabaseField(canBeNull = false)
    private Date date;
    @DatabaseField(canBeNull = true, foreign = true, foreignColumnName = "idPlace", foreignAutoCreate = true)
    private Place place;


    public GlobalScan(Date date, Place place){
        this.date = date;
        this.place = place;
    }

    protected GlobalScan(Parcel in) {
        idGlobalScan = in.readInt();
        place = in.readParcelable(Place.class.getClassLoader());
    }

    public static final Creator<GlobalScan> CREATOR = new Creator<GlobalScan>() {
        @Override
        public GlobalScan createFromParcel(Parcel in) {
            return new GlobalScan(in);
        }

        @Override
        public GlobalScan[] newArray(int size) {
            return new GlobalScan[size];
        }
    };

    public Date getDate(){
        return this.date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Place getPlace(){
        return this.place;
    }

    public void setPlace(Place place){
        this.place = place;
    }

    public int getIdGlobalScan(){
        return this.idGlobalScan;
    }

    public GlobalScan(){ }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idGlobalScan);
        parcel.writeParcelable(place, i);
    }

    @Override
    public String toString(){
        return "id" + idGlobalScan;
    }
}
