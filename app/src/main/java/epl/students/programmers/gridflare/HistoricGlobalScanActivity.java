package epl.students.programmers.gridflare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Adapter_Places;
import epl.students.programmers.gridflare.tools.Adapter_When;
import epl.students.programmers.gridflare.tools.GlobalScan;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.RecyclerItemClickListener;

public class HistoricGlobalScanActivity extends AppCompatActivity {

    ArrayList<GlobalScan> when;
    Place thePlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_global_scan);
        setTitle("All the scans for this place");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        Intent i = getIntent();
        thePlace =  (Place) i.getParcelableExtra("thePlace");

        displayData();
    }

    private void displayData(){
        DatabaseManager databaseManager = new DatabaseManager(getBaseContext());

        when = databaseManager.readGlobal(thePlace);

        RecyclerView recyclerView = findViewById(R.id.recycleView_global_scan_rooms_historic);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getBaseContext(), HistoricActivity.class);
                        intent.putExtra("theGlobalScan", when.get(position));
                        intent.putExtra("thePlace", thePlace);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        Adapter_When adapter = new Adapter_When(when);

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
