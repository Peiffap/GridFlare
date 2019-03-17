package epl.students.programmers.gridflare.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import epl.students.programmers.gridflare.SQLite.MySQLiteOpener;

public class Test {

    public void DB_delete(Date date){
        SQLiteDatabase db = MySQLiteOpener.get().getWritableDatabase();
        db.delete("TEST", "WHEN =?", new String[]{date.toString()});
        db.close();
    }

    public static void DB_add(String place, int ping, double proportionOfLost, double strength, double DL, Date date){
        SQLiteDatabase db = MySQLiteOpener.get().getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("PLACE", place);
        values.put("PING", ping);
        values.put("PROPORTION_OF_LOST", proportionOfLost);
        values.put("STRENGTH", strength);
        values.put("DL", DL);

        db.insert("TEST", null, values);

        db.close();
    }

    public static ArrayList<Scanne_information> get_by_place(String place){
        SQLiteDatabase db = MySQLiteOpener.get().getReadableDatabase();

        // Columns to get
        String[] columns = new String[]{"PLACE", "PING", "PROPORTION_OF_LOST", "STRENGTH", "DL"};

        Cursor cursor = db.query("TEST", columns, "PLACE =?", new String[]{place}, null, null, null);

        cursor.moveToFirst();

        ArrayList<Scanne_information> al = new ArrayList<>();

        while (!cursor.isAfterLast()){
            String pl = cursor.getString(0);
            int ping = cursor.getInt(1);
            double prop = cursor.getDouble(2);
            double str = cursor.getDouble(3);
            double dl = cursor.getDouble(4);
            Date date = new Date();
            Scanne_information test = new Scanne_information(pl,ping, (float)prop, (float)str,(float) dl, date);
            al.add(test);

            cursor.moveToNext();
        }

        cursor.close();

        return al;
    }
}
