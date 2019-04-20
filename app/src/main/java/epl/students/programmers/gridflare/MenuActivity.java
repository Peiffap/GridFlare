package epl.students.programmers.gridflare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.EmailBot;
import epl.students.programmers.gridflare.tools.MenuPlacesAdapter;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;


public class MenuActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    View popupPlace;
    View popupEmail;
    RecyclerView rv;

    DatabaseManager dm;

    private int currentDisplayed;
    private View currentDisplayedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_places);
        navigationView = findViewById(R.id.navigation_view_places);
        navigationView.setSelectedItemId(R.id.main_menu_btn);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.live_menu_btn:
                        dm.close();
                        Intent intent = new Intent(getBaseContext(), LiveScanningActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        break;
                    case R.id.scan_menu_btn:
                        dm.close();
                        Intent intent2 = new Intent(getBaseContext(), NewScanActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        break;
                }
                return true;
            }
        });
        dm = new DatabaseManager(getBaseContext());
        currentDisplayed = -1;

        popupPlace = findViewById(R.id.d_popup_new_place);
        ((ViewGroup) popupPlace.getParent()).removeView(popupPlace);

        closeEmail(null);

        rv = findViewById(R.id.d_places_scroll);
        MenuPlacesAdapter adapter = new MenuPlacesAdapter(dm.readPlace());
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false));
        rv.setAdapter(adapter);
    }

    public void displayRoomData(View v){
        int roomID = (int)((View)v.getParent()).getTag();//Verifier qu'il arrive bien
        if(currentDisplayed == roomID) {
            removeDisplayedData(currentDisplayedView);
            currentDisplayedView = null;
            currentDisplayed = -1;
            return;
        } else if(currentDisplayed != -1){
            removeDisplayedData(currentDisplayedView);
        }
        Scan_information scan = dm.readLastScan(roomID);//Veriifer aussi qu'il y ai quelque chose a display

        if(scan == null){
            Toast.makeText(getBaseContext(),"Scan this room before doing this",Toast.LENGTH_LONG).show();
            currentDisplayed = -1;
            return;
        }
        currentDisplayed = roomID;
        currentDisplayedView = v;

        LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
        View result_template = layoutInflater.inflate(R.layout.template_result,null,false);

        ((TextView)result_template.findViewById(R.id.d_ping_result)).setText(scan.getPing() + " ms");
        ((TextView)result_template.findViewById(R.id.d_strength_result)).setText(scan.getStrength() + " %");
        ((TextView)result_template.findViewById(R.id.d_dl_result)).setText(scan.getDl() + " ms");
        ((TextView)result_template.findViewById(R.id.d_lost_result)).setText(scan.getProportionOfLost() + " %");

        ((LinearLayout)v.getParent()).addView(result_template);
    }

    public void removeDisplayedData(View v){
        ((LinearLayout)v.getParent()).removeViewAt(1);
    }

    public void newRoomPopup(View v){
        LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
        View new_template = layoutInflater.inflate(R.layout.template_new_room,null,false);
        ((LinearLayout)v.getParent().getParent()).addView(new_template);
    }

    public void validateNewRoom(View v){
        View popup = (View)v.getParent();//Voir si pas mieux de cast plus
        String name; int floor;
        try {
            name = ((TextView) popup.findViewById(R.id.d_new_room_name)).getText().toString();
            floor = Integer.parseInt(((TextView) popup.findViewById(R.id.d_new_room_floor)).getText().toString());//Verifier que l'étage correspond
            if(name == null || name == ""){
                throw new Exception();
            }
        } catch (Exception e){
            Toast.makeText(getBaseContext(),"Enter valid information before",Toast.LENGTH_LONG).show();
            cancelNewRoom(v);
            return;
        }
        String place_name = ((TextView)((LinearLayout)popup.getParent()).findViewById(R.id.d_place_name)).getText().toString();
        ArrayList<Place> p = dm.readPlace(place_name);//Encore une fois je pars du principe que pas de doublons
        Room r = new Room(name, floor, p.get(0));
        dm.insertRoom(r);
        Toast.makeText(getBaseContext(),"New room created",Toast.LENGTH_LONG).show();
        cancelNewRoom(v);
    }

    public void cancelNewRoom(View v){
        View popup = (View)v.getParent();
        ((LinearLayout)popup.getParent()).removeView(popup);
    }

    public void deleteRoom(View v){
        int roomID = (int)((View)v.getParent().getParent()).getTag();
        dm.deleteRoom(dm.readRoom(roomID));
        Toast.makeText(getBaseContext(),"Room deleted",Toast.LENGTH_LONG).show();
    }

    String placeName;

    public void shareButton(View v){
        placeName = ((TextView)((View)v.getParent()).findViewById(R.id.d_place_name)).getText().toString();
        ((RelativeLayout)findViewById(R.id.d_menu_container)).addView(popupEmail);
    }

    public void confirmEmail(View v){
        final EmailBot bot = new EmailBot();
        final String email = ((TextView)findViewById(R.id.d_email_address)).getText().toString();
        if(email != "" && placeName != "")
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bot.sendScanReport(email, placeName, getBaseContext());
                }
            }).start();
        closeEmail(v);
    }

    public void closeEmail(View v){
        popupEmail = findViewById(R.id.d_popup_email);
        ((ViewGroup) popupEmail.getParent()).removeView(popupEmail);
    }
}
