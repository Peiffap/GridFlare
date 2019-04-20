package epl.students.programmers.gridflare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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

    private BottomNavigationView navigationView;

    WifiScanner wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_live_scanning);
        navigationView = findViewById(R.id.navigation_view_live_scanning);
        navigationView.setSelectedItemId(R.id.live_menu_btn);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_menu_btn:
                        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        break;
                    case R.id.scan_menu_btn:
                        Intent intent2 = new Intent(getBaseContext(), NewScanActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        break;
                }
                return true;
            }
        });
        stop = false;
        pourcent = findViewById(R.id.d_live_value);
        //realValue = findViewById(R.id.live_dbm);
        //background = findViewById(R.id.live_background);
        wifi = new WifiScanner(getApplicationContext());
        startLiveScan();
    }

    /*
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
*/

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
                            //realValue.setText(rssi + "dBm");
                            //background.setBackgroundColor(wifi.getLiveColor());
                        }
                    });
                }
            }
        }).start();
    }
}
