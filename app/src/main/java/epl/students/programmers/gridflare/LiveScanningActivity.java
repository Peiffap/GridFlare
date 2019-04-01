package epl.students.programmers.gridflare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import epl.students.programmers.gridflare.tools.WifiScanner; //Sera utile pour le live scanning

public class LiveScanningActivity extends AppCompatActivity {

    private boolean stop;
    TextView pourcent;
    TextView realValue;
    View background;

    WifiScanner wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_scanning);
        setTitle("Live scanning");
        stop = false;
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        pourcent = findViewById(R.id.live_value);
        realValue = findViewById(R.id.live_dbm);
        background = findViewById(R.id.live_background);
        wifi = new WifiScanner(getApplicationContext());
        startLiveScan();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                stop = true;
                this.finish();
                break;
            case R.id.informations:
                Intent it = new Intent(this, InformationsActivity.class);
                startActivity(it);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Apres vérification, la puissance du signal varie entre -30 et -90 (attention en echelle logarithmique meme si on va d'abord test de faire en linéaire)
    private void startLiveScan(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!stop){
                    final int rssi = wifi.update_live_scan();
                    pourcent.post(new Runnable() {
                        @Override
                        public void run() {
                            pourcent.setText(wifi.get_live_numeric_scale() + "%");
                            realValue.setText(rssi + "dBm");
                            background.setBackgroundColor(wifi.getLiveColor());
                        }
                    });
                }
            }
        }).start();
    }
}
