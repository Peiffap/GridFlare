package epl.students.programmers.gridflare;

import android.content.Context;

import epl.students.programmers.gridflare.SQLite.SQLiteInterface;

public class Controller {

    private SQLiteInterface sql;
    private static Controller instance;

    private Controller(){
        super();
    }

    public static Controller getInstance() {
        if(instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public SQLiteInterface getSQLiteInterface(Context context){
        if(sql == null){
            sql = new SQLiteInterface(context);
        }
        return sql;
    }
}
