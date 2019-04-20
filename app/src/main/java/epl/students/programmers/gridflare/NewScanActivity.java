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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;
import epl.students.programmers.gridflare.tools.WifiScanner;

public class NewScanActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    AutoCompleteTextView autoComplete;

    TextView ping;
    TextView strength;
    TextView lost;
    TextView dl;
    TextView wifiName;

    TextView nextButton;

    ImageButton refresh;
    ImageButton save;//Encore faire son listener
                    //Aussi encore faire le text qui s'auto-complete et ducoup la partie save
                    //Et mettre le nom du wifi
                    //Et pq pas remettre l'animation en bas ce serait cool

    WifiScanner wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_new_scan);
        navigationView = findViewById(R.id.navigation_view_new_scan);
        navigationView.setSelectedItemId(R.id.scan_menu_btn);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.live_menu_btn:
                        Intent intent = new Intent(getBaseContext(), LiveScanningActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        break;
                    case R.id.main_menu_btn:
                        Intent intent2 = new Intent(getBaseContext(), MenuActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        break;
                }
                return true;
            }
        });
        wifi = new WifiScanner(getApplicationContext());
        ping = findViewById(R.id.d_ping_new_scan);
        strength = findViewById(R.id.d_strength_new_scan);
        lost = findViewById(R.id.d_lost_new_scan);
        dl = findViewById(R.id.d_dl_new_scan);
        refresh = findViewById(R.id.d_refresh_new_scan);
        save = findViewById(R.id.d_validate_new_scan);
        wifiName = findViewById(R.id.d_wifi_name_new_scan);
        wifiName.setText(wifi.getWifiName());
        //Le next btn est désactiver pour le moment
        nextButton = findViewById(R.id.d_next_new_scan);
        nextButton.setText("");

        autoComplete = findViewById(R.id.d_text_edit_new_scan);
        setupAutoCompleteTextView();

        //launch_test();
    }

    public void launch_test(View v){
        Toast.makeText(getBaseContext(), "Test in progress. Stay where you are! ", Toast.LENGTH_LONG).show();

        if(!wifi.isWifiEnabled())//Check one more time
            openDialog();

        refresh.setEnabled(false);
        save.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                wifi.update();
                ping.post(new Runnable() {
                    @Override
                    public void run() {
                        if (wifi.getPing() == (float)-1 || wifi.getProportionOfLost() == (float)-1 || wifi.getStrength() == (float)-1){
                            Toast.makeText(getBaseContext(), "Error. Check your connection, and try later.", Toast.LENGTH_LONG).show();
                        }
                        ping.setText(wifi.getPing() +" ms");
                        lost.setText(wifi.getProportionOfLost()+" %");
                        strength.setText(wifi.getStrength()+" %");
                        dl.setText(wifi.getDl()+"");

                        refresh.setEnabled(true);
                        save.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    private void setupAutoCompleteTextView(){
        DatabaseManager dm = new DatabaseManager(getBaseContext());
        ArrayList<Room> rooms = dm.readRoom();
        String[] names = new String[rooms.size()];
        for(int i = 0; i < rooms.size(); i++){
            names[i] = rooms.get(i).getRoom_name();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, names);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);
        dm.close();
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

    public void saveData(View v){
        //Asserts
        DatabaseManager dm = new DatabaseManager(getBaseContext());
        ArrayList<Room> rooms = dm.readRoom(autoComplete.getText().toString());
        if(rooms.size() == 0)
            Toast.makeText(getBaseContext(),"This room does not exists",Toast.LENGTH_LONG).show();
        else {
            Room r = rooms.get(0);//Prendre le cas si y en a plusieurs aussi peut etre
            Scan_information info = new Scan_information(r, wifi.getStrength(), wifi.getPing(), wifi.getProportionOfLost(), wifi.getDl(), null);
            dm.insertScan(info);
            dm.close();
            Toast.makeText(getBaseContext(),"Scan saved",Toast.LENGTH_LONG).show();
        }

    }
}
