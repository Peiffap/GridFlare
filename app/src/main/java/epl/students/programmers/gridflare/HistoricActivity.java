package epl.students.programmers.gridflare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Date;

import epl.students.programmers.gridflare.tools.Adapter_Scanne_information;
import epl.students.programmers.gridflare.tools.Scanne_information;
import epl.students.programmers.gridflare.tools.Test;

public class HistoricActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);
        setTitle("Historic");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        ListView listView = findViewById(R.id.listView);
        Adapter_Scanne_information adapter = new Adapter_Scanne_information(getApplicationContext(), 0);

        listView.setAdapter(adapter);

        ArrayList<Test> historic = new ArrayList<>();

        //TODO ajouter a historic les objet Scanne_information

        historic.add(new Test("bedroom",50,30.0F,0.0F,9000.0F));
        historic.add(new Test("bathroom",60,34.0F,2.0F,9500.0F));


        adapter.addAll(historic);
    }

    //Back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.informations:
                Intent it = new Intent(this, InformationsActivity.class);
                startActivity(it);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
