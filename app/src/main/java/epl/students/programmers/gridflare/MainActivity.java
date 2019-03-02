package epl.students.programmers.gridflare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    WifiScanner wifi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Launch a test");
        wifi = new WifiScanner(getApplicationContext());
        if(!wifi.isWifiEnabled())
            openDialog();
    }

    public void go_to_scanner(View v){
        Intent it = new Intent(this, ScanningActivity.class);
        startActivity(it);
    }

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
}
