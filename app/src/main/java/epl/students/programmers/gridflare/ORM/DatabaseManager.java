package epl.students.programmers.gridflare.ORM;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.List;

import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;


public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "GridFlare.db";
    private static final int DATABASE_VERSION = 9;

    public DatabaseManager( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable( connectionSource, Scan_information.class );
            TableUtils.createTable( connectionSource, Room.class );
            Log.i( "DATABASE", "DB create" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't create Database", exception );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable( connectionSource, Scan_information.class, true );
            TableUtils.dropTable( connectionSource, Room.class, true );
            onCreate( database, connectionSource);
            Log.i( "DATABASE", "DB update" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't upgrade Database", exception );
        }
    }

    public void insertScan( Scan_information info) {
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            dao.create(info);
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
        }
    }


    public ArrayList<Scan_information> readScan(String room) {
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            Dao<Room, Integer> dao_room = getDao(Room.class);

            QueryBuilder<Scan_information,Integer> scan_informationQueryBuilder = dao.queryBuilder();
            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();
            roomQueryBuilder.where().eq("room_name",room);


            List<Scan_information> test = scan_informationQueryBuilder.join(roomQueryBuilder).query();


            return (ArrayList<Scan_information>) test;
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }
    public ArrayList<Scan_information> readScan() {
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            return (ArrayList<Scan_information>) dao.queryForAll();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

}