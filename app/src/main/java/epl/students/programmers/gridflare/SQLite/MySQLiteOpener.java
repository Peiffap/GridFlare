package epl.students.programmers.gridflare.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpener extends SQLiteOpenHelper {

    public MySQLiteOpener(Context context, String name, SQLiteDatabase.CursorFactory fact, int version){
        super(context, name, fact, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {//Only called if we change the DB
        //Ajouter la creation de la table
        sqLiteDatabase.execSQL("CREATE TABLE Point (x INTEGER NOT NULL, y INTEGER NOT NULL, strength INTEGER, ping REAL, dl REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {//If version change

    }
}
