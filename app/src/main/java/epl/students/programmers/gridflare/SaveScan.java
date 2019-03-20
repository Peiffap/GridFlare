package epl.students.programmers.gridflare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Scan_information;

public class SaveScan extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    private String ROOM;
    private Scan_information myScan;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_scan);
        setTitle("Save th scan");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        spinner = findViewById(R.id.spinner_save);
        ArrayAdapter<CharSequence> spinner_items = ArrayAdapter.createFromResource(this, R.array.spinner_items,android.R.layout.simple_spinner_item);
        spinner_items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_items);
        spinner.setOnItemSelectedListener(this);

        Intent i = getIntent();

        myScan = new Scan_information();//i.getParcelableExtra("Scan");


        getWindow().setLayout((int)(width*0.8), (int) (height*0.6));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //Spinner method
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(spinner.getVisibility() == View.VISIBLE){
            String text = adapterView.getItemAtPosition(i).toString();
            if(text.equals("Select a Room")){
                Toast.makeText(adapterView.getContext(),"Select a room please",Toast.LENGTH_LONG).show();
            }
            else {
                ROOM = text;
                text = "You are at :" + text;
                Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_LONG).show();

            }
        }
    }

    public void cancelSave(View v){
        this.finish();
    }

    public void confirmSave(AdapterView<?> adapterView, View v){
        myScan.setRoom(ROOM);
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.insertScan(myScan);
        databaseManager.close();

        String text = "This scan has been correctly saved";
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_LONG).show();
        this.finish();
    }

}
