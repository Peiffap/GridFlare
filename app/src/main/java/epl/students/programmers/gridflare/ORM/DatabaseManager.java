package epl.students.programmers.gridflare.ORM;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import epl.students.programmers.gridflare.tools.Data;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;
import epl.students.programmers.gridflare.tools.GlobalScan;


public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "GridFlare.db";

    private static final int DATABASE_VERSION = 21;

    public DatabaseManager( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable( connectionSource, Scan_information.class );
            TableUtils.createTable( connectionSource, Room.class );
            TableUtils.createTable( connectionSource, Data.class );
            TableUtils.createTable( connectionSource, Place.class);
            TableUtils.createTable( connectionSource, GlobalScan.class);
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
            TableUtils.dropTable( connectionSource, Data.class, true );
            TableUtils.dropTable( connectionSource, Place.class, true);
            TableUtils.dropTable( connectionSource, GlobalScan.class, true);
            onCreate( database, connectionSource);
            Log.i( "DATABASE", "DB update" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't upgrade Database", exception );
        }
    }

    //region insert
    public void insertScan( Scan_information info) {
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            dao.create(info);
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
        }
    }

    public void insertRoom(Room room) {
        try {
            Dao<Room, Integer> dao = getDao( Room.class );
            dao.create(room);
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
        }
    }

    public void insertData( Data data) {
        try {
            Dao<Data, Integer> dao = getDao( Data.class );
            dao.create(data);
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
    //endregion

    //region read
    public void insertGlobalScan(GlobalScan global){
        try {
            Dao<GlobalScan, Integer> dao = getDao(GlobalScan.class);
            dao.create(global);
        } catch( Exception exception) {
            Log.e("DATABASE", "Can't insert data into database", exception);
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

    public ArrayList<Scan_information> readScan(String room, GlobalScan global){
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            Dao<Room, Integer> dao_room = getDao(Room.class);


            QueryBuilder<Scan_information,Integer> scan_informationQueryBuilder = dao.queryBuilder();
            scan_informationQueryBuilder.where().eq("globalScan_idGlobalScan", global.getIdGlobalScan());
            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();
            roomQueryBuilder.where().eq("room_name",room);


            List<Scan_information> test = scan_informationQueryBuilder.join(roomQueryBuilder).query();


            return (ArrayList<Scan_information>) test;
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

    public ArrayList<Scan_information> readScan(String room, int idScan) {
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            Dao<Room, Integer> dao_room = getDao(Room.class);
            Dao<Data, Integer> dao_data = getDao(Data.class);

            QueryBuilder<Scan_information,Integer> scan_informationQueryBuilder = dao.queryBuilder();

            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();
            roomQueryBuilder.where().eq("room_name",room);

            QueryBuilder<Data,Integer> dataQueryBuilder = dao_data.queryBuilder();
            dataQueryBuilder.where().eq("idScan", idScan);

            scan_informationQueryBuilder.join(roomQueryBuilder);
            scan_informationQueryBuilder.join(dataQueryBuilder);
            List<Scan_information> test = scan_informationQueryBuilder.query();


            return (ArrayList<Scan_information>) test;
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

    public Scan_information readLastScan(String room){
        try {
            Dao<Scan_information, Integer> dao = getDao( Scan_information.class );
            Dao<Room, Integer> dao_room = getDao(Room.class);

            QueryBuilder<Scan_information,Integer> scan_informationQueryBuilder = dao.queryBuilder();
            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();
            roomQueryBuilder.where().eq("room_name",room);


            List<Scan_information> test = scan_informationQueryBuilder.join(roomQueryBuilder).query();
            Scan_information max = test.get(0);
            for(Scan_information scan: test){
                if(scan.getId_Scan_information() > max.getId_Scan_information())
                    max = scan;
            }
            return max;

        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't read data into Database", exception );
            return null;
        }
    }

    public ArrayList<Room> readRoom(String room){
    //public ArrayList<Room> readRoom2(String room){
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

    //Return all the rooms of a place
    public ArrayList<Room> readRoom(Place place){
        try {
            Dao<Room, Integer> dao_room = getDao(Room.class);
            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();

            Dao<Place, Integer> dao_place = getDao(Place.class);
            QueryBuilder<Place, Integer> placeQueryBuilder = dao_place.queryBuilder();
            placeQueryBuilder.where().eq("place_name", place.getPlace_name());//Why not by id?

            roomQueryBuilder.join(placeQueryBuilder);

            return (ArrayList<Room>) roomQueryBuilder.query();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

    public ArrayList<Room> readRoomFromPlace(String place){
        try {
            Dao<Room, Integer> dao_room = getDao(Room.class);
            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();

            Dao<Place, Integer> dao_place = getDao(Place.class);
            QueryBuilder<Place, Integer> placeQueryBuilder = dao_place.queryBuilder();

            placeQueryBuilder.where().eq("place_name", place);//Why not by id?

            roomQueryBuilder.join(placeQueryBuilder);

            return (ArrayList<Room>) roomQueryBuilder.query();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

    public ArrayList<Room> readRoom(String room, int floor, Place place){
        try {
            Dao<Room, Integer> dao_room = getDao(Room.class);
            Dao<Place, Integer> dao_place = getDao(Place.class);

            QueryBuilder<Room,Integer> roomQueryBuilder = dao_room.queryBuilder();
            roomQueryBuilder.where().eq("floor",floor).and().eq("room_name",room);

            QueryBuilder<Place, Integer> placeQueryBuilder = dao_place.queryBuilder();
            placeQueryBuilder.where().eq("place_name", place.getPlace_name());

            roomQueryBuilder.join(placeQueryBuilder);


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

    public ArrayList<Data> readData() {
        try {
            Dao<Data, Integer> dao_room = getDao(Data.class);

            return (ArrayList<Data>) dao_room.queryForAll();
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't insert data into Database", exception);
            return null;
        }
    }

    public ArrayList<Data> readData(int id) {
        try {
            Dao<Data, Integer> dao_room = getDao(Data.class);

            return (ArrayList<Data>) dao_room.queryBuilder().where().eq("idScan",id).query();
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't insert data into Database", exception);
            return null;
        }
    }

    public ArrayList<Data> readData(GlobalScan globalScan){
        try {
            Dao<Data, Integer> dao_room = getDao(Data.class);

            return (ArrayList<Data>) dao_room.queryBuilder().where().eq("id_globalScan",globalScan).query();
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't insert data into Database", exception);
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

    public int getNextIDScan(){
        try {
            Dao<Data, Integer> dao_room = getDao(Data.class);
            QueryBuilder<Data, Integer> queryBuilder = dao_room.queryBuilder();
            queryBuilder.orderBy("idScan",false);
            ArrayList<Data> data = (ArrayList<Data>) queryBuilder.query();
            if(data.size() == 0)
                return 0;
            else
                return data.get(0).getIdScan() + 1;
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return -2;
        }
    }

    public ArrayList<Place> readPlace(String place){
        try {
            Dao<Place, Integer> dao_place = getDao(Place.class);

            return (ArrayList<Place>) dao_place.queryBuilder().where().eq("place_name",place).query();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }
    //endregion

    public ArrayList<GlobalScan> readGlobal(Place place){
        try {
            Dao<GlobalScan, Integer> dao_gs = getDao(GlobalScan.class);
            Dao<Place, Integer> dao_place = getDao(Place.class);

            QueryBuilder<Place,Integer> placeQueryBuilder = dao_place.queryBuilder();
            placeQueryBuilder.where().eq("place_name",place.getPlace_name());

            QueryBuilder<GlobalScan,Integer> globalScanQueryBuilder = dao_gs.queryBuilder();

            return (ArrayList<GlobalScan>) globalScanQueryBuilder.join(placeQueryBuilder).query();
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert data into Database", exception );
            return null;
        }
    }

    public void deleteRoom(Room room){
        System.out.println("------------------------------DELETE");
        try{
            Dao<Room, Integer> dao_room = getDao(Room.class);

            dao_room.delete(room);
        } catch( Exception exception) {
            Log.e("DATABASE", "Can't delete from database", exception);
        }
    }

    public void deleteScan(Scan_information si){
        try{
            Dao<Scan_information, Integer> dao_scan = getDao(Scan_information.class);

            dao_scan.delete(si);
        } catch( Exception exception) {
            Log.e("DATABASE", "Can't delete from database", exception);
        }
    }

    public void deletePlace(Place place){
        try{
            Dao<Place, Integer> dao_scan = getDao(Place.class);

            dao_scan.delete(place);
        } catch( Exception exception) {
            Log.e("DATABASE", "Can't delete from database", exception);
        }
    }

    public void deleteGlobalScan(GlobalScan globalScan) {
        try{
            Dao<GlobalScan, Integer> dao_scan = getDao(GlobalScan.class);

            dao_scan.delete(globalScan);
        } catch( Exception exception) {
            Log.e("DATABASE", "Can't delete from database", exception);
        }
    }

    public void updateRoom(Room room){
        try{
            Dao<Room, Integer> dao = getDao(Room.class);
            dao.update(room);
        } catch( Exception exception) {
            Log.e("DATABASE", "Can't update the database", exception);
        }
    }

    public void updatePlace(Place place){
        try{
            Dao<Place, Integer> dao = getDao(Place.class);
            dao.update(place);
        } catch( Exception exception) {
            Log.e("DATABASE", "Can't delete the database", exception);
        }
    }
}