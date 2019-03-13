package epl.students.programmers.gridflare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;
import epl.students.programmers.gridflare.tools.WifiScanner;

import static android.widget.Toast.makeText;


public class MainActivity extends AppCompatActivity {

    WifiScanner wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Launch a test");
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        wifi = new WifiScanner(getApplicationContext());
        if(!wifi.isWifiEnabled())
            openDialog();

        setSingleEvent(gridLayout);
    }

    public void go_to_scanner(View v){
        Intent it = new Intent(this, ScanningActivity.class);
        startActivity(it);
    }

    public void go_to_heatmap(View v){
        Intent it = new Intent(this, HeatmapActivity.class);
        startActivity(it);
    }

    public void openDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Wi-Fi disabled");
        alertDialog.setMessage("Do you want turn on your Wi-Fi ?");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                makeText(getBaseContext(),"Sorry this app cannot work without Wifi",Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                wifi.enableWifi();
            }

        });

        alertDialog.show();
    }

    public void setSingleEvent(GridLayout singleEvent) {
        for(int i = 0; i<singleEvent.getChildCount(); i++){
            CardView cardView = (CardView)singleEvent.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI == 0){
                        go_to_heatmap(view);
                    }
                    else if (finalI == 1){
                        go_to_scanner(view);
                    }
                    else makeText(MainActivity.this, "index " + finalI, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
