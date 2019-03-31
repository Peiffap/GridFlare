package epl.students.programmers.gridflare.tools;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "T_Data")
public class Data implements Parcelable {
    @DatabaseField(generatedId = true, unique = true)
    int idData;
    @DatabaseField(canBeNull = false)
    int idScan;
    @DatabaseField
    Date date;

    public Data(int idScan, Date date) {
        this.idScan = idScan;
        this.date = date;
    }
    //For ORM
    public Data(){}

    protected Data(Parcel in) {
        idData = in.readInt();
        idScan = in.readInt();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public int getIdScan() {
        return idScan;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString(){
        return "id" + getIdScan();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idData);
        parcel.writeInt(idScan);
    }
}
