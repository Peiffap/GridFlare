package epl.students.programmers.gridflare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Adapter_Places;
import epl.students.programmers.gridflare.tools.Adapter_Rooms;
import epl.students.programmers.gridflare.tools.Adapter_Scan_information;
import epl.students.programmers.gridflare.tools.GlobalScan;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.RecyclerItemClickListener;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;

import static android.widget.Toast.makeText;

public class PlacesActivity extends AppCompatActivity {

    ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        setTitle("Places");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable


        displayData();
    }

    private void displayData(){
        DatabaseManager databaseManager = new DatabaseManager(getBaseContext());

        places = databaseManager.readPlace();

        RecyclerView recyclerView = findViewById(R.id.recycleView_places);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getBaseContext(), RoomsActivity.class);
                        intent.putExtra("thePlace", places.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        openDialogUpgrade(places.get(position));
                    }
                })
        );
        Adapter_Places adapter = new Adapter_Places(places);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        databaseManager.close();
    }

    private void openDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.edittext_dialog,null);
        alertDialog.setTitle("Add a new place");

        final EditText place_name = view.findViewById(R.id.add_room_place_name);
        place_name.setHint("Enter the name of the place/building");
        final EditText number_of_floors = view.findViewById(R.id.add_room_floor);
        number_of_floors.setHint("Enter the number of floors here");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                makeText(getBaseContext(),place_name.getText().toString() + "::" + number_of_floors.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String place = place_name.getText().toString();
                int floor;
                try{
                    floor = Integer.parseInt(number_of_floors.getText().toString());
                } catch (Exception e){
                    Toast.makeText(getBaseContext(), "Please enter the number of floor", Toast.LENGTH_LONG).show();
                    return;
                }
                DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                if(databaseManager.readRoom(place,floor).size() == 0) {
                    Place tmp = new Place(place, floor);
                    databaseManager.insertPlace(tmp);
                    Toast.makeText(getBaseContext(),tmp.toString(),Toast.LENGTH_SHORT).show();
                    displayData();
                }
                else
                    Toast.makeText(getBaseContext(),"Already saved",Toast.LENGTH_SHORT).show();
                databaseManager.close();
            }

        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    public void openDialogUpgrade(final Place place){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.edittext_dialog,null);
        alertDialog.setTitle("Modify this room");

        final EditText place_name = view.findViewById(R.id.add_room_place_name);
        place_name.setText(place.getPlace_name());
        final EditText place_floor = view.findViewById(R.id.add_room_floor);
        place_floor.setText(""+place.getNumber_of_floor());

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                makeText(getBaseContext(),place_name.getText().toString() + "::" + place_floor.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String place_n = place_name.getText().toString();
                int floor = Integer.parseInt(place_floor.getText().toString());
                DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                place.setPlace_name(place_n);
                place.setNumber_of_floor(floor);
                databaseManager.updatePlace(place);

                databaseManager.close();

                alertDialog.dismiss();
            }

        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                openDialogDelete(place);
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    public void openDialogDelete(final Place place){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_base,null);
        alertDialog.setTitle("Delete this place");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                databaseManager.deletePlace(place);
                ArrayList<Room> all_rooms_for_this_place = databaseManager.readRoom(place);
                for(Room room: all_rooms_for_this_place){
                    ArrayList<Scan_information> scans_for_this_room = databaseManager.readScan(room.getRoom_name());
                    for(Scan_information si: scans_for_this_room){
                        databaseManager.deleteScan(si);
                    }
                    databaseManager.deleteRoom(room);
                }
                ArrayList<GlobalScan> all_global_scans_for_this_place = databaseManager.readGlobal(place);
                for(GlobalScan gl: all_global_scans_for_this_place){
                    databaseManager.deleteGlobalScan(gl);
                }
                databaseManager.deletePlace(place);
                databaseManager.close();
                makeText(getBaseContext(),"Place " + place.getPlace_name() +" deleted", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }

        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rooms_activity_menu, menu);
        return true;
    }

    //Back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_rooms_activity:
                openDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
