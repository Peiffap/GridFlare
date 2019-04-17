package epl.students.programmers.gridflare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Adapter_Rooms;
import epl.students.programmers.gridflare.tools.GlobalScan;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.RecyclerItemClickListener;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;

import static android.widget.Toast.makeText;

public class RoomsActivity extends AppCompatActivity {

    ArrayList<Room> rooms;
    Place myPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        setTitle("Rooms");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        Intent i = getIntent();
        myPlace =  (Place) i.getParcelableExtra("thePlace");

        displayData();
    }

    private void displayData(){
        final DatabaseManager databaseManager = new DatabaseManager(getBaseContext());

        rooms = databaseManager.readRoom(myPlace);

        RecyclerView recyclerView = findViewById(R.id.recycleView_rooms);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                // Do nothing
            }

            @Override public void onLongItemClick(View view, int position) {
                Room theRoom = rooms.get(position);
                openDialogUpgrade(theRoom);
            }
        }));

        Adapter_Rooms adapter = new Adapter_Rooms(rooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        databaseManager.close();
    }

    private void openDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.edittext_dialog,null);
        alertDialog.setTitle("Add a new room");

        final EditText room_name = view.findViewById(R.id.add_room_place_name);
        room_name.setHint("Enter the room name");
        final EditText room_floor = view.findViewById(R.id.add_room_floor);
        room_floor.setHint("Enter the floor of this room");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                makeText(getBaseContext(),room_name.getText().toString() + "::" + room_floor.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String room = room_name.getText().toString();
                int floor;
                try{
                    floor = Integer.parseInt(room_floor.getText().toString());
                } catch (Exception e){
                    Toast.makeText(getBaseContext(), "Please enter the  floor number", Toast.LENGTH_LONG).show();
                    return;
                }
                DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                if(databaseManager.readRoom(room,floor, myPlace).size() == 0) {
                    Room tmp = new Room(room, floor, myPlace);
                    databaseManager.insertRoom(tmp);
                    Toast.makeText(getBaseContext(),tmp.toString(),Toast.LENGTH_LONG).show();
                    displayData();
                }
                else
                    Toast.makeText(getBaseContext(),"Already saved",Toast.LENGTH_LONG).show();
                databaseManager.close();
            }

        });


        alertDialog.setView(view);
        alertDialog.show();
    }

    private void openDialogUpgrade(final Room room){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.edittext_dialog,null);
        alertDialog.setTitle("Modify this room");

        final EditText room_name = view.findViewById(R.id.add_room_place_name);
        room_name.setText(room.getRoom_name());
        final EditText room_floor = view.findViewById(R.id.add_room_floor);
        room_floor.setText(""+room.getFloor());

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                makeText(getBaseContext(),room_name.getText().toString() + "::" + room_floor.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String room_n = room_name.getText().toString();
                int floor = Integer.parseInt(room_floor.getText().toString());
                DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                room.setRoom_name(room_n);
                room.setFloor(floor);
                databaseManager.updateRoom(room);

                databaseManager.close();
            }

        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                openDialogDelete(room);
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    public void openDialogDelete(final Room room){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_base,null);
        alertDialog.setTitle("Delete this room");



        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                ArrayList<Scan_information> scans_for_this_room = databaseManager.readScan(room.getRoom_name());
                for(Scan_information si: scans_for_this_room){
                    databaseManager.deleteScan(si);
                }
                databaseManager.deleteRoom(room);
                databaseManager.close();
                makeText(getBaseContext(),"Room " + room.getRoom_name() +" deleted", Toast.LENGTH_LONG).show();
            }

        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    public void go_to_historic(View v){
        Intent intent = new Intent(this, HistoricGlobalScanActivity.class);
        intent.putExtra("thePlace", myPlace);
        startActivity(intent);
    }

    public void go_to_global_scan(View v){
        openCreateGlobalTestConfirmation(v);
    }

    protected void openCreateGlobalTestConfirmation(View v){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_base, null);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Do you want to start a new global scan?");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                makeText(getBaseContext(),"Cancelled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                GlobalScan scan = new GlobalScan(new Date(), myPlace);
                DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                databaseManager.insertGlobalScan(scan);
                databaseManager.close();

                Intent intent = new Intent(getApplicationContext(), GlobalTestRoomsActivity.class);
                intent.putExtra("thePlace", myPlace);
                intent.putExtra("theGlobal", scan);

                makeText(getBaseContext(),"New global scan started",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                startActivity(intent);
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
