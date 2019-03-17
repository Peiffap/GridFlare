package epl.students.programmers.gridflare.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import epl.students.programmers.gridflare.SQLite.MySQLiteOpener;

public class Test {
    private String place;
    private int ping;
    private double proportionOfLost;
    private double strength;
    private double DL;
    private String date;

    public Test(String place, int ping, double proportionOfLost, double strength, double DL){
        this.place = place;
        this.ping = ping;
        this.proportionOfLost = proportionOfLost;
        this.strength = strength;
        this.DL = DL;
        Date date_e = new Date();
        date = date_e.toString(); // Does it work like this ?
    }

    public String getPlace(){
        return this.place;
    }

    public int getPing(){
        return this.ping;
    }

    public double getProportionOfLost() {
        return proportionOfLost;
    }

    public double getStrength() {
        return strength;
    }

    public double getDL(){
        return this.DL;
    }

    public String getDate(){
        return this.date;
    }

    public void DB_delete(){
        SQLiteDatabase db = MySQLiteOpener.get().getWritableDatabase();
        db.delete("TEST", "WHEN =?", new String[]{date});
        db.close();
    }

    public void DB_add(){
        SQLiteDatabase db = MySQLiteOpener.get().getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("PLACE", this.place);
        values.put("PING", this.ping);
        values.put("PROPORTION_OF_LOST", this.proportionOfLost);
        values.put("STRENGTH", this.strength);
        values.put("DL", this.DL);

        db.insert("TEST", null, values);

        db.close();
    }

    public ArrayList<Test> get_by_place(String place){
        SQLiteDatabase db = MySQLiteOpener.get().getReadableDatabase();

        // Columns to get
        String[] columns = new String[]{"PLACE", "PING", "PROPORTION_OF_LOST", "STRENGTH", "DL"};

        Cursor cursor = db.query("TEST", columns, "PLACE =?", new String[]{place}, null, null, null);

        cursor.moveToFirst();

        ArrayList<Test> al = new ArrayList<>();

        while (!cursor.isAfterLast()){
            String pl = cursor.getString(0);
            int ping = cursor.getInt(1);
            double prop = cursor.getDouble(2);
            double str = cursor.getDouble(3);
            double dl = cursor.getDouble(4);

            Test test = new Test(pl, ping, prop, str, dl);
            al.add(test);

            cursor.moveToNext();
        }

        cursor.close();

        return al;
    }
}
