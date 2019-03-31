package epl.students.programmers.gridflare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Adapter_Rooms;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.Room;

import static android.widget.Toast.makeText;

public class GlobalTestRoomsActivity extends AppCompatActivity {

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
        DatabaseManager databaseManager = new DatabaseManager(getBaseContext());

        rooms = databaseManager.readRoom(myPlace);

        RecyclerView recyclerView = findViewById(R.id.recycleView_rooms);

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
                int floor = Integer.parseInt(room_floor.getText().toString());
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
