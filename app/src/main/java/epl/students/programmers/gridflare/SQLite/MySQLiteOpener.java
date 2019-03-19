package epl.students.programmers.gridflare.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import epl.students.programmers.gridflare.GridFlareApp;

public class MySQLiteOpener extends SQLiteOpenHelper {

    private static MySQLiteOpener instance;

    private static final String DATABASE_NAME = "GridFlareApp.sqlite";

    private static final String DATABASE_SQL_FILENAME = "GridFlareDBText.sql";

    private static final int DATABASE_VERSION = 1;

    public MySQLiteOpener(Context context, String name, SQLiteDatabase.CursorFactory fact, int version){
        super(context, name, fact, version);
        instance = this;
    }

    private MySQLiteOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        instance = this;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {//Only called if we change the DB
        //Ajouter la creation de la table
        sqLiteDatabase.execSQL("CREATE TABLE Point (x INTEGER NOT NULL, y INTEGER NOT NULL, strength INTEGER, ping REAL, dl REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {//If version change

    }

    public static MySQLiteOpener get(){
        if (instance == null){
            return new MySQLiteOpener(GridFlareApp.getContext());
        }
        return instance;
    }

}
