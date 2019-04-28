package epl.students.programmers.gridflare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.EmailBot;
import epl.students.programmers.gridflare.tools.MenuPlacesAdapter;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;


public class MenuActivity extends Fragment implements View.OnClickListener{

    View popupPlace;
    View popupEmail;
    RecyclerView rv;
    RelativeLayout container_view;
    TextView emailAddress;
    TextView newPlaceName;

    DatabaseManager dm;

    private int currentDisplayed;
    private View currentDisplayedView;

    MenuPlacesAdapter menuAdapter;

    public static Fragment newInstance() {
        return (new MenuActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.d_places, container, false);

        dm = new DatabaseManager(getActivity());
        currentDisplayed = -1;

        container_view = v.findViewById(R.id.d_menu_container);
        emailAddress = v.findViewById(R.id.d_email_address);
        newPlaceName = v.findViewById(R.id.d_new_place_name);

        setupButtons(v);

        popupPlace = v.findViewById(R.id.d_popup_new_place);
        ((ViewGroup) popupPlace.getParent()).removeView(popupPlace);
        popupEmail = v.findViewById(R.id.d_popup_email);
        closeEmail(null);

        rv = v.findViewById(R.id.d_places_scroll);
        menuAdapter = new MenuPlacesAdapter(dm.readPlace());
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false));
        rv.setAdapter(menuAdapter);

        return v;
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.add_place_btn:
                newPlacePopup(v);
                break;
            case R.id.confirm_email:
                confirmEmail(v);
                break;
            case R.id.cancel_email:
                closeEmail(v);
                break;
            case R.id.validate_new_place_btn:
                validateNewPlace(v);
                break;
            case R.id.cancel_new_place_btn:
                cancelNewPlace(v);
                break;
        }
    }

    private void setupButtons(View v){
        v.findViewById(R.id.add_place_btn).setOnClickListener(this);
        v.findViewById(R.id.confirm_email).setOnClickListener(this);
        v.findViewById(R.id.cancel_email).setOnClickListener(this);
        v.findViewById(R.id.validate_new_place_btn).setOnClickListener(this);
        v.findViewById(R.id.cancel_new_place_btn).setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
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
        Scan_information scan = dm.readLastScan(roomID);//Verifier aussi qu'il y ait quelque chose a display

        if(scan == null){
            Toast.makeText(getActivity(),"Scan this room before doing this.",Toast.LENGTH_LONG).show();
            currentDisplayed = -1;
            return;
        }
        currentDisplayed = roomID;
        currentDisplayedView = v;

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View result_template = layoutInflater.inflate(R.layout.template_result,null,false);

        ((TextView)result_template.findViewById(R.id.d_ping_result)).setText(scan.getPing() + " ms");
        ((TextView)result_template.findViewById(R.id.d_strength_result)).setText(scan.getStrength() + " %");
        ((TextView)result_template.findViewById(R.id.d_dl_result)).setText(scan.getDl() + " Mbps");
        ((TextView)result_template.findViewById(R.id.d_lost_result)).setText(scan.getProportionOfLost() + " %");

        ((LinearLayout)v.getParent()).addView(result_template);
        v.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.rounded_corner_blue_border));
    }

    public void removeDisplayedData(View v){
        ((LinearLayout)v.getParent()).removeViewAt(1);
        v.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.rounded_corner_medium));
    }

    public void newRoomPopup(View v){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View new_template = layoutInflater.inflate(R.layout.template_new_room,null,false);
        ((LinearLayout)v.getParent().getParent()).addView(new_template);
    }

    public void validateNewRoom(View v){
        View popup = (View) v.getParent();//Voir si pas mieux de cast plus
        String name; int floor;
        String place_name = ((TextView)((LinearLayout)popup.getParent()).findViewById(R.id.d_place_name)).getText().toString();
        ArrayList<Place> p = dm.readPlace(place_name);//Encore une fois je pars du principe que pas de doublons
        Place pl = p.get(0);
        try {
            name = ((TextView) popup.findViewById(R.id.d_new_room_name)).getText().toString();
            floor = Integer.parseInt(((TextView) popup.findViewById(R.id.d_new_room_floor)).getText().toString());//Verifier que l'étage correspond
            if(Objects.equals(name, "") || floor > pl.getNumber_of_floor()){
                throw new Exception();
            }
        } catch (Exception e){
            Toast.makeText(getActivity(),"Enter valid information first.",Toast.LENGTH_LONG).show();
            cancelNewRoom(v);
            return;
        }
        Room r = new Room(name, floor, pl);
        dm.insertRoom(r);
        menuAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(),"New room created.",Toast.LENGTH_LONG).show();
        cancelNewRoom(v);
    }

    public void cancelNewRoom(View v){
        closeKeyboard();
        View popup = (View)v.getParent();
        ((LinearLayout)popup.getParent()).removeView(popup);
    }

    public void deleteRoom(View v){
        int roomID = (int)((View)v.getParent().getParent()).getTag();
        Room r = dm.readRoom(roomID);
        dm.deleteRoom(r);
        menuAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(),"Room deleted.",Toast.LENGTH_LONG).show();
    }

    String placeName;

    public void shareButton(View v){
        placeName = ((TextView)((View)v.getParent()).findViewById(R.id.d_place_name)).getText().toString();
        container_view.addView(popupEmail);
    }

    public void confirmEmail(View v){
        final EmailBot bot = new EmailBot();
        final String email = emailAddress.getText().toString();
        if (!Objects.equals(email, "") && !Objects.equals(placeName, ""))//Peut etre mettre un popup sinon
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bot.sendScanReport(email, placeName, getActivity());
                }
            }).start();
        closeEmail(v);
    }

    public void closeEmail(View v){
        closeKeyboard();
        ((ViewGroup) popupEmail.getParent()).removeView(popupEmail);
    }

    public void newPlacePopup(View v){
        container_view.addView(popupPlace);
    }

    public void validateNewPlace(View v){
        final String placeName = newPlaceName.getText().toString();
        if (!Objects.equals(placeName, "")){
            ArrayList<Place> samePlaces = dm.readPlace(placeName);
            if(samePlaces.size() == 0) { // No duplicate
                Place p = new Place(placeName, 1);
                dm.insertPlace(p);
                menuAdapter.updateList(dm.readPlace());
                menuAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "New place created.", Toast.LENGTH_LONG).show();
                cancelNewPlace(v);
            }
            else { // Duplicate
                Toast.makeText(getActivity(), "This place already exists", Toast.LENGTH_LONG).show();
                cancelNewPlace(v);
            }
        } else {
            Toast.makeText(getActivity(),"Please enter a place name.",Toast.LENGTH_LONG).show();
        }
    }

    public void cancelNewPlace(View v){
        closeKeyboard();
        ((ViewGroup) popupPlace.getParent()).removeView(popupPlace);
    }

    public void deletePlace(View v){
        placeName = ((TextView)((View)v.getParent()).findViewById(R.id.d_place_name)).getText().toString();
        dm.deletePlace(dm.readPlace(placeName).get(0));
        menuAdapter.updateList(dm.readPlace());
        menuAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(),"Place deleted.",Toast.LENGTH_LONG).show();
    }

    public void startGlobalScan(View v){
        String placeName = ((TextView)v).getText().toString();
        Place p = dm.readPlace(placeName).get(0);
        ArrayList<Room> rooms = dm.readRoom(p);
        if(rooms.size() == 0){
            Toast.makeText(getActivity(), "You must add rooms before starting a test.", Toast.LENGTH_LONG).show();
        }
        else {
            Intent it = new Intent(getActivity(), GlobalScanActivity.class);
            it.putExtra("place", placeName);
            startActivity(it);
        }
    }

    private void closeKeyboard(){
        View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
