package epl.students.programmers.gridflare;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import epl.students.programmers.gridflare.tools.WifiScanner;

public class LiveScanningActivity extends AppCompatActivity {

    private boolean stop;
    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_scanning);
        setTitle("Live scanning");
        stop = false;
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        txt = findViewById(R.id.live_value);
        startLiveScan();
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

    private void startLiveScan(){
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!stop){
                    WifiInfo wi = wifiManager.getConnectionInfo();
                    final int rssi = wi.getRssi();
                    txt.post(new Runnable() {
                        @Override
                        public void run() {
                            txt.setText(rssi + "dBm");
                        }
                    });
                }
            }
        }).start();
    }
}
