package epl.students.programmers.gridflare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Random;

import ca.hss.heatmaplib.HeatMap;
import epl.students.programmers.gridflare.tools.WifiScanner;

public class HeatmapActivity extends AppCompatActivity {

    HeatMap hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heatmap);
        setTitle("Heatmap");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        hm = (HeatMap) findViewById(R.id.heatmap);

        //Set the heatmap range
        hm.setMinimum(0.0);
        hm.setMaximum(100.0);

        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            HeatMap.DataPoint point = new HeatMap.DataPoint(rand.nextFloat(), rand.nextFloat(), rand.nextDouble() * 100.0);
            hm.addData(point);
        }

        HeatMap.DataPoint point = new HeatMap.DataPoint(0.2f, 0.6f, 100.0);
        hm.addData(point);
    }

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
