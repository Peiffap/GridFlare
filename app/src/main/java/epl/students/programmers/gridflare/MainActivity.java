package epl.students.programmers.gridflare;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    WifiScanner wifi;

    private final int NUMBER_OF_LEVELS=5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Launch a test");
        wifi = new WifiScanner(getApplicationContext());
    }

    public void launch_a_test(View v){
        Toast.makeText(getBaseContext(), "Test in progress. Stay where you are.",
                Toast.LENGTH_LONG).show();
        TextView value = findViewById(R.id.launch_value);

        //value.setText("" + wifi.getStrength() + "\n" + wifi.getPing() + "\n" + "Done");

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int level = 0;
        if(wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), NUMBER_OF_LEVELS);
        }

        value.setText(Integer.toString(level));
    }
}
