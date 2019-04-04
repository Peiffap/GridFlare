package epl.students.programmers.gridflare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Data;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;
import epl.students.programmers.gridflare.tools.WifiScanner;
import me.itangqi.waveloadingview.WaveLoadingView;

import static android.widget.Toast.makeText;

public class ScanningActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    WifiScanner wifi;

    private WaveLoadingView waveLoadingView;

    private ProgressBar progressBar_strength;
    private ProgressBar progressBar_ping;
    private ProgressBar progressBar_lost;
    private ProgressBar progressBar_Dl;

    private TextView strength_value;
    private TextView ping_value;
    private TextView lost_value;
    private TextView dl_value;
    private TextView strength_title;
    private TextView ping_title;
    private TextView lost_title;
    private TextView dl_title;

    private Button saveButton;
    private Button launchScan;

    private Room ROOM;
    private boolean alreadySaved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_scanning);
        setTitle("Scanning");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        wifi = new WifiScanner(getApplicationContext());
        progressBar_strength = findViewById(R.id.progressBar_strength);
        progressBar_strength.setVisibility(View.INVISIBLE);
        progressBar_ping = findViewById(R.id.progressBar_ping);
        progressBar_ping.setVisibility(View.INVISIBLE);
        progressBar_lost = findViewById(R.id.progressBar_lost);
        progressBar_lost.setVisibility(View.INVISIBLE);
        progressBar_Dl = findViewById(R.id.progressBar_Dl);
        progressBar_Dl.setVisibility(View.INVISIBLE);
        strength_value = findViewById(R.id.strength_value);
        ping_value = findViewById(R.id.ping_value);
        lost_value = findViewById(R.id.lost_value);
        dl_value = findViewById(R.id.Dl_value);
        strength_title = findViewById(R.id.strength_title);
        ping_title = findViewById(R.id.ping_title);
        lost_title = findViewById(R.id.lost_title);
        dl_title = findViewById(R.id.dl_title);
        waveLoadingView = findViewById(R.id.waveLoading);
        waveLoadingView.setProgressValue(50);
        waveLoadingView.setVisibility(View.INVISIBLE);

        saveButton = findViewById(R.id.save_scan);
        saveButton.setVisibility(View.GONE);
        launchScan = findViewById(R.id.button);

        alreadySaved = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Start a scan
    public void launch_a_test(View v){
        alreadySaved = false;
        saveButton.setVisibility(View.INVISIBLE);
        launchScan.setVisibility(View.INVISIBLE);
        progressBar_strength.setVisibility(View.INVISIBLE);
        progressBar_ping.setVisibility(View.INVISIBLE);
        progressBar_lost.setVisibility(View.INVISIBLE);
        progressBar_Dl.setVisibility(View.INVISIBLE);
        strength_title.setText("");
        ping_title.setText("");
        lost_title.setText("");
        dl_title.setText("");
        strength_value.setText("");
        ping_value.setText("");
        lost_title.setText("");
        dl_title.setText("");
        Toast.makeText(getBaseContext(), "Test in progress. Stay where you are! ", Toast.LENGTH_LONG).show();

        if(!wifi.isWifiEnabled())//Check one more time
            openDialog();

        final TextView value = findViewById(R.id.launch_value);
        value.setText(R.string.working);
        waveLoadingView.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {//Thread because the ping is a blocking function
            @Override
            public void run() {
                wifi.update();
                //Update SQL
                //Il faudra encore ajouter la position et le DL
                //Controller.getInstance().getSQLiteInterface(getBaseContext()).addPoint(0, 0, wifi.getStrength(), wifi.getPing(), 0);
                //Update View
                value.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (wifi.getPing() == (float)-1 || wifi.getProportionOfLost() == (float)-1 || wifi.getStrength() == (float)-1){
                            value.setText("Error. Check your connection, and try later.");
                        }
                        else {
                            waveLoadingView.setVisibility(View.INVISIBLE);
                            progressBar_strength.setVisibility(View.VISIBLE);
                            progressBar_ping.setVisibility(View.VISIBLE);
                            progressBar_lost.setVisibility(View.VISIBLE);
                            progressBar_Dl.setVisibility(View.VISIBLE);
                            saveButton.setVisibility(View.VISIBLE);
                            launchScan.setVisibility(View.VISIBLE);
                            strength_title.setText(R.string.strength);
                            ping_title.setText(R.string.ping_2);
                            lost_title.setText(R.string.lost);
                            dl_title.setText(R.string.time);
                            progressBar_strength.setProgress(wifi.getStrength());
                            strength_value.setText(wifi.getStrength()+" %");
                            float ping = wifi.getPing();
                            if(ping < 31){
                                progressBar_ping.setProgress(100);
                            }
                            else if(ping < 50){
                                progressBar_ping.setProgress(75);
                            }
                            else if( ping < 70){
                                progressBar_ping.setProgress(50);
                            }
                            else {
                                progressBar_ping.setProgress(25);
                            }
                            ping_value.setText(wifi.getPing()+" ms");
                            progressBar_lost.setProgress((int)wifi.getProportionOfLost());
                            lost_value.setText(wifi.getProportionOfLost()+" %");
                            float dl = wifi.getDl();
                            if(dl < 8999){
                                progressBar_Dl.setProgress(100);
                            }
                            else if(dl < 10000){
                                progressBar_Dl.setProgress(75);
                            }
                            else if(dl < 11000){
                                progressBar_Dl.setProgress(50);
                            }
                            else{
                                progressBar_Dl.setProgress(25);
                            }
                            dl_value.setText(wifi.getDl()+"");


                            value.setText("Done");
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

    //Back button
    @Override
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

    //Spinner method

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void openDialogSave(View v){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
        alertDialog.setTitle("Save this scan");
        alertDialog.setMessage("Do you want to save this scan ?");

        final Spinner mySpinner = (Spinner) view.findViewById(R.id.spinner_dialog);

        final DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
        ArrayAdapter<Place> spinner_items = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, databaseManager.readPlace());
        spinner_items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(spinner_items);
        mySpinner.setOnItemSelectedListener(this);

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                makeText(getBaseContext(),"Save cancelled",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "NEXT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                if(alreadySaved){
                    makeText(getBaseContext(), "This scan is already saved", Toast.LENGTH_LONG).show();
                }
                else if(!mySpinner.getSelectedItem().toString().equals("Select a Place")){

                    Place tmp = (Place) mySpinner.getSelectedItem();

                    openDialogSave2(databaseManager.readPlace(tmp.getPlace_name()));

                    databaseManager.close();
                    dialog.dismiss();
                }
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    public void openDialogSave2(ArrayList<Place> places){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
        alertDialog.setTitle("Save this scan");
        alertDialog.setMessage("Do you want to save this scan ?");

        final Spinner mySpinner = (Spinner) view.findViewById(R.id.spinner_dialog);

        final DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
        ArrayAdapter<Room> spinner_items = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, databaseManager.readRoomFromPlace(places.get(0).getPlace_name()));
        spinner_items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(spinner_items);
        mySpinner.setOnItemSelectedListener(this);

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                makeText(getBaseContext(),"Save cancelled",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "NEXT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                if (alreadySaved) {
                    makeText(getBaseContext(), "This scan is already saved", Toast.LENGTH_LONG).show();
                } else if (!mySpinner.getSelectedItem().toString().equals("Select a Room")) {
                    alreadySaved = true;
                    ROOM = (Room) mySpinner.getSelectedItem();
                    String text = "You are at : " + ROOM;

                    DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                    ArrayList<Data> datas = databaseManager.readData(-1);
                    if (datas.size() == 0)
                        databaseManager.insertScan(new Scan_information(ROOM, wifi.getStrength(), wifi.getPing(), wifi.getProportionOfLost(), wifi.getDl(), new Data(-1, new Date())));
                    else
                        databaseManager.insertScan(new Scan_information(ROOM, wifi.getStrength(), wifi.getPing(), wifi.getProportionOfLost(), wifi.getDl(), datas.get(0)));

                    Log.i("DATABASE", "reuse room");

                    databaseManager.close();

                    makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();

                    dialog.dismiss();
                }
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }
}