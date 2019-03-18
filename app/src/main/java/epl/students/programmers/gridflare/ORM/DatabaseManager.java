package epl.students.programmers.gridflare.ORM;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;

import epl.students.programmers.gridflare.tools.Scan_information;


public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "GridFlare.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable( connectionSource, Scan_information.class );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't create Database", exception );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable( connectionSource, Scan_information.class, true );
            onCreate( database, connectionSource);
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't upgrade Database", exception );
        }
    }

    public void insertScan( Scan_information info) {
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            dao.create(info);
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert score into Database", exception );
        }
    }


    public ArrayList<Scan_information> readScan(String room) {
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            return (ArrayList<Scan_information>) dao.queryBuilder().where().eq("room",room).query();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert score into Database", exception );
            return null;
        }
    }
    public ArrayList<Scan_information> readScan() {
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            return (ArrayList<Scan_information>) dao.queryForAll();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert score into Database", exception );
            return null;
        }
    }

}