package epl.students.programmers.gridflare;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Date;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Adapter_Scan_information;
import epl.students.programmers.gridflare.tools.Adapter_globalScan;
import epl.students.programmers.gridflare.tools.Data;
import epl.students.programmers.gridflare.tools.GlobalScan;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.RecyclerItemClickListener;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;

public class HistoricActivity extends AppCompatActivity{

    ArrayList<Scan_information> historicByRoom;
    GlobalScan theGlobalScan;
    Place thePlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);
        setTitle("Historic");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        Intent intent = getIntent();
        theGlobalScan = intent.getParcelableExtra("theGlobalScan");
        thePlace = intent.getParcelableExtra("thePlace");

        DatabaseManager databaseManager = new DatabaseManager(this);

        RecyclerView recyclerView = findViewById(R.id.listView);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(historicByRoom.get(position).getNumberOfScans() == 0 || historicByRoom.get(position).getNumberOfScans() == 999){
                            return;
                        }
                        Intent intent = new Intent(getBaseContext(), RoomSumUpActivity.class);
                        intent.putExtra("theRoom", historicByRoom.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        computeMean();

        Adapter_globalScan adapter = new Adapter_globalScan(historicByRoom);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        databaseManager.close();
    }

    public void computeMean(){
        DatabaseManager databaseManager = new DatabaseManager(this);
        ArrayList<Room> rooms = databaseManager.readRoom(thePlace);
        historicByRoom = new ArrayList<>();

        for(int i = 0; i < rooms.size(); i++){
            ArrayList<Scan_information> aRoom = databaseManager.readScan(rooms.get(i).getRoom_name(), theGlobalScan);
            int strength = 0;
            float ping = 0;
            float proportionOfLost = 0;
            float dl = 0;
            if(aRoom.size() != 0) {
                for (int j = 0; j < aRoom.size(); j++) {
                    Scan_information si = aRoom.get(j);
                    strength += si.getStrength();
                    ping += si.getPing();
                    proportionOfLost += si.getProportionOfLost();
                    dl += si.getDl();
                }
                strength = (int) (((double) strength) / ((double) aRoom.size()));
                ping = (float) (((double) ping) / ((double) aRoom.size()));
                proportionOfLost = (float) (((double) proportionOfLost) / ((double) aRoom.size()));
                dl = (float) (((double) dl) / ((double) aRoom.size()));
            }
            Scan_information meaned = new Scan_information(new Room(rooms.get(i).getRoom_name(),rooms.get(i).getFloor()), strength, ping, proportionOfLost, dl, new Data(-1,new Date()));
            meaned.setNumberOfScans(aRoom.size());
            historicByRoom.add(meaned);
        }
    }

    //Back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
