package epl.students.programmers.gridflare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;
import epl.students.programmers.gridflare.tools.WifiScanner;

public class GlobalScanActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    AutoCompleteTextView autoComplete;

    TextView ping;
    TextView strength;
    TextView lost;
    TextView dl;
    TextView wifiName;

    TextView workInProgress;

    ImageButton refresh;
    ImageButton save;
    View nextButton;

    WifiScanner wifi;

    Place p;
    ArrayList<Room> rooms;
    int currentRoom;

    DatabaseManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_new_scan);
        //navigationView = findViewById(R.id.navigation_view_new_scan);
        //navigationView.setEnabled(false);//A voir si ca marche

        dm = new DatabaseManager(getBaseContext());

        wifi = new WifiScanner(getApplicationContext());
        ping = findViewById(R.id.d_ping_new_scan);
        strength = findViewById(R.id.d_strength_new_scan);
        lost = findViewById(R.id.d_lost_new_scan);
        dl = findViewById(R.id.d_dl_new_scan);
        refresh = findViewById(R.id.d_refresh_new_scan);
        save = findViewById(R.id.d_validate_new_scan);
        wifiName = findViewById(R.id.d_wifi_name_new_scan);
        wifiName.setText(wifi.getWifiName());
        workInProgress = findViewById(R.id.d_work_in_progress);
        workInProgress.setVisibility(View.GONE);

        autoComplete = findViewById(R.id.d_text_edit_new_scan);

        String placeName = getIntent().getStringExtra("place");
        p = dm.readPlace(placeName).get(0);
        rooms = dm.readRoom(p);
        save.setEnabled(true);

        currentRoom = 0;
        autoComplete.setEnabled(false);

        nextButton = findViewById(R.id.d_next_new_scan);
        setCurrentRoom();
        launch_test(null);
    }

    public void launch_test(View v){
        workInProgress.setText("Work in progress...");
        workInProgress.setVisibility(View.VISIBLE);
        Toast.makeText(getBaseContext(), "Test in progress. Stay where you are! ", Toast.LENGTH_LONG).show();

        if(!wifi.isWifiEnabled()) {//Check one more time
            openDialog();
            wifiName.setText(wifi.getWifiName());
        }

        refresh.setEnabled(false);
        save.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                wifi.update();
                ping.post(new Runnable() {
                    @Override
                    public void run() {
                        wifiName.setText(wifi.getWifiName());
                        if (wifi.getPing() == (float)-1 || wifi.getProportionOfLost() == (float)-1 || wifi.getStrength() == (float)0){

                            ping.setText("_");
                            lost.setText("_");
                            strength.setText(wifi.getStrength() + " %");
                            dl.setText("_");

                            refresh.setEnabled(true);
                            save.setEnabled(true);
                            nextButton.setEnabled(true);
                            workInProgress.setText("The connection failed. Try later");

                            Toast.makeText(getBaseContext(), "Error. Check your connection, and try later.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            ping.setText(wifi.getPing() + " ms");
                            lost.setText(wifi.getProportionOfLost() + " %");
                            strength.setText(wifi.getStrength() + " %");
                            dl.setText(wifi.getDl() + " ms");

                            refresh.setEnabled(true);
                            save.setEnabled(true);
                            nextButton.setEnabled(true);
                            workInProgress.setVisibility(View.GONE);
                            nextButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

    public void openDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Wi-Fi disabled");
        alertDialog.setMessage("Do you want turn on your Wi-Fi?");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(),"Sorry this app cannot work without Wi-Fi",Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                wifi.enableWifi();
            }

        });

        alertDialog.show();
    }

    public void saveData(View v) {
        Room r = rooms.get(currentRoom);//Prendre le cas si y en a plusieurs aussi peut etre
        Scan_information info = new Scan_information(r, wifi.getStrength(), wifi.getPing(), wifi.getProportionOfLost(), wifi.getDl(), null);
        dm.insertScan(info);
        Toast.makeText(getBaseContext(), "Scan saved : " + r.getRoom_name(), Toast.LENGTH_LONG).show();
    }

    public void nextRoom(View v){
        saveData(v);
        currentRoom++;
        if(currentRoom == rooms.size()){//Finish scan
            dm.close();
            Toast.makeText(getBaseContext(), "Global scan finished", Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            setCurrentRoom();
            launch_test(null);
        }
    }

    private void setCurrentRoom(){
        ping.setText("?");
        strength.setText("?");
        lost.setText("?");
        dl.setText("?");

        autoComplete.setText(rooms.get(currentRoom).getRoom_name());
        save.setEnabled(false);
        save.setVisibility(View.GONE);
        nextButton.setEnabled(false);
        nextButton.setVisibility(View.GONE);
    }
}
