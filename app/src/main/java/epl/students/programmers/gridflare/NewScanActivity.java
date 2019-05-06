package epl.students.programmers.gridflare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.tools.Place;
import epl.students.programmers.gridflare.tools.Room;
import epl.students.programmers.gridflare.tools.Scan_information;
import epl.students.programmers.gridflare.tools.WifiScanner;

public class NewScanActivity extends Fragment implements View.OnClickListener{

    BottomNavigationView navigationView;

    AutoCompleteTextView autoComplete;

    TextView ping;
    TextView strength;
    TextView lost;
    TextView dl;
    TextView wifiName;

    TextView workInProgress;

    View popup;

    ImageButton refresh;
    ImageButton save;//Encore faire son listener
                    //Aussi encore faire le text qui s'auto-complete et ducoup la partie save
                    //Et mettre le nom du wifi
                    //Et pq pas remettre l'animation en bas ce serait cool

    WifiScanner wifi;

    public static Fragment newInstance() {
        return (new NewScanActivity());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getView() != null)
            setupAutoCompleteTextView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.d_new_scan, container, false);
        wifi = new WifiScanner(Objects.requireNonNull(getActivity()));
        ping = v.findViewById(R.id.d_ping_new_scan);
        strength = v.findViewById(R.id.d_strength_new_scan);
        lost = v.findViewById(R.id.d_lost_new_scan);
        dl = v.findViewById(R.id.d_dl_new_scan);
        refresh = v.findViewById(R.id.d_refresh_new_scan);
        refresh.setOnClickListener(this);
        save = v.findViewById(R.id.d_validate_new_scan);
        save.setOnClickListener(this);
        wifiName = v.findViewById(R.id.d_wifi_name_new_scan);
        wifiName.setText(wifi.getWifiName());
        workInProgress = v.findViewById(R.id.d_work_in_progress);
        workInProgress.setVisibility(View.GONE);

        popup = v.findViewById(R.id.d_popup_place_selection);
        popup.setVisibility(View.GONE);

        View nextBtn = v.findViewById(R.id.d_next_new_scan);
        ((ViewGroup) nextBtn.getParent()).removeView(nextBtn);

        autoComplete = v.findViewById(R.id.d_text_edit_new_scan);
        setupAutoCompleteTextView();
        launch_test(null);
        return v;
    }

    @SuppressLint("SetTextI18n")
    public void launch_test(View v){
        closeKeyboard();
        Toast.makeText(getActivity(), "Test in progress. Stay where you are!", Toast.LENGTH_LONG).show();
        workInProgress.setText("Scan in progress...");
        workInProgress.setVisibility(View.VISIBLE);

        if(wifi.isWifiDisabled()) {//Check one more time
            openDialog();
        }

        refresh.setEnabled(false);
        save.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                wifi.update();
                ping.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        wifiName.setText(wifi.getWifiName());
                        if (wifi.getPing() == (float)-1 || wifi.getProportionOfLost() == (float)-1 || wifi.getStrength() == (float)-1){

                            ping.setText("_");
                            lost.setText("_");
                            strength.setText(wifi.getStrength() + " %");
                            dl.setText("_");

                            refresh.setEnabled(true);
                            save.setEnabled(true);
                            workInProgress.setText("The connection failed. Try again later.");

                            Toast.makeText(getActivity(), "Error. Check your connection, and try again later.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            ping.setText(Integer.toString((int) wifi.getPing()) + " ms");
                            lost.setText(wifi.getProportionOfLost() + " %");
                            strength.setText(wifi.getStrength() + " %");
                            dl.setText(wifi.getDl() + " Mbps");

                            refresh.setEnabled(true);
                            save.setEnabled(true);
                            workInProgress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    public void setupAutoCompleteTextView(){
        DatabaseManager dm = new DatabaseManager(getActivity());
        ArrayList<Room> rooms = dm.readRoom();
        Room[] names = new Room[rooms.size()];
        for(int i = 0; i < rooms.size(); i++){
            names[i] = rooms.get(i);
        }
        ArrayAdapter<Room> adapter = new ArrayAdapter<Room>(Objects.requireNonNull(getActivity()), android.R.layout.select_dialog_item, names);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);
        dm.close();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.d_refresh_new_scan:
                launch_test(v);
                break;
            case R.id.d_validate_new_scan:
                saveData(v);
                break;
        }
    }

    public void openDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Wi-Fi disabled.");
        alertDialog.setMessage("Do you want turn on your Wi-Fi?");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"Sorry, this app cannot work without Wi-Fi.",Toast.LENGTH_LONG).show();
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
        closeKeyboard();

        //Check if valid scan
        if(ping.getText().toString().equals("_")){
            Toast.makeText(getActivity(), "Can't save an invalid scan.", Toast.LENGTH_LONG).show();
            return;
        }

        String roomName; String placeName; DatabaseManager dm = new DatabaseManager(getActivity());
        ArrayList<Room> rooms;

        try {
            String theRoomTotal = autoComplete.getText().toString();
            // We assume that each room has a place attached to it. Not a very clean way
            String[] splited = theRoomTotal.split("\n\\(");
            roomName = splited[0];
            placeName = splited[1].substring(0, splited[1].length() - 1);
            rooms = dm.readRoom(roomName, placeName);
            Room r = rooms.get(0);// Should be the only one
            Scan_information info = new Scan_information(r, wifi.getStrength(), (int) wifi.getPing(), wifi.getProportionOfLost(), wifi.getDl());
            dm.insertScan(info);
            dm.close();
            Toast.makeText(getActivity(),"Scan saved    ",Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(getActivity(),"This room does not exists",Toast.LENGTH_LONG).show();
            popup.setVisibility(View.VISIBLE);//Still populate it
            ArrayList<Place> places = dm.readPlace();
            LinearLayout     sv = popup.findViewById(R.id.d_place_selection_scroll);
            for(Place p : places){
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View placeView = layoutInflater.inflate(R.layout.template_place_selection, null);
                ((TextView)placeView.findViewById(R.id.d_place_selection_name)).setText(p.getPlace_name());
                sv.addView(placeView);
            }
            dm.close();
        }
    }

    public void placeSelected(View v){
        //Create the new room
        String placeName = ((TextView)v.findViewById(R.id.d_place_selection_name)).getText().toString();
        DatabaseManager dm = new DatabaseManager(getActivity());
        Place p = dm.readPlace(placeName).get(0);
        Room r = new Room(autoComplete.getText().toString(), 0, p);
        dm.insertRoom(r);
        //Create the scan
        Scan_information info = new Scan_information(r, wifi.getStrength(), (int) wifi.getPing(), wifi.getProportionOfLost(), wifi.getDl());
        dm.insertScan(info);
        dm.close();
        //GUI udpate
        ((ViewGroup)popup.findViewById(R.id.d_place_selection_scroll)).removeAllViews();
        popup.setVisibility(View.GONE);
        Toast.makeText(getActivity(),"Scan saved",Toast.LENGTH_LONG).show();
        ((RecyclerView)getActivity().findViewById(R.id.d_places_scroll)).getAdapter().notifyDataSetChanged();
    }

    private void closeKeyboard(){
        View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
