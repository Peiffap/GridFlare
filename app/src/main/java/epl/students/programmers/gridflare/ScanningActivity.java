package epl.students.programmers.gridflare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScanningActivity extends AppCompatActivity {

    WifiScanner wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_scanning);
        setTitle("Launch a test 2");
        wifi = new WifiScanner(getApplicationContext());
    }

    public void launch_a_test(View v){
        Toast.makeText(getBaseContext(), "Test in progress. Stay where you are.", Toast.LENGTH_LONG).show();

        final TextView value = findViewById(R.id.launch_value);
        value.setText("Working");

        new Thread(new Runnable() {//Thread because the ping is a blocking function
            @Override
            public void run() {
                wifi.update();
                value.post(new Runnable() {
                    @Override
                    public void run() {
                        value.setText("" + wifi.getStrength() + "\n" + wifi.getPing() + "\n" + wifi.getProportionOfLost() + "%\n Done");
                        //Stop the waiting animation
                    }
                });
            }
        }).start();
    }
}
