package epl.students.programmers.gridflare;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import epl.students.programmers.gridflare.tools.WifiScanner; //Sera utile pour le live scanning

public class LiveScanningActivity extends Fragment {

    private boolean stop;
    TextView pourcent;

    WifiScanner wifi;

    public static Fragment newInstance() {
        return (new LiveScanningActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.d_live_scanning, container, false);
        stop = false;
        pourcent = v.findViewById(R.id.d_live_value);
        wifi = new WifiScanner(Objects.requireNonNull(getActivity()));
        startLiveScan();
        return v;
    }

    //Apres vérification, la puissance du signal varie entre -30 et -90 (attention en echelle logarithmique meme si on va d'abord test de faire en linéaire)
    private void startLiveScan(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!stop){
                    final int rssi = wifi.update_live_scan();
                    pourcent.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            pourcent.setText(wifi.get_live_numeric_scale() + "%");
                            //realValue.setText(rssi + "dBm");
                            //background.setBackgroundColor(wifi.getLiveColor());
                        }
                    });
                }
            }
        }).start();
    }
}
