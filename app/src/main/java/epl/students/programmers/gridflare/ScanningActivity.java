package epl.students.programmers.gridflare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import epl.students.programmers.gridflare.tools.WifiScanner;

public class ScanningActivity extends AppCompatActivity {

    WifiScanner wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_scanning);
        setTitle("Scanning");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        wifi = new WifiScanner(getApplicationContext());
    }

    //Start a scan
    public void launch_a_test(View v){
        Toast.makeText(getBaseContext(), "Test in progress. Stay where you are ! ", Toast.LENGTH_LONG).show();

        if(!wifi.isWifiEnabled())//Check one more time
            openDialog();

        final TextView value = findViewById(R.id.launch_value);
        value.setText("Working");

        new Thread(new Runnable() {//Thread because the ping is a blocking function
            @Override
            public void run() {
                wifi.update();
                //Update SQL
                //Il faudra encore ajouter la position et le DL
                Controller.getInstance().getSQLiteInterface(getBaseContext()).addPoint(0, 0, wifi.getStrength(), wifi.getPing(), 0);
                //Update View
                value.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (wifi.getPing() == (float)-1 || wifi.getProportionOfLost() == (float)-1 || wifi.getStrength() == (float)-1){
                            value.setText("Error. Check your connection, and try later.");
                        }
                        else {
                            String to_print = "Wi-Fi strength: " + wifi.getStrength() + "\n" +
                                    "ping: " + wifi.getPing() + "ms\n" +
                                    "Proportion of lost packets: " + wifi.getProportionOfLost() + "%\n";
                            value.setText(to_print);
                            //Stop the waiting animation
                        }
                    }
                });
            }
        }).start();
    }

    //Check wifi connection
    public void openDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Wi-Fi disabled");
        alertDialog.setMessage("Do you want turn on your Wi-Fi ?");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(),"Sorry this app cannot work without Wifi",Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                wifi.enableWifi();
            }

        });

        alertDialog.show();
    }

    //Back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            this.finish();

        return super.onOptionsItemSelected(item);
    }
}
