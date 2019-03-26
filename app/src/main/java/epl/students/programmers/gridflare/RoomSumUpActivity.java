package epl.students.programmers.gridflare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import epl.students.programmers.gridflare.tools.Scan_information;

public class RoomSumUpActivity extends AppCompatActivity {

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

    private TextView room;
    private TextView numberOfScans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_sum_up_activity);
        setTitle("Room sum up");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        progressBar_strength = findViewById(R.id.progressBar_strength);
        progressBar_ping = findViewById(R.id.progressBar_ping);
        progressBar_lost = findViewById(R.id.progressBar_lost);
        progressBar_Dl = findViewById(R.id.progressBar_Dl);
        strength_value = findViewById(R.id.strength_value);
        ping_value = findViewById(R.id.ping_value);
        lost_value = findViewById(R.id.lost_value);
        dl_value = findViewById(R.id.Dl_value);
        strength_title = findViewById(R.id.strength_title);
        ping_title = findViewById(R.id.ping_title);
        lost_title = findViewById(R.id.lost_title);
        dl_title = findViewById(R.id.dl_title);
        room = findViewById(R.id.room_sumup);
        numberOfScans = findViewById(R.id.number_of_scans_sumup);

        Intent i = getIntent();
        Scan_information myScan =  (Scan_information) i.getParcelableExtra("theRoom");

        room.setText(myScan.getRoom().toString().toUpperCase());
        numberOfScans.setText(String.format("%s%d", getString(R.string.number_scan), myScan.getNumberOfScans()));
        strength_title.setText(R.string.strength);
        ping_title.setText(R.string.ping_2);
        lost_title.setText(R.string.lost);
        dl_title.setText(R.string.time);
        progressBar_strength.setProgress(myScan.getStrength());
        strength_value.setText(String.format("%d %%", myScan.getStrength()));
        float ping = myScan.getPing();
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
        ping_value.setText(String.format("%s ms", truncate(myScan.getPing())));
        progressBar_lost.setProgress((int)myScan.getProportionOfLost());
        lost_value.setText(String.format("%s %%", truncate(myScan.getProportionOfLost())));
        float dl = myScan.getDl();
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
        dl_value.setText(String.format("%s", truncate(myScan.getDl())));

    }

    //Back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static String truncate(float i){
        int tmp = (int)(i*Math.pow(10, 2));
        return Float.toString((float)(tmp/Math.pow(10, 2)));
    }
}
