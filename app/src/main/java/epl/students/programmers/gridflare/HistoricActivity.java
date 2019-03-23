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
import epl.students.programmers.gridflare.tools.RecyclerItemClickListener;
import epl.students.programmers.gridflare.tools.Scan_information;

public class HistoricActivity extends AppCompatActivity{

    ArrayList<Scan_information> historicByRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);
        setTitle("Historic");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        String[] rooms = getResources().getStringArray(R.array.spinner_items);

        DatabaseManager databaseManager = new DatabaseManager(this);
        historicByRoom = new ArrayList<>();

        for(int i = 0; i < rooms.length; i++){
            ArrayList<Scan_information> aRoom = databaseManager.readScan(rooms[i]);
            if(aRoom.size() == 0) continue;
            int strength = 0;
            float ping = 0;
            float proportionOfLost = 0;
            float dl = 0;
            for(int j = 0; j < aRoom.size(); j++){
                Scan_information si = aRoom.get(j);
                strength += si.getStrength();
                ping += si.getPing();
                proportionOfLost += si.getProportionOfLost();
                dl += si.getDl();
            }
            strength = (int) (((double) strength) / ((double)aRoom.size()));
            ping = (float) (((double) ping) / ((double)aRoom.size()));
            proportionOfLost = (float) (((double) proportionOfLost) / ((double)aRoom.size()));
            dl = (float) (((double) dl) / ((double)aRoom.size()));

            Scan_information meaned = new Scan_information(rooms[i], strength, ping, proportionOfLost, dl, new Date());
            meaned.setNumberOfScans(aRoom.size());
            historicByRoom.add(meaned);
        }

        RecyclerView recyclerView = findViewById(R.id.listView);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getBaseContext(), RoomSumUpActivity.class);
                        intent.putExtra("theRoom", historicByRoom.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        Adapter_Scan_information adapter = new Adapter_Scan_information(historicByRoom);


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        databaseManager.close();
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
