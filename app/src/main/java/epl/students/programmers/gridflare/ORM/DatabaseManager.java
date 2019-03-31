package epl.students.programmers.gridflare.ORM;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ScrollView;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.List;

import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;


public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "GridFlare.db";
    private static final int DATABASE_VERSION = 14;

    public DatabaseManager( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable( connectionSource, Scan_information.class );
            TableUtils.createTable( connectionSource, Room.class );
            TableUtils.createTable( connectionSource, Place.class);
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
            TableUtils.dropTable( connectionSource, Place.class, true);
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

    public void insertRoom( Room room) {
        try {
            Dao<Room, Integer> dao = getDao( Room.class );
            dao.create(room);
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
        }
    }

    public void insertPlace(Place place){
        try {
            Dao<Place, Integer> dao = getDao( Place.class );
            dao.create(place);
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

    public ArrayList<Room> readRoom(String room){
        try {
            Dao<Room, Integer> dao_room = getDao(Room.class);

            return (ArrayList<Room>) dao_room.queryBuilder().where().eq("room_name",room).query();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }
    public ArrayList<Room> readRoom(String room, int floor){
        try {
            Dao<Room, Integer> dao_room = getDao(Room.class);
            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();

            roomQueryBuilder.where().eq("floor",floor).and().eq("room_name",room);

            return (ArrayList<Room>) roomQueryBuilder.query();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

    public ArrayList<Room> readRoom(Place place){
        try {
            Dao<Room, Integer> dao_room = getDao(Room.class);
            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();

            roomQueryBuilder.where().eq("place_idPlace_idPlace", place);

            return (ArrayList<Room>) roomQueryBuilder.query();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

    public ArrayList<Room> readRoom(String room, int floor, Place place){
        try {
            Dao<Room, Integer> dao_room = getDao(Room.class);
            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();

            roomQueryBuilder.where().eq("floor",floor).and().eq("room_name",room).and().eq("place_idPlace_idPlace", place);

            return (ArrayList<Room>) roomQueryBuilder.query();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

    public ArrayList<Room> readRoom(){
        try {
            Dao<Room, Integer> dao_room = getDao(Room.class);

            return (ArrayList<Room>) dao_room.queryForAll();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

    public ArrayList<Place> readPlace(){
        try {
            Dao<Place, Integer> dao_place = getDao(Place.class);

            return (ArrayList<Place>) dao_place.queryForAll();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

}