package epl.students.programmers.gridflare.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import epl.students.programmers.gridflare.tools.Point;

public class SQLiteInterface {

    private String name = "measure.sqlite";
    private int v = 1;
    private MySQLiteOpener access;

    public SQLiteInterface(Context context){
        access = new MySQLiteOpener(context, name, null, v);
    }

    public void addPoint(int x, int y, int strength, double ping, double dl){
        SQLiteDatabase db = access.getWritableDatabase();
        String request = "INSERT INTO Point (x, y, strength, ping, dl) VALUES ("+x+", "+y+", "+strength+", "+ping+", "+dl+");";
        db.execSQL(request);
        db.close();//Pas sur ATTENTION
    }

    public ArrayList<Point> getPoints(){
        ArrayList<Point> lst = new ArrayList<>();
        SQLiteDatabase db = access.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Point",null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                Point p = new Point(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getDouble(3), cursor.getDouble(4));
                lst.add(p);
            }
        }
        cursor.close();
        db.close();//Pas sur ATTENTION
        return lst;
    }
}
