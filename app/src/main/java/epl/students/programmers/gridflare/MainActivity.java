package epl.students.programmers.gridflare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    WifiScanner wifi;

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
        value.setText("" + wifi.getStrength() + "\n" + wifi.getPing() + "\n" + "Done");
    }
}
